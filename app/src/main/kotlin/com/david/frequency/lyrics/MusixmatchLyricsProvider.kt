/**
 * vivimusic Project (C) 2026
 * Licensed under GPL-3.0 | See git history for contributors
 */

package com.david.frequency.lyrics

import android.content.Context
import com.music.musixmatch.Musixmatch
import com.david.frequency.constants.EnableMusixmatchKey
import com.david.frequency.utils.dataStore
import com.david.frequency.utils.get

object MusixmatchLyricsProvider : LyricsProvider {
    override val name = "Musixmatch"

    override fun isEnabled(context: Context): Boolean =
        context.dataStore[EnableMusixmatchKey] ?: true

    override suspend fun getLyrics(
        id: String,
        title: String,
        artist: String,
        duration: Int,
        album: String?,
    ): Result<String> =
        Musixmatch.getLyrics(title, artist, duration, album)

    override suspend fun getAllLyrics(
        id: String,
        title: String,
        artist: String,
        duration: Int,
        album: String?,
        callback: (String) -> Unit
    ) {
        Musixmatch.getAllLyrics(title, artist, duration, album, callback)
    }
}
