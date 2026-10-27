/**
 * vivimusic Project (C) 2026
 * Licensed under GPL-3.0 | See git history for contributors
 */

package com.david.frequency.ui.screens.settings

import android.content.Context
import androidx.annotation.DrawableRes
import com.david.frequency.R

data class SettingSearchEntry(
    val title: String,
    val description: String? = null,
    val category: String,
    val route: String,
    @DrawableRes val icon: Int,
    val keywords: List<String> = emptyList(),
)

object SettingsSearchRegistry {

    fun getEntries(context: Context): List<SettingSearchEntry> = listOf(
        // --- Appearance & UI ---
        SettingSearchEntry(
            title = context.getString(R.string.theme),
            description = context.getString(R.string.theme_desc),
            category = context.getString(R.string.appearance),
            route = "settings/appearance/theme",
            icon = R.drawable.palette,
            keywords = listOf("theme", "dark", "light", "amoled", "pure black", "color", "accent", "style")
        ),
        SettingSearchEntry(
            title = context.getString(R.string.app_font),
            description = "Choose app typography and font family",
            category = context.getString(R.string.appearance),
            route = "settings/appearance/font",
            icon = R.drawable.alphabet_cyrillic,
            keywords = listOf("font", "typography", "text", "typeface", "custom font")
        ),
        SettingSearchEntry(
            title = context.getString(R.string.mini_player),
            description = "Classic, New, or Apple Music mini-player design",
            category = context.getString(R.string.appearance),
            route = "settings/appearance",
            icon = R.drawable.nav_bar,
            keywords = listOf("mini player", "dock", "bottom bar", "classic", "apple style")
        ),
        SettingSearchEntry(
            title = context.getString(R.string.player_design),
            description = "Player artwork style, background, and seekbar design",
            category = context.getString(R.string.appearance),
            route = "settings/appearance",
            icon = R.drawable.play,
            keywords = listOf("player", "design", "artwork", "glow", "canvas", "visual")
        ),
        SettingSearchEntry(
            title = context.getString(R.string.default_open_tab),
            description = "Choose default screen when opening Vivi Music",
            category = context.getString(R.string.appearance),
            route = "settings/appearance",
            icon = R.drawable.nav_bar,
            keywords = listOf("default tab", "start tab", "home", "search", "library", "launch")
        ),
        SettingSearchEntry(
            title = context.getString(R.string.display_density),
            description = "Adjust UI element scale and sizing",
            category = context.getString(R.string.appearance),
            route = "settings/appearance",
            icon = R.drawable.grid_view,
            keywords = listOf("density", "display", "scale", "ui size", "compact")
        ),
        SettingSearchEntry(
            title = context.getString(R.string.grid_cell_size),
            description = "Choose grid item size across the app",
            category = context.getString(R.string.appearance),
            route = "settings/appearance",
            icon = R.drawable.grid_view,
            keywords = listOf("grid", "item size", "big", "small", "layout")
        ),
        SettingSearchEntry(
            title = context.getString(R.string.slim_navbar),
            description = "Compact bottom navigation bar style",
            category = context.getString(R.string.appearance),
            route = "settings/appearance",
            icon = R.drawable.nav_bar,
            keywords = listOf("slim", "navbar", "navigation", "compact bar")
        ),
        SettingSearchEntry(
            title = context.getString(R.string.floating_navbar),
            description = context.getString(R.string.floating_navbar_desc),
            category = context.getString(R.string.appearance),
            route = "settings/appearance",
            icon = R.drawable.nav_bar,
            keywords = listOf("floating", "navbar", "dock", "pill")
        ),

        // --- Player & Audio ---
        SettingSearchEntry(
            title = context.getString(R.string.streaming_audio_quality),
            description = "Max (320kbps), High, Medium, or Low audio streaming",
            category = context.getString(R.string.player_and_audio),
            route = "settings/player",
            icon = R.drawable.play,
            keywords = listOf("audio quality", "bitrate", "320kbps", "streaming quality", "audio", "max")
        ),
        SettingSearchEntry(
            title = context.getString(R.string.download_audio_quality),
            description = "Quality for offline song downloads (up to 320kbps)",
            category = context.getString(R.string.player_and_audio),
            route = "settings/player",
            icon = R.drawable.download,
            keywords = listOf("download quality", "offline bitrate", "320kbps", "save offline")
        ),
        SettingSearchEntry(
            title = context.getString(R.string.data_saver),
            description = context.getString(R.string.data_saver_desc),
            category = context.getString(R.string.player_and_audio),
            route = "settings/player",
            icon = R.drawable.energy_savings_leaf,
            keywords = listOf("data saver", "bandwidth", "canvas", "video background", "disable canvas")
        ),
        SettingSearchEntry(
            title = context.getString(R.string.vivi_equalizer),
            description = context.getString(R.string.vivi_equalizer_desc),
            category = context.getString(R.string.player_and_audio),
            route = "settings/equalizer",
            icon = R.drawable.frequencyequlizer,
            keywords = listOf("equalizer", "eq", "bass", "treble", "sound", "audio fx", "presets")
        ),
        SettingSearchEntry(
            title = context.getString(R.string.audio_normalization),
            description = "Adjust audio volume to a standard level across songs",
            category = context.getString(R.string.player_and_audio),
            route = "settings/player",
            icon = R.drawable.volume_up,
            keywords = listOf("audio normalization", "volume", "loudness", "leveling", "replaygain")
        ),
        SettingSearchEntry(
            title = context.getString(R.string.skip_silence),
            description = context.getString(R.string.skip_silence_desc),
            category = context.getString(R.string.player_and_audio),
            route = "settings/player",
            icon = R.drawable.fast_forward,
            keywords = listOf("skip silence", "silent audio", "gapless", "skip")
        ),
        SettingSearchEntry(
            title = context.getString(R.string.auto_load_more),
            description = "Automatically queue similar songs when playlist ends",
            category = context.getString(R.string.player_and_audio),
            route = "settings/player",
            icon = R.drawable.playlist_add,
            keywords = listOf("auto load more", "infinite queue", "radio", "autoplay")
        ),
        SettingSearchEntry(
            title = context.getString(R.string.auto_download_on_like),
            description = "Automatically download songs when added to Favorites",
            category = context.getString(R.string.player_and_audio),
            route = "settings/player",
            icon = R.drawable.download,
            keywords = listOf("auto download", "favorite", "like", "offline save")
        ),
        SettingSearchEntry(
            title = context.getString(R.string.stop_music_on_task_clear),
            description = "Stop audio playback when clearing app from recent tasks",
            category = context.getString(R.string.player_and_audio),
            route = "settings/player",
            icon = R.drawable.close,
            keywords = listOf("stop music", "task clear", "kill app", "background playback")
        ),
        SettingSearchEntry(
            title = context.getString(R.string.pause_music_when_media_is_muted),
            description = "Pause playback when volume reaches zero",
            category = context.getString(R.string.player_and_audio),
            route = "settings/player",
            icon = R.drawable.volume_off,
            keywords = listOf("pause on mute", "zero volume", "silence")
        ),

        // --- Lyrics ---
        SettingSearchEntry(
            title = context.getString(R.string.lyrics_provider_priority),
            description = context.getString(R.string.lyrics_provider_priority_desc),
            category = context.getString(R.string.lyrics),
            route = "settings/lyrics",
            icon = R.drawable.lyrics,
            keywords = listOf("lyrics", "youlyplus", "musixmatch", "lrclib", "kugou", "betterlyrics", "providers", "priority", "order")
        ),
        SettingSearchEntry(
            title = context.getString(R.string.ai_lyrics_translation),
            description = "Translate lyrics using OpenRouter, DeepL, OpenAI, Claude, Gemini",
            category = context.getString(R.string.lyrics),
            route = "settings/lyrics",
            icon = R.drawable.translate,
            keywords = listOf("ai translation", "ai lyrics", "openrouter", "deepl", "openai", "claude", "gemini", "translate")
        ),
        SettingSearchEntry(
            title = context.getString(R.string.lyrics_animation_style),
            description = "Apple Music, Karaoke, Fluid, Fade, or Slide style lyrics",
            category = context.getString(R.string.lyrics),
            route = "settings/lyrics",
            icon = R.drawable.lyrics,
            keywords = listOf("lyrics animation", "karaoke", "apple music", "fade", "fluid", "letter by letter")
        ),
        SettingSearchEntry(
            title = context.getString(R.string.lyrics_text_size),
            description = "Adjust lyrics font size and line spacing",
            category = context.getString(R.string.lyrics),
            route = "settings/lyrics",
            icon = R.drawable.lyrics,
            keywords = listOf("lyrics size", "text size", "font size", "line spacing", "lyrics font")
        ),
        SettingSearchEntry(
            title = context.getString(R.string.lyrics_glow_effect),
            description = context.getString(R.string.lyrics_glow_effect_desc),
            category = context.getString(R.string.lyrics),
            route = "settings/lyrics",
            icon = R.drawable.lyrics,
            keywords = listOf("lyrics glow", "glow effect", "blur", "apple music blur")
        ),
        SettingSearchEntry(
            title = context.getString(R.string.lyrics_swipe_to_change_song),
            description = context.getString(R.string.lyrics_swipe_to_change_song_desc),
            category = context.getString(R.string.lyrics),
            route = "settings/lyrics",
            icon = R.drawable.swipe,
            keywords = listOf("lyrics swipe", "gesture", "next song", "previous song")
        ),

        // --- Content & Library ---
        SettingSearchEntry(
            title = context.getString(R.string.content_language),
            description = "Set music recommendations language and region",
            category = context.getString(R.string.content),
            route = "settings/content",
            icon = R.drawable.language,
            keywords = listOf("content language", "app language", "region", "country", "recommendations")
        ),
        SettingSearchEntry(
            title = context.getString(R.string.hide_explicit),
            description = "Filter out songs marked with explicit tags",
            category = context.getString(R.string.content),
            route = "settings/content",
            icon = R.drawable.security,
            keywords = listOf("explicit", "clean", "parental", "curation", "filter")
        ),
        SettingSearchEntry(
            title = context.getString(R.string.hide_video_songs),
            description = "Hide unofficial or music video versions of songs",
            category = context.getString(R.string.content),
            route = "settings/content",
            icon = R.drawable.slow_motion_video,
            keywords = listOf("video songs", "music videos", "audio only", "hide videos")
        ),
        SettingSearchEntry(
            title = context.getString(R.string.hide_youtube_shorts),
            description = "Hide YouTube Shorts videos from search and explore",
            category = context.getString(R.string.content),
            route = "settings/content",
            icon = R.drawable.slow_motion_video,
            keywords = listOf("shorts", "youtube shorts", "filter shorts")
        ),
        SettingSearchEntry(
            title = context.getString(R.string.show_liked_playlist),
            description = "Show Liked Songs auto-playlist in Library",
            category = context.getString(R.string.content),
            route = "settings/content",
            icon = R.drawable.favorite,
            keywords = listOf("liked playlist", "auto playlists", "downloaded playlist", "top playlist", "cached playlist")
        ),
        SettingSearchEntry(
            title = context.getString(R.string.lyrics_romanization),
            description = "Convert Japanese Romaji, Korean Hangul, or Chinese Pinyin",
            category = context.getString(R.string.content),
            route = "settings/content/romanization",
            icon = R.drawable.translate,
            keywords = listOf("romanization", "romaji", "pinyin", "hangul", "pronunciation", "lyrics")
        ),

        // --- Account & Connected Services ---
        SettingSearchEntry(
            title = context.getString(R.string.account),
            description = "Sign in with YouTube Music account, manage cookies and sync",
            category = context.getString(R.string.account),
            route = "settings/account",
            icon = R.drawable.google,
            keywords = listOf("account", "login", "google", "youtube music", "cookie", "sync", "visitor data")
        ),
        SettingSearchEntry(
            title = context.getString(R.string.discord_integration),
            description = "Display currently playing song on your Discord profile",
            category = context.getString(R.string.account),
            route = "settings/integrations/discord",
            icon = R.drawable.discord,
            keywords = listOf("discord", "rpc", "presence", "rich presence", "status", "game status")
        ),
        SettingSearchEntry(
            title = context.getString(R.string.lastfm_integration),
            description = "Scrobble listening history to Last.fm",
            category = context.getString(R.string.account),
            route = "settings/integrations/lastfm",
            icon = R.drawable.music_note,
            keywords = listOf("lastfm", "last.fm", "scrobble", "stats", "history")
        ),
        SettingSearchEntry(
            title = context.getString(R.string.listen_together),
            description = "Listen to music in sync with friends in real-time rooms",
            category = context.getString(R.string.account),
            route = "settings/integrations/listen_together",
            icon = R.drawable.group,
            keywords = listOf("listen together", "party", "room", "share music", "collaborative", "session")
        ),

        // --- Storage & Backup ---
        SettingSearchEntry(
            title = context.getString(R.string.storage),
            description = "Manage song and image cache sizes, clear downloaded data",
            category = context.getString(R.string.storage),
            route = "settings/storage",
            icon = R.drawable.storage,
            keywords = listOf("storage", "cache", "song cache", "image cache", "clear cache", "disk space")
        ),
        SettingSearchEntry(
            title = context.getString(R.string.backup_restore),
            description = "Export and restore your playlists, history, and settings",
            category = context.getString(R.string.storage),
            route = "settings/backup_restore",
            icon = R.drawable.restore,
            keywords = listOf("backup", "restore", "export backup", "import backup", "save data")
        ),
        SettingSearchEntry(
            title = context.getString(R.string.autobackup_settings),
            description = "Schedule automatic recurring backups to local storage",
            category = context.getString(R.string.storage),
            route = "settings/backup_restore/autobackup",
            icon = R.drawable.database_upload,
            keywords = listOf("auto backup", "scheduled backup", "automatic backup")
        ),
        SettingSearchEntry(
            title = context.getString(R.string.import_from_spotify),
            description = "Transfer public Spotify playlists directly to Vivi Music",
            category = context.getString(R.string.storage),
            route = "settings/spotify",
            icon = R.drawable.spotify,
            keywords = listOf("spotify", "spotify import", "playlists", "transfer")
        ),
        SettingSearchEntry(
            title = context.getString(R.string.import_csv),
            description = "Import playlists from CSV spreadsheets or M3U files",
            category = context.getString(R.string.storage),
            route = "settings/backup_restore",
            icon = R.drawable.playlist_add,
            keywords = listOf("csv", "m3u", "import playlist", "spreadsheet")
        ),

        // --- About & System ---
        SettingSearchEntry(
            title = context.getString(R.string.system_update),
            description = "Check for app updates, beta releases, and changelog",
            category = context.getString(R.string.about),
            route = "settings/update",
            icon = R.drawable.network_update,
            keywords = listOf("update", "check update", "version", "new release", "beta update", "nightly")
        ),
        SettingSearchEntry(
            title = context.getString(R.string.privacy),
            description = "Pause listening/search history, disable screenshots",
            category = context.getString(R.string.about),
            route = "settings/privacy",
            icon = R.drawable.security,
            keywords = listOf("privacy", "listen history", "search history", "pause history", "screenshot")
        ),
        SettingSearchEntry(
            title = context.getString(R.string.proxy),
            description = "Configure custom HTTP/SOCKS network proxy and IP versions",
            category = context.getString(R.string.about),
            route = "settings/content",
            icon = R.drawable.network_node,
            keywords = listOf("proxy", "http", "socks", "ip version", "ipv4", "ipv6", "network")
        ),
        SettingSearchEntry(
            title = context.getString(R.string.about),
            description = "App version, license, developer info, and community links",
            category = context.getString(R.string.about),
            route = "settings/about",
            icon = R.drawable.info,
            keywords = listOf("about", "version", "github", "telegram", "developer", "license", "donate")
        ),
    )

    fun search(query: String, context: Context): List<SettingSearchEntry> {
        val q = query.trim().lowercase()
        if (q.isBlank()) return emptyList()
        val all = getEntries(context)
        return all.filter { entry ->
            entry.title.lowercase().contains(q) ||
            (entry.description?.lowercase()?.contains(q) == true) ||
            entry.category.lowercase().contains(q) ||
            entry.keywords.any { it.lowercase().contains(q) }
        }
    }
}
