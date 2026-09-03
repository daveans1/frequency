path = 'app/src/main/kotlin/com/david/frequency/widget/MetrolistWidgetManager.kt'
with open(path, 'r', encoding='utf-8') as f:
    content = f.read()

# 1. Update updateWidgets to also update lens widgets.
update_widgets_find = '''        updateTurntableWidgets(
            title = title,
            artist = artist,
            albumArt = albumArt,
            isPlaying = isPlaying
        )
    }'''
    
update_widgets_replace = '''        updateTurntableWidgets(
            title = title,
            artist = artist,
            albumArt = albumArt,
            isPlaying = isPlaying
        )
        
        updateLensWidgets(
            title = title,
            artist = artist,
            albumArt = albumArt,
            isPlaying = isPlaying,
            isLiked = isLiked,
            duration = duration,
            currentPosition = currentPosition
        )
    }'''

content = content.replace(update_widgets_find, update_widgets_replace)

# 2. Add updateLensWidgets method and createLensRemoteViews
lens_methods = '''
    // LENS WIDGET STATE
    private var lensControlsVisible = false
    private var lensControlsTimerJob: kotlinx.coroutines.Job? = null
    
    fun setLensControlsVisible(visible: Boolean) {
        lensControlsVisible = visible
    }

    private fun updateLensWidgets(
        title: String,
        artist: String,
        albumArt: Bitmap?,
        isPlaying: Boolean,
        isLiked: Boolean,
        duration: Long,
        currentPosition: Long
    ) {
        val appWidgetManager = AppWidgetManager.getInstance(context)
        val lensWidgetComponent = ComponentName(context, LensWidgetReceiver::class.java)
        val lensWidgetIds = appWidgetManager.getAppWidgetIds(lensWidgetComponent)

        if (lensWidgetIds.isEmpty()) return

        val views = createLensRemoteViews(
            title = title,
            artist = artist,
            albumArt = albumArt,
            isPlaying = isPlaying,
            isLiked = isLiked,
            duration = duration,
            currentPosition = currentPosition
        )

        appWidgetManager.updateAppWidget(lensWidgetIds, views)
    }

    private fun createLensRemoteViews(
        title: String,
        artist: String,
        albumArt: Bitmap?,
        isPlaying: Boolean,
        isLiked: Boolean,
        duration: Long,
        currentPosition: Long
    ): RemoteViews {
        val views = RemoteViews(context.packageName, R.layout.widget_lens)

        // Song Info
        views.setTextViewText(R.id.widget_lens_song_title, title)
        views.setTextViewText(R.id.widget_lens_artist_name, artist)

        // Album Art (Full bleed)
        if (albumArt != null) {
            views.setImageViewBitmap(R.id.widget_lens_art, albumArt)
        } else {
            views.setImageViewResource(R.id.widget_lens_art, R.mipmap.ic_launcher)
        }
        
        // Background toggle intent (tap album art to show/hide controls)
        views.setOnClickPendingIntent(R.id.widget_lens_art, getLensToggleControlsIntent())

        // Controls visibility
        val controlsVisibility = if (lensControlsVisible) android.view.View.VISIBLE else android.view.View.GONE
        views.setViewVisibility(R.id.widget_lens_controls_container, controlsVisibility)

        // Play/Pause button
        views.setImageViewResource(
            R.id.widget_lens_play_pause,
            if (isPlaying) R.drawable.ic_widget_pause else R.drawable.ic_widget_play
        )
        views.setOnClickPendingIntent(R.id.widget_lens_play_pause, getLensPlayPauseIntent())

        // Like button
        views.setImageViewResource(
            R.id.widget_lens_like_button,
            if (isLiked) R.drawable.ic_widget_heart_nav else R.drawable.ic_widget_heart_outline_nav
        )
        views.setOnClickPendingIntent(R.id.widget_lens_like_button, getLensLikeIntent())

        // Next/Prev buttons
        views.setOnClickPendingIntent(R.id.widget_lens_next_button, getLensNextIntent())
        views.setOnClickPendingIntent(R.id.widget_lens_prev_button, getLensPrevIntent())

        // Progress bar (similar to compact_wide)
        val progress = if (duration > 0) {
            (currentPosition.toFloat() / duration.toFloat()) * 10000
        } else 0f
        views.setInt(R.id.widget_lens_progress_fill, "setLevel", progress.toInt())

        return views
    }
    
    private fun getLensToggleControlsIntent(): PendingIntent {
        val intent = Intent(context, LensWidgetReceiver::class.java).apply {
            action = LensWidgetReceiver.ACTION_TOGGLE_CONTROLS
        }
        return PendingIntent.getBroadcast(
            context,
            120,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun getLensPlayPauseIntent(): PendingIntent {
        val intent = Intent(context, LensWidgetReceiver::class.java).apply {
            action = LensWidgetReceiver.ACTION_PLAY_PAUSE
        }
        return PendingIntent.getBroadcast(
            context,
            121,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun getLensLikeIntent(): PendingIntent {
        val intent = Intent(context, LensWidgetReceiver::class.java).apply {
            action = LensWidgetReceiver.ACTION_LIKE
        }
        return PendingIntent.getBroadcast(
            context,
            122,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun getLensNextIntent(): PendingIntent {
        val intent = Intent(context, LensWidgetReceiver::class.java).apply {
            action = LensWidgetReceiver.ACTION_NEXT
        }
        return PendingIntent.getBroadcast(
            context,
            123,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun getLensPrevIntent(): PendingIntent {
        val intent = Intent(context, LensWidgetReceiver::class.java).apply {
            action = LensWidgetReceiver.ACTION_PREVIOUS
        }
        return PendingIntent.getBroadcast(
            context,
            124,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }
'''

content = content.replace('private fun getPlayPauseIntent(): PendingIntent {', lens_methods + '\n    private fun getPlayPauseIntent(): PendingIntent {')

with open(path, 'w', encoding='utf-8') as f:
    f.write(content)
