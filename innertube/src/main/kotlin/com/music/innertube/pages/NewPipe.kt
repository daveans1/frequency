package com.music.innertube

import com.music.innertube.models.response.PlayerResponse
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.toRequestBody
import org.schabi.newpipe.extractor.NewPipe
import org.schabi.newpipe.extractor.downloader.Downloader
import org.schabi.newpipe.extractor.downloader.Request
import org.schabi.newpipe.extractor.downloader.Response
import org.schabi.newpipe.extractor.exceptions.ReCaptchaException
import org.schabi.newpipe.extractor.services.youtube.YoutubeJavaScriptPlayerManager
import org.schabi.newpipe.extractor.stream.StreamInfo
import java.io.IOException

class NewPipeDownloaderImpl : Downloader() {
    private val client = OkHttpClient.Builder()
        .connectTimeout(15, java.util.concurrent.TimeUnit.SECONDS)
        .readTimeout(15, java.util.concurrent.TimeUnit.SECONDS)
        .dns(object : okhttp3.Dns {
            override fun lookup(hostname: String): List<java.net.InetAddress> {
                val addresses = okhttp3.Dns.SYSTEM.lookup(hostname)
                return when (YouTube.ipVersion) {
                    com.music.innertube.models.IpVersion.IPV4 -> addresses.filter { it is java.net.Inet4Address }.ifEmpty { addresses }
                    com.music.innertube.models.IpVersion.IPV6 -> addresses.filter { it is java.net.Inet6Address }.ifEmpty { addresses }
                    com.music.innertube.models.IpVersion.AUTO -> addresses
                }
            }
        })
        .proxySelector(object : java.net.ProxySelector() {
            override fun select(uri: java.net.URI?): List<java.net.Proxy> = listOfNotNull(YouTube.proxy ?: java.net.Proxy.NO_PROXY)
            override fun connectFailed(uri: java.net.URI?, sa: java.net.SocketAddress?, ioe: java.io.IOException?) {}
        })
        .proxyAuthenticator { _, response ->
            YouTube.proxyAuth?.let { auth ->
                response.request.newBuilder()
                    .header("Proxy-Authorization", auth)
                    .build()
            } ?: response.request
        }
        .build()

    @Throws(IOException::class, ReCaptchaException::class)
    override fun execute(request: Request): Response {
        val httpMethod = request.httpMethod()
        val url = request.url()
        val headers = request.headers()
        val dataToSend = request.dataToSend()

        println("[NewPipeDownloader] Executing request: $httpMethod $url")

        val requestBuilder =
            okhttp3.Request
                .Builder()
                .method(httpMethod, dataToSend?.toRequestBody())
                .url(url)
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:140.0) Gecko/20100101 Firefox/140.0")

        headers.forEach { (headerName, headerValueList) ->
            if (headerValueList.size > 1) {
                requestBuilder.removeHeader(headerName)
                headerValueList.forEach { headerValue ->
                    requestBuilder.addHeader(headerName, headerValue)
                }
            } else if (headerValueList.size == 1) {
                requestBuilder.header(headerName, headerValueList[0])
            }
        }

        try {
            val response = client.newCall(requestBuilder.build()).execute()
            println("[NewPipeDownloader] Response code: ${response.code}")

            if (response.code == 429) {
                response.close()
                throw ReCaptchaException("reCaptcha Challenge requested", url)
            }

            val responseBodyToReturn = response.body?.string() ?: ""
            val latestUrl = response.request.url.toString()
            return Response(response.code, response.message, response.headers.toMultimap(), responseBodyToReturn, latestUrl)
        } catch (e: Exception) {
            println("[NewPipeDownloader] Request failed: ${e.message}")
            e.printStackTrace()
            throw e
        }
    }
}

object NewPipeExtractor {
    private var newPipeDownloader: NewPipeDownloaderImpl? = null
    private var isInitialized = false

    fun init() {
        if (!isInitialized) {
            println("[NewPipeExtractor] Initializing NewPipe with Downloader...")
            newPipeDownloader = NewPipeDownloaderImpl()
            NewPipe.init(newPipeDownloader)
            isInitialized = true
        }
    }

    fun getSignatureTimestamp(videoId: String): Result<Int> {
        init()
        return runCatching {
            YoutubeJavaScriptPlayerManager.getSignatureTimestamp(videoId)
        }
    }

    fun getStreamUrl(
        format: PlayerResponse.StreamingData.Format,
        videoId: String
    ): String? {
        init()
        val signatureCipher = format.signatureCipher ?: format.cipher
        return if (!signatureCipher.isNullOrEmpty()) {
            YouTubeExtractor.decryptUrl(signatureCipher)
        } else if (!format.url.isNullOrEmpty()) {
            format.url
        } else {
            null
        }
    }

    fun newPipePlayer(videoId: String): List<Pair<Int, String>> {
        init()
        println("[NewPipeExtractor] Fetching streams for videoId: $videoId")
        return try {
            val streamInfo = StreamInfo.getInfo(
                NewPipe.getService(0),
                "https://www.youtube.com/watch?v=$videoId"
            )
            val streamsList = streamInfo.audioStreams + streamInfo.videoStreams + streamInfo.videoOnlyStreams
            println("[NewPipeExtractor] Successfully fetched ${streamsList.size} streams via fallback")
            streamsList.mapNotNull {
                (it.itagItem?.id ?: return@mapNotNull null) to it.content
            }
        } catch (e: Exception) {
            println("[NewPipeExtractor] Fallback stream extraction failed: ${e.message}")
            e.printStackTrace()
            emptyList()
        }
    }
}
