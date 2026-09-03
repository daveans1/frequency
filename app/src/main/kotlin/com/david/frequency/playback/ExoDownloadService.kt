/**
 * vivimusic Project (C) 2026
 * Licensed under GPL-3.0 | See git history for contributors
 */

package com.david.frequency.playback

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Icon
import androidx.media3.common.util.NotificationUtil
import androidx.media3.common.util.Util
import androidx.media3.exoplayer.offline.Download
import androidx.media3.exoplayer.offline.DownloadManager
import androidx.media3.exoplayer.offline.DownloadNotificationHelper
import androidx.media3.exoplayer.offline.DownloadService
import androidx.media3.exoplayer.scheduler.PlatformScheduler
import androidx.media3.exoplayer.scheduler.Scheduler
import com.david.frequency.R
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class ExoDownloadService : DownloadService(
    NOTIFICATION_ID,
    1000L,
    CHANNEL_ID,
    R.string.downloading,
    0
) {
    @Inject
    lateinit var downloadUtil: DownloadUtil

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == REMOVE_ALL_PENDING_DOWNLOADS) {
            val cursor = downloadManager.downloadIndex.getDownloads()
            while (cursor.moveToNext()) {
                val download = cursor.download
                if (download.state == Download.STATE_DOWNLOADING || download.state == Download.STATE_QUEUED || download.state == Download.STATE_STOPPED) {
                    downloadManager.removeDownload(download.request.id)
                }
            }
        } else if (intent?.action == ACTION_PAUSE_DOWNLOADS) {
            downloadManager.pauseDownloads()
        } else if (intent?.action == ACTION_RESUME_DOWNLOADS) {
            downloadManager.resumeDownloads()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun getDownloadManager() = downloadUtil.downloadManager

    override fun getScheduler(): Scheduler = PlatformScheduler(this, JOB_ID)

    override fun getForegroundNotification(
        downloads: MutableList<Download>,
        notMetRequirements: Int
    ): Notification {
        val isPaused = downloadManager.downloadsPaused

        val builder = Notification.Builder.recoverBuilder(
            this, downloadUtil.downloadNotificationHelper.buildProgressNotification(
                this,
                R.drawable.download,
                null,
                if (downloads.size == 1) Util.fromUtf8Bytes(downloads[0].request.data)
                else resources.getQuantityString(R.plurals.n_song, downloads.size, downloads.size),
                downloads,
                notMetRequirements
            )
        )

        if (isPaused) {
            builder.addAction(
                Notification.Action.Builder(
                    Icon.createWithResource(this, R.drawable.play),
                    "Resume",
                    PendingIntent.getService(
                        this,
                        1,
                        Intent(this, ExoDownloadService::class.java).setAction(ACTION_RESUME_DOWNLOADS),
                        PendingIntent.FLAG_IMMUTABLE
                    )
                ).build()
            )
        } else {
            builder.addAction(
                Notification.Action.Builder(
                    Icon.createWithResource(this, R.drawable.pause),
                    "Pause",
                    PendingIntent.getService(
                        this,
                        2,
                        Intent(this, ExoDownloadService::class.java).setAction(ACTION_PAUSE_DOWNLOADS),
                        PendingIntent.FLAG_IMMUTABLE
                    )
                ).build()
            )
        }

        builder.addAction(
            Notification.Action.Builder(
                Icon.createWithResource(this, R.drawable.close),
                "Stop",
                PendingIntent.getService(
                    this,
                    3,
                    Intent(this, ExoDownloadService::class.java).setAction(REMOVE_ALL_PENDING_DOWNLOADS),
                    PendingIntent.FLAG_IMMUTABLE
                )
            ).build()
        )

        return builder.build()
    }


    /**
     * This helper will outlive the lifespan of a single instance of [ExoDownloadService]
     */
    class TerminalStateNotificationHelper(
        private val context: Context,
        private val notificationHelper: DownloadNotificationHelper,
        private var nextNotificationId: Int,
    ) : DownloadManager.Listener {
        override fun onDownloadChanged(
            downloadManager: DownloadManager,
            download: Download,
            finalException: Exception?,
        ) {
            if (download.state == Download.STATE_FAILED) {
                val notification = notificationHelper.buildDownloadFailedNotification(
                    context,
                    R.drawable.error,
                    null,
                    Util.fromUtf8Bytes(download.request.data)
                )
                NotificationUtil.setNotification(context, nextNotificationId++, notification)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as android.app.NotificationManager
        notificationManager.cancel(NOTIFICATION_ID)
    }

    companion object {
        const val CHANNEL_ID = "download"
        const val NOTIFICATION_ID = 1
        const val JOB_ID = 1
        const val REMOVE_ALL_PENDING_DOWNLOADS = "REMOVE_ALL_PENDING_DOWNLOADS"
        const val ACTION_PAUSE_DOWNLOADS = "ACTION_PAUSE_DOWNLOADS"
        const val ACTION_RESUME_DOWNLOADS = "ACTION_RESUME_DOWNLOADS"
    }
}
