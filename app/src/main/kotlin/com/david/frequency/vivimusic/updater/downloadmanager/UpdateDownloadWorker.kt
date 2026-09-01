package com.david.frequency.vivimusic.updater.downloadmanager

import android.content.Context
import android.os.Environment
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.david.frequency.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.zip.ZipInputStream
import com.david.frequency.constants.AutoBackupEnabledKey
import com.david.frequency.constants.AutoBackupBeforeUpdateKey
import com.david.frequency.utils.dataStore
import com.david.frequency.utils.get
import com.david.frequency.utils.AutoBackupHelper
import timber.log.Timber

class UpdateDownloadWorker(private val context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        val apkUrl = inputData.getString("apk_url") ?: return@withContext Result.failure()
        val version = inputData.getString("version") ?: "unknown"
        val fileSize = inputData.getString("file_size") ?: ""

        try {
            val autoBackupEnabled = context.dataStore[AutoBackupEnabledKey] ?: true
            val backupBeforeUpdate = context.dataStore[AutoBackupBeforeUpdateKey] ?: true
            if (autoBackupEnabled && backupBeforeUpdate) {
                Timber.tag("UpdateDownloadWorker").d("Auto backup enabled. Creating backup before update.")
                AutoBackupHelper.performBackup(context, "before_update")
            }
        } catch (e: Exception) {
            Timber.tag("UpdateDownloadWorker").e(e, "Failed to perform auto backup before update")
        }

        DownloadNotificationManager.showDownloadStarting(version, fileSize)

        try {
            val downloadDir = File(
                context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),
                "vivi_updates"
            )
            if (!downloadDir.exists()) {
                downloadDir.mkdirs()
            }

            val isZip = apkUrl.contains("nightly.link") || apkUrl.endsWith(".zip")
            val downloadFile = if (isZip) File(downloadDir, "vivi_temp.zip") else File(downloadDir, "vivi.apk")
            
            var downloadedLength = 0L
            if (downloadFile.exists()) {
                downloadedLength = downloadFile.length()
            }

            var currentUrl = apkUrl
            var connection: HttpURLConnection? = null
            var redirectCount = 0
            while (true) {
                val url = URL(currentUrl)
                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "GET"
                conn.connectTimeout = 20000
                conn.readTimeout = 20000
                conn.setRequestProperty("User-Agent", "ViVi-Music-App/${com.david.frequency.BuildConfig.VERSION_NAME}")
                
                if (downloadedLength > 0) {
                    conn.setRequestProperty("Range", "bytes=$downloadedLength-")
                }
                
                conn.instanceFollowRedirects = true
                conn.connect()

                val responseCode = conn.responseCode
                if (responseCode == HttpURLConnection.HTTP_MOVED_TEMP ||
                    responseCode == HttpURLConnection.HTTP_MOVED_PERM ||
                    responseCode == HttpURLConnection.HTTP_SEE_OTHER ||
                    responseCode == 307 || responseCode == 308) {
                    val location = conn.getHeaderField("Location")
                    if (!location.isNullOrBlank() && redirectCount < 5) {
                        redirectCount++
                        currentUrl = location
                        conn.disconnect()
                        continue
                    }
                }
                connection = conn
                break
            }

            if (connection == null) {
                DownloadNotificationManager.showDownloadFailed(
                    version,
                    context.getString(R.string.server_error, -1)
                )
                return@withContext Result.failure()
            }

            val responseCode = connection.responseCode
            val isResuming = responseCode == HttpURLConnection.HTTP_PARTIAL
            
            if (responseCode != HttpURLConnection.HTTP_OK && responseCode != HttpURLConnection.HTTP_PARTIAL) {
                DownloadNotificationManager.showDownloadFailed(
                    version,
                    context.getString(R.string.server_error, responseCode)
                )
                return@withContext Result.failure()
            }

            val fileLength = if (isResuming) {
                connection.contentLength + downloadedLength
            } else {
                downloadedLength = 0L // Start from scratch if not resuming
                connection.contentLength.toLong()
            }

            val inputStream = connection.inputStream

            val outputStream = FileOutputStream(downloadFile, isResuming)

            val buffer = ByteArray(8192)
            var bytesRead: Int
            var totalBytesRead: Long = downloadedLength

            while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                if (isStopped) {
                    outputStream.close()
                    inputStream.close()
                    connection.disconnect()
                    return@withContext Result.retry()
                }

                outputStream.write(buffer, 0, bytesRead)
                totalBytesRead += bytesRead

                if (fileLength > 0) {
                    val progress = (totalBytesRead.toFloat() / fileLength.toFloat() * 100).toInt()
                    // Update notification
                    DownloadNotificationManager.updateDownloadProgress(progress, version)
                    // Update WorkManager progress for UI observation
                    setProgress(workDataOf("progress" to progress.toFloat() / 100f))
                }
            }

            outputStream.flush()
            outputStream.close()
            inputStream.close()
            connection.disconnect()

            val finalFile = if (isZip) {
                val targetApkFile = File(downloadDir, "vivi.apk")
                var extracted = false
                try {
                    ZipInputStream(downloadFile.inputStream()).use { zis ->
                        var entry = zis.nextEntry
                        while (entry != null) {
                            if (!entry.isDirectory && entry.name.endsWith(".apk")) {
                                FileOutputStream(targetApkFile).use { fos ->
                                    zis.copyTo(fos)
                                }
                                extracted = true
                                break
                            }
                            entry = zis.nextEntry
                        }
                    }
                } catch (e: Exception) {
                    if (downloadFile.exists()) downloadFile.delete()
                    DownloadNotificationManager.showDownloadFailed(
                        version,
                        e.message ?: "Failed to extract zip file"
                    )
                    return@withContext Result.failure()
                } finally {
                    if (downloadFile.exists()) {
                        downloadFile.delete()
                    }
                }
                if (!extracted) {
                    DownloadNotificationManager.showDownloadFailed(
                        version,
                        "Could not find APK in zip"
                    )
                    return@withContext Result.failure()
                }
                targetApkFile
            } else {
                downloadFile
            }

            if (version.startsWith("nightly-r")) {
                val runNumberString = version.removePrefix("nightly-r")
                val runNumber = runNumberString.toIntOrNull()
                if (runNumber != null) {
                    val sharedPreferences = context.getSharedPreferences("update_settings", Context.MODE_PRIVATE)
                    sharedPreferences.edit().putInt("last_installed_nightly_run", runNumber).apply()
                }
            }

            DownloadNotificationManager.showDownloadComplete(version, finalFile.absolutePath)

            Result.success(workDataOf("file_path" to finalFile.absolutePath))
        } catch (e: Exception) {
            if (e is java.io.IOException) {
                Timber.tag("UpdateDownloadWorker").e(e, "Network error during download, scheduling retry")
                // Don't show the failed notification yet, let WorkManager retry
                return@withContext Result.retry()
            }
            DownloadNotificationManager.showDownloadFailed(
                version,
                e.message ?: context.getString(R.string.download_failed)
            )
            Result.failure()
        }
    }
}
