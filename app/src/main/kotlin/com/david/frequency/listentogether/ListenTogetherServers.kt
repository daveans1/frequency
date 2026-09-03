/**
 * vivimusic Project (C) 2026
 * Licensed under GPL-3.0 | See git history for contributors
 */

package com.david.frequency.listentogether

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class ListenTogetherServer(
    val name: String,
    val url: String,
    val location: String,
    val operator: String
)

object ListenTogetherServers {
    private const val ServersJson = """
        [
          {
            "name": "Global Sync",
            "url": "wss://devilmi-vivi-music-listen-together.hf.space",
            "location": "Global",
            "operator": "Frequency"
          },
          {
            "name": "Frequency Sync Server",
            "url": "wss://vivimusic-listen-together.onrender.com",
            "location": "USA",
            "operator": "Frequency"
          }
        ]
    """

    private val json = Json { ignoreUnknownKeys = true }

    val servers: List<ListenTogetherServer> by lazy {
        json.decodeFromString(ServersJson)
    }

    val defaultServerUrl: String
        get() = servers.first().url

    fun findByUrl(url: String): ListenTogetherServer? = servers.firstOrNull { it.url == url }
}
