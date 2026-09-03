package com.david.frequency.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.david.frequency.R
import com.david.frequency.playback.MusicService

class MoodGridWidgetReceiver : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }
    
    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action?.startsWith(ACTION_PLAY_MOOD) == true) {
            val playlistId = intent.getStringExtra(EXTRA_PLAYLIST_ID) ?: return
            
            val serviceIntent = Intent(context, MusicService::class.java).apply {
                action = ACTION_PLAY_MOOD
                putExtra(EXTRA_PLAYLIST_ID, playlistId)
            }
            try {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    context.startForegroundService(serviceIntent)
                } else {
                    context.startService(serviceIntent)
                }
            } catch (e: Exception) {
            }
        }
    }

    companion object {
        const val ACTION_PLAY_MOOD = "com.david.frequency.widget.PLAY_MOOD"
        const val EXTRA_PLAYLIST_ID = "playlist_id"
        
        fun updateAppWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int
        ) {
            val prefs = context.getSharedPreferences("mood_grid_widget_", Context.MODE_PRIVATE)
            
            val views = RemoteViews(context.packageName, R.layout.widget_mood_grid)
            
            for (i in 0..3) {
                val playlistId = prefs.getString("tile__id", null)
                val playlistName = prefs.getString("tile__name", "Mood ")
                
                val textId = when(i) {
                    0 -> R.id.widget_mood_text_1
                    1 -> R.id.widget_mood_text_2
                    2 -> R.id.widget_mood_text_3
                    else -> R.id.widget_mood_text_4
                }
                
                val tileId = when(i) {
                    0 -> R.id.widget_mood_tile_1
                    1 -> R.id.widget_mood_tile_2
                    2 -> R.id.widget_mood_tile_3
                    else -> R.id.widget_mood_tile_4
                }
                
                views.setTextViewText(textId, playlistName)
                
                if (playlistId != null) {
                    val intent = Intent(context, MoodGridWidgetReceiver::class.java).apply {
                        action = "_"
                        putExtra(EXTRA_PLAYLIST_ID, playlistId)
                    }
                    val pendingIntent = PendingIntent.getBroadcast(
                        context,
                        200 + appWidgetId * 10 + i,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                    )
                    views.setOnClickPendingIntent(tileId, pendingIntent)
                }
            }
            
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}
