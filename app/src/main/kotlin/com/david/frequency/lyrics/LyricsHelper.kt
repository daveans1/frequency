/**
 * vivimusic Project (C) 2026
 * Licensed under GPL-3.0 | See git history for contributors
 */

package com.david.frequency.lyrics

import android.content.Context
import android.util.LruCache
import com.david.frequency.constants.LyricsProviderOrderKey
import com.david.frequency.constants.PreferredLyricsProvider
import com.david.frequency.constants.PreferredLyricsProviderKey
import com.david.frequency.db.entities.LyricsEntity.Companion.LYRICS_NOT_FOUND
import com.david.frequency.extensions.toEnum
import com.david.frequency.models.MediaMetadata
import com.david.frequency.utils.NetworkConnectivityObserver
import com.david.frequency.utils.dataStore
import com.david.frequency.utils.reportException
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LyricsHelper
@Inject
constructor(
    @ApplicationContext private val context: Context,
    private val networkConnectivity: NetworkConnectivityObserver,
) {
    /**
     * Resolves the ordered list of lyrics providers from the user's saved priority order.
     * Falls back to migrating the legacy [PreferredLyricsProvider] enum if the new order
     * preference has not been written yet, ensuring a smooth upgrade for existing users.
     */
    private suspend fun resolveLyricsProviders(): List<LyricsProvider> {
        val preferences = context.dataStore.data.first()
        val orderString = preferences[LyricsProviderOrderKey].orEmpty()

        if (orderString.isNotBlank()) {
            return LyricsProviderRegistry.getOrderedProviders(orderString)
        }

        // Migration path: place the old preferred provider first in the default order only if explicitly set
        val oldPrefString = preferences[PreferredLyricsProviderKey]
        if (oldPrefString != null) {
            val preferredEnum = oldPrefString.toEnum(PreferredLyricsProvider.MUSIXMATCH)
            val preferredName = LyricsProviderRegistry.getProviderNameForEnum(preferredEnum)
            val defaultOrder = LyricsProviderRegistry.getDefaultProviderOrder()
            val migratedOrder = listOf(preferredName) + defaultOrder.filter { it != preferredName }
            return migratedOrder.mapNotNull { LyricsProviderRegistry.getProviderByName(it) }
        }

        return LyricsProviderRegistry.getDefaultProviderOrder().mapNotNull { LyricsProviderRegistry.getProviderByName(it) }
    }



    private val cache = LruCache<String, List<LyricsResult>>(MAX_CACHE_SIZE)
    private var currentLyricsJob: Job? = null

    private val helperScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val activeFetches = mutableMapOf<String, Deferred<LyricsWithProvider>>()
    private val fetchesMutex = Mutex()

    suspend fun getLyrics(mediaMetadata: MediaMetadata): LyricsWithProvider {
        currentLyricsJob?.cancel()

        val cached = cache.get(mediaMetadata.id)?.firstOrNull()
        if (cached != null) {
            return LyricsWithProvider(cached.lyrics, cached.providerName)
        }

        // Check network connectivity before making network requests
        // Use synchronous check as fallback if flow doesn't emit
        val isNetworkAvailable = try {
            networkConnectivity.isCurrentlyConnected()
        } catch (e: Exception) {
            // If network check fails, try to proceed anyway
            true
        }
        
        if (!isNetworkAvailable) {
            // Still proceed but return not found to avoid hanging
            return LyricsWithProvider(LYRICS_NOT_FOUND, "Unknown")
        }

        val cacheKey = mediaMetadata.id
        val deferred = fetchesMutex.withLock {
            activeFetches.getOrPut(cacheKey) {
                helperScope.async {
                    val providers = resolveLyricsProviders().filter { it.isEnabled(context) }
                    
                    val deferredResults = providers.map { provider ->
                        provider to async {
                            try {
                                provider.getLyrics(
                                    mediaMetadata.id,
                                    mediaMetadata.title,
                                    mediaMetadata.artists.joinToString { it.name },
                                    mediaMetadata.duration,
                                    mediaMetadata.album?.title,
                                )
                            } catch (e: Exception) {
                                reportException(e)
                                Result.failure(e)
                            }
                        }
                    }

                    var resultWithProvider: LyricsWithProvider? = null

                    for ((provider, deferredResult) in deferredResults) {
                        val result = deferredResult.await()
                        if (result.isSuccess) {
                            val lyrics = result.getOrThrow()
                            if (lyrics.isNotBlank()) {
                                resultWithProvider = LyricsWithProvider(lyrics, provider.name)
                                break
                            }
                        }
                    }
                    
                    // Cancel any still-running requests to save network/resources
                    deferredResults.forEach { it.second.cancel() }
                    
                    resultWithProvider ?: LyricsWithProvider(LYRICS_NOT_FOUND, "Unknown")
                }
            }
        }

        return try {
            val result = deferred.await()
            if (result.lyrics != LYRICS_NOT_FOUND) {
                cache.put(cacheKey, listOf(LyricsResult(result.provider, result.lyrics)))
            }
            result
        } finally {
            fetchesMutex.withLock {
                activeFetches.remove(cacheKey)
            }
        }
    }

    suspend fun getAllLyrics(
        mediaId: String,
        songTitle: String,
        songArtists: String,
        duration: Int,
        album: String? = null,
        callback: (LyricsResult) -> Unit,
    ) {
        currentLyricsJob?.cancel()

        val cacheKey = "$songArtists-$songTitle".replace(" ", "")
        cache.get(cacheKey)?.let { results ->
            results.forEach {
                callback(it)
            }
            return
        }

        // Check network connectivity before making network requests
        // Use synchronous check as fallback if flow doesn't emit
        val isNetworkAvailable = try {
            networkConnectivity.isCurrentlyConnected()
        } catch (e: Exception) {
            // If network check fails, try to proceed anyway
            true
        }
        
        if (!isNetworkAvailable) {
            // Still try to proceed in case of false negative
            return
        }

        val allResult = mutableListOf<LyricsResult>()
        val providers = resolveLyricsProviders()
        currentLyricsJob = CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            val jobs = providers.filter { it.isEnabled(context) }.map { provider ->
                launch {
                    try {
                        provider.getAllLyrics(mediaId, songTitle, songArtists, duration, album) { lyrics ->
                            if (lyrics.isNotBlank()) {
                                val result = LyricsResult(provider.name, lyrics)
                                synchronized(allResult) {
                                    allResult.add(result)
                                }
                                callback(result)
                            }
                        }
                    } catch (e: Exception) {
                        // Catch network-related exceptions like UnresolvedAddressException
                        reportException(e)
                    }
                }
            }
            jobs.forEach { it.join() }
            cache.put(cacheKey, allResult)
        }

        currentLyricsJob?.join()
    }

    fun cancelCurrentLyricsJob() {
        currentLyricsJob?.cancel()
        currentLyricsJob = null
    }

    companion object {
        private const val MAX_CACHE_SIZE = 3
    }
}

data class LyricsResult(
    val providerName: String,
    val lyrics: String,
)

data class LyricsWithProvider(
    val lyrics: String,
    val provider: String,
)