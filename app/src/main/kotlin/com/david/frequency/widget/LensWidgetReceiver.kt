package com.david.frequency.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import com.david.frequency.playback.MusicService

class LensWidgetReceiver : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        if (MusicService.isRunning) {
            val intent = Intent(context, MusicService::class.java).apply {
                action = ACTION_UPDATE_LENS_WIDGET
            }
            try {
                context.startService(intent)
            } catch (e: Exception) {
            }
        }
    }

    override fun onAppWidgetOptionsChanged(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int,
        newOptions: Bundle
    ) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions)
        if (MusicService.isRunning) {
            val intent = Intent(context, MusicService::class.java).apply {
                action = ACTION_UPDATE_LENS_WIDGET
            }
            try {
                context.startService(intent)
            } catch (e: Exception) {
            }
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        when (intent.action) {
            ACTION_TOGGLE_CONTROLS, ACTION_PLAY_PAUSE, ACTION_LIKE, ACTION_NEXT, ACTION_PREVIOUS -> {
                val serviceIntent = Intent(context, MusicService::class.java).apply {
                    action = intent.action
                    putExtras(intent)
                }
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        context.startService(serviceIntent)
                    } else {
                        context.startService(serviceIntent)
                    }
                } catch (e: Exception) {
                }
            }
        }
    }

    companion object {
        const val ACTION_PLAY_PAUSE = "com.david.frequency.widget.lens.PLAY_PAUSE"
        const val ACTION_LIKE = "com.david.frequency.widget.lens.LIKE"
        const val ACTION_NEXT = "com.david.frequency.widget.lens.NEXT"
        const val ACTION_PREVIOUS = "com.david.frequency.widget.lens.PREVIOUS"
        const val ACTION_UPDATE_LENS_WIDGET = "com.david.frequency.widget.lens.UPDATE_WIDGET"
        const val ACTION_TOGGLE_CONTROLS = "com.david.frequency.widget.lens.TOGGLE_CONTROLS"
    }
}
