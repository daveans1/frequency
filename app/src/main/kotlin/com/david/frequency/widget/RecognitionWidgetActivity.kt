package com.david.frequency.widget

import android.Manifest
import androidx.activity.ComponentActivity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import coil3.ImageLoader
import coil3.request.ImageRequest
import coil3.toBitmap
import com.david.frequency.MainActivity
import com.david.frequency.R
import com.david.frequency.db.MusicDatabase
import com.david.frequency.recognition.MusicRecognitionService
import com.music.shazamkit.models.RecognitionStatus
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class RecognitionWidgetActivity : ComponentActivity() {

    @Inject
    lateinit var database: MusicDatabase

    private val scope = CoroutineScope(Dispatchers.Main)
    
    companion object {
        const val REQUEST_RECORD_AUDIO = 1001
        const val NOTIFICATION_CHANNEL_ID = "recognition_results"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) 
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                REQUEST_RECORD_AUDIO
            )
        } else {
            startRecognition()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_RECORD_AUDIO) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startRecognition()
            } else {
                Toast.makeText(this, "Microphone permission required", Toast.LENGTH_SHORT).show()
                finish()
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun startRecognition() {
        Toast.makeText(this, "Listening...", Toast.LENGTH_SHORT).show()
        
        scope.launch {
            val status = MusicRecognitionService.recognize(this@RecognitionWidgetActivity)
            showResultNotification(status)
            finish()
        }
    }

    private suspend fun showResultNotification(status: RecognitionStatus) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "Recognition Results",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Results from music recognition widget"
            }
            notificationManager.createNotificationChannel(channel)
        }
        
        val builder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.shortcut_mic)
            .setAutoCancel(true)

        when (status) {
            is RecognitionStatus.Success -> {
                val intent = Intent(this, MainActivity::class.java).apply {
                    action = "com.david.frequency.action.RECOGNITION_RESULT"
                    // Pass the query to search screen
                    putExtra("query", status.result.title + " " + status.result.artist)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                val pendingIntent = PendingIntent.getActivity(
                    this,
                    0,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )

                builder.setContentTitle(status.result.title)
                    .setContentText(status.result.artist)
                    .setContentIntent(pendingIntent)
                    
                // Try to load thumbnail
                status.result.coverArtHqUrl?.let { url ->
                    try {
                        val request = ImageRequest.Builder(this)
                            .data(url)
                            .size(300)
                            .build()
                        val result = ImageLoader(this).execute(request)
                        val bitmap = result.image?.toBitmap()
                        if (bitmap != null) {
                            builder.setLargeIcon(bitmap)
                        }
                    } catch (e: Exception) {}
                }
            }
            is RecognitionStatus.NoMatch -> {
                builder.setContentTitle("Song not recognized")
                    .setContentText("Could not identify the song.")
            }
            is RecognitionStatus.Error -> {
                builder.setContentTitle("Recognition Error")
                    .setContentText(status.message)
            }
            else -> return
        }
        
        notificationManager.notify(999, builder.build())
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }
}
