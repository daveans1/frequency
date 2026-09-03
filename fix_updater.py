path = r'app/src/main/kotlin/com/david/frequency/vivimusic/updater/frequencyupdater.kt'
with open(path, 'r', encoding='utf-8') as f:
    content = f.read()

# 1. Update checking logic to fetch nightly size
import re

size_fetch_code = '''                var nightlySizeMB = "~30"
                try {
                    val url = java.net.URL(apkDownloadUrl)
                    var connection = url.openConnection() as java.net.HttpURLConnection
                    connection.requestMethod = "HEAD"
                    connection.instanceFollowRedirects = true
                    var status = connection.responseCode
                    if (status == java.net.HttpURLConnection.HTTP_MOVED_TEMP || status == java.net.HttpURLConnection.HTTP_MOVED_PERM || status == 303) {
                        val newUrl = connection.getHeaderField("Location")
                        if (newUrl != null) {
                            connection = java.net.URL(newUrl).openConnection() as java.net.HttpURLConnection
                            connection.requestMethod = "HEAD"
                        }
                    }
                    val cl = connection.getHeaderField("Content-Length")
                    val sizeBytes = cl?.toLongOrNull() ?: 0L
                    if (sizeBytes > 0) {
                        nightlySizeMB = String.format("%.1f", sizeBytes / (1024.0 * 1024.0))
                    }
                } catch (e: Exception) {
                    // Ignore, keep "~30"
                }
                
                withContext(Dispatchers.Main) {
                    onSuccess(displayTag, true, changelogList, nightlySizeMB, formattedReleaseDate, "Bleeding-edge nightly build from main branch.", null, apkDownloadUrl)
                }'''

content = content.replace(
'''                withContext(Dispatchers.Main) {
                    onSuccess(displayTag, true, changelogList, "~30", formattedReleaseDate, "Bleeding-edge nightly build from main branch.", null, apkDownloadUrl)
                }''',
size_fetch_code
)

# 2. Update UI buttons
cancel_logic = '''                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                AnimatedActionButton(
                                    text = if (isDownloading && !isDownloadComplete) stringResource(com.david.frequency.R.string.cancel) else stringResource(com.david.frequency.R.string.later),
                                    onClick = { 
                                        if (isDownloading && !isDownloadComplete) {
                                            androidx.work.WorkManager.getInstance(context).cancelUniqueWork("update_download")
                                            isDownloading = false
                                            downloadProgress = 0f
                                        } else {
                                            navController.navigateUp() 
                                        }
                                    },
                                    modifier = Modifier.weight(1f),
                                    isOutlined = true,
                                    enabled = true
                                )'''

content = re.sub(r'''                            Row\(\s*modifier = Modifier.fillMaxWidth\(\),\s*horizontalArrangement = Arrangement.spacedBy\(12.dp\)\s*\)\s*\{\s*AnimatedActionButton\(\s*text = stringResource\(R.string.later\),\s*onClick = \{ navController.navigateUp\(\) \},\s*modifier = Modifier.weight\(1f\),\s*isOutlined = true,\s*enabled = !isDownloading\s*\)''', cancel_logic, content)

with open(path, 'w', encoding='utf-8') as f:
    f.write(content)
print("Updated successfully")
