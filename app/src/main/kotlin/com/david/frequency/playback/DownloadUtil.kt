/**
 * vivimusic Project (C) 2026
 * Licensed under GPL-3.0 | See git history for contributors
 */

package com.david.frequency.playback

import coil3.SingletonImageLoader
import coil3.request.CachePolicy
import coil3.request.ImageRequest

import android.content.Context
import android.net.ConnectivityManager
import androidx.core.content.getSystemService
import androidx.core.net.toUri
import androidx.media3.database.DatabaseProvider
import androidx.media3.datasource.ResolvingDataSource
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.datasource.cache.SimpleCache
import androidx.media3.datasource.okhttp.OkHttpDataSource
import androidx.media3.exoplayer.offline.Download
import androidx.media3.exoplayer.offline.DownloadManager
import androidx.media3.exoplayer.offline.DownloadService
import androidx.media3.exoplayer.offline.DownloadNotificationHelper
import com.music.innertube.YouTube
import com.david.frequency.constants.AudioQuality
import com.david.frequency.constants.DownloadAudioQualityKey
import com.david.frequency.constants.IpVersionKey
import com.music.innertube.models.IpVersion
import okhttp3.Dns
import java.net.InetAddress
import java.net.Inet4Address
import java.net.Inet6Address
import com.david.frequency.db.MusicDatabase
import com.david.frequency.db.entities.FormatEntity
import com.david.frequency.db.entities.SongEntity
import com.david.frequency.di.DownloadCache
import com.david.frequency.di.PlayerCache
import com.david.frequency.ui.utils.resize
import com.david.frequency.utils.YTPlayerUtils
import com.david.frequency.utils.enumPreference
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import java.time.LocalDateTime
import java.util.concurrent.Executor
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DownloadUtil
@Inject
constructor(
    @ApplicationContext context: Context,
    val database: MusicDatabase,
    val databaseProvider: DatabaseProvider,
    @DownloadCache val downloadCache: SimpleCache,
    @PlayerCache val playerCache: SimpleCache,
) {
    private val connectivityManager = context.getSystemService<ConnectivityManager>()!!
    private val downloadAudioQuality by enumPreference(context, DownloadAudioQualityKey, AudioQuality.MEDIUM)
    private val ipVersion by enumPreference(context, IpVersionKey, IpVersion.AUTO)

    // Keyed by (mediaId::qualityName) so that a Saavn URL cached at 128kbps is never
    // mistakenly served when the user later downloads the same track at 320kbps.
    private val songUrlCache = HashMap<String, Pair<String, Long>>()

    /** Build a cache key that encodes both track and download quality to prevent cross-tier collisions. */
    private fun urlCacheKey(mediaId: String) = "$mediaId::${downloadAudioQuality.name}"

    private val appContext: Context = context

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    val downloads = MutableStateFlow<Map<String, Download>>(emptyMap())

    private val dataSourceFactory =
        ResolvingDataSource.Factory(
            CacheDataSource
                .Factory()
                .setCache(playerCache)
                .setUpstreamDataSourceFactory(
                    OkHttpDataSource.Factory(
                        OkHttpClient.Builder()
                            .dns(object : Dns {
                                override fun lookup(hostname: String): List<InetAddress> {
                                    val addresses = Dns.SYSTEM.lookup(hostname)
                                    return when (this@DownloadUtil.ipVersion) {
                                        IpVersion.IPV4 -> addresses.filter { it is Inet4Address }.ifEmpty { addresses }
                                        IpVersion.IPV6 -> addresses.filter { it is Inet6Address }.ifEmpty { addresses }
                                        IpVersion.AUTO -> addresses
                                    }
                                }
                            })
                            .proxy(YouTube.proxy)
                            .proxyAuthenticator { _, response ->
                                YouTube.proxyAuth?.let { auth ->
                                    response.request.newBuilder()
                                        .header("Proxy-Authorization", auth)
                                        .build()
                                } ?: response.request
                            }
                            .connectTimeout(15, java.util.concurrent.TimeUnit.SECONDS)
                            .readTimeout(15, java.util.concurrent.TimeUnit.SECONDS)
                            .build(),
                    ).setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:140.0) Gecko/20100101 Firefox/140.0"),
                ),
        ) { dataSpec ->
            val mediaId = dataSpec.key ?: error("No media id")
            val length = if (dataSpec.length >= 0) dataSpec.length else 1

            // Only use the player cache if the cached format bitrate meets the download quality target.
            // If the cached stream is lower quality (e.g. streamed on LOW, downloading on MAX),
            // skip the cache entirely and fetch a fresh high-quality stream from the network.
            if (playerCache.isCached(mediaId, dataSpec.position, length)) {
                val cachedFormat = runBlocking(Dispatchers.IO) {
                    database.format(mediaId).firstOrNull()
                }
                if (cachedFormat == null || cachedFormat.bitrate >= downloadAudioQuality.targetDownloadBitrate) {
                    return@Factory dataSpec
                }
                // else: fall through and fetch a fresh stream at the correct quality
            }

            // Use a quality-scoped key so a URL cached at a different quality tier is never
            // served here — the root cause of "downloaded a totally different/wrong song".
            songUrlCache[urlCacheKey(mediaId)]?.takeIf { it.second > System.currentTimeMillis() }?.let { (cachedUrl, _) ->
                var newSpec = dataSpec.withUri(cachedUrl.toUri())
                val cl = runBlocking(Dispatchers.IO) { database.format(mediaId).firstOrNull()?.contentLength }
                if (dataSpec.length < 0 && cl != null && cl > 0L) {
                    val remaining = cl - dataSpec.position
                    if (remaining > 0L) {
                        newSpec = newSpec.buildUpon().setLength(remaining).build()
                    }
                }
                return@Factory newSpec
            }

            val playbackData = runBlocking(Dispatchers.IO) {
                val songObj = database.song(mediaId).firstOrNull()
                val knownTitle = songObj?.song?.title
                val knownArtist = songObj?.artists?.joinToString(", ") { it.name }
                val expectedDuration = songObj?.song?.duration

                YTPlayerUtils.playerResponseForPlayback(
                    mediaId,
                    audioQuality = downloadAudioQuality,
                    connectivityManager = connectivityManager,
                    isDownload = true,
                    context = appContext,
                    knownTitle = knownTitle,
                    knownArtist = knownArtist,
                    expectedDuration = expectedDuration
                )
            }.getOrThrow()
            val format = playbackData.format

            database.query {
                upsert(
                    FormatEntity(
                        id = mediaId,
                        itag = format.itag,
                        mimeType = format.mimeType.split(";")[0],
                        codecs = format.mimeType.split("codecs=").getOrNull(1)?.removeSurrounding("\"") ?: "mp4a.40.2",
                        bitrate = format.bitrate,
                        sampleRate = format.audioSampleRate,
                        contentLength = format.contentLength ?: 0L,
                        loudnessDb = playbackData.audioConfig?.loudnessDb,
                        perceptualLoudnessDb = playbackData.audioConfig?.perceptualLoudnessDb,
                        playbackUrl = playbackData.playbackTracking?.videostatsPlaybackUrl?.baseUrl
                    ),
                )

                val now = LocalDateTime.now()
                val existing = getSongByIdBlocking(mediaId)?.song

                val updatedSong = if (existing != null) {
                    if (existing.dateDownload == null) {
                        existing.copy(dateDownload = now)
                    } else {
                        existing
                    }
                } else {
                    SongEntity(
                        id = mediaId,
                        title = playbackData.videoDetails?.title ?: "Unknown",
                        duration = playbackData.videoDetails?.lengthSeconds?.toIntOrNull() ?: 0,
                        thumbnailUrl = playbackData.videoDetails?.thumbnail?.thumbnails?.lastOrNull()?.url?.resize(1200, 1200),
                        dateDownload = now,
                        isDownloaded = false
                    )
                }

                upsert(updatedSong)

                // Pre-cache the high-res thumbnail immediately when download starts
                updatedSong.thumbnailUrl?.let { url ->
                    val request = ImageRequest.Builder(context)
                        .data(url)
                        .memoryCachePolicy(CachePolicy.ENABLED)
                        .diskCachePolicy(CachePolicy.ENABLED)
                        .build()
                    SingletonImageLoader.get(context).enqueue(request)
                }
            }

            // For YouTube streams: append the &range= param so the download cache can
            // handle progressive HTTP range requests. For JioSaavn streams the CDN
            // natively supports progressive range requests without it.
            val streamUrl = if (playbackData.isSaavnStream) {
                playbackData.streamUrl
            } else {
                "${playbackData.streamUrl}&range=0-${format.contentLength ?: 10_000_000}"
            }

            songUrlCache[urlCacheKey(mediaId)] = streamUrl to (System.currentTimeMillis() + (playbackData.streamExpiresInSeconds * 1000L))

            var newSpec = dataSpec.withUri(streamUrl.toUri())
            val cl = format.contentLength
            if (dataSpec.length < 0 && cl != null && cl > 0L) {
                val remaining = cl - dataSpec.position
                if (remaining > 0L) {
                    newSpec = newSpec.buildUpon().setLength(remaining).build()
                }
            }
            newSpec
        }


    val downloadNotificationHelper =
        DownloadNotificationHelper(context, ExoDownloadService.CHANNEL_ID)

    @OptIn(DelicateCoroutinesApi::class)
    val downloadManager: DownloadManager = run {
        // Workaround for ExoPlayer issue where downloads can get permanently stuck in STATE_REMOVING
        try {
            val db = databaseProvider.writableDatabase
            db.delete("ExoPlayerDownloads", "state = ?", arrayOf(Download.STATE_REMOVING.toString()))
        } catch (e: Exception) {
            e.printStackTrace()
        }

        DownloadManager(
            context,
            databaseProvider,
            downloadCache,
            dataSourceFactory,
            java.util.concurrent.Executors.newCachedThreadPool()
        ).apply {
            maxParallelDownloads = 3
            addListener(
                object : DownloadManager.Listener {
                    override fun onDownloadChanged(
                        downloadManager: DownloadManager,
                        download: Download,
                        finalException: Exception?,
                    ) {
                        downloads.update { map ->
                            map.toMutableMap().apply {
                                set(download.request.id, download)
                            }
                        }

                        scope.launch {
                            when (download.state) {
                                Download.STATE_COMPLETED -> {
                                    database.updateDownloadedInfo(download.request.id, true, LocalDateTime.now())
                                }
                                Download.STATE_FAILED,
                                Download.STATE_STOPPED,
                                Download.STATE_REMOVING -> {
                                    database.updateDownloadedInfo(download.request.id, false, null)
                                }
                                else -> {
                                }
                            }
                        }
                    }

                    override fun onDownloadRemoved(
                        downloadManager: DownloadManager,
                        download: Download
                    ) {
                        downloads.update { map ->
                            map.toMutableMap().apply {
                                remove(download.request.id)
                            }
                        }
                    }
                }
            )
        }
    }

    init {
        val result = mutableMapOf<String, Download>()
        val cursor = downloadManager.downloadIndex.getDownloads()
        var hasUnfinished = false
        while (cursor.moveToNext()) {
            val download = cursor.download
            result[download.request.id] = download
            if (download.state == Download.STATE_QUEUED || download.state == Download.STATE_DOWNLOADING || download.state == Download.STATE_STOPPED) {
                hasUnfinished = true
            }
        }
        downloads.value = result
        
        if (hasUnfinished) {
            try {
                DownloadService.start(context, ExoDownloadService::class.java)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getDownload(songId: String): Flow<Download?> = downloads.map { it[songId] }

    fun release() {
        scope.cancel()
    }
}
