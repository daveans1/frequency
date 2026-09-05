path = r'app/src/main/kotlin/com/david/frequency/vivimusic/updater/downloadmanager/UpdateDownloadWorker.kt'
with open(path, 'r', encoding='utf-8') as f:
    content = f.read()

import re

# We want to change the downloadFile path to include version
content = re.sub(
    r'''            val isZip = apkUrl\.contains\("nightly\.link"\) \|\| apkUrl\.endsWith\("\.zip"\)\n            val downloadFile = if \(isZip\) File\(downloadDir, "frequency_temp\.zip"\) else File\(downloadDir, "frequency\.apk"\)''',
    r'''            val isZip = apkUrl.contains("nightly.link") || apkUrl.endsWith(".zip")
            
            // Clean up old updates to avoid appending to them
            val oldFiles = downloadDir.listFiles()
            oldFiles?.forEach { if (it.name != "frequency_.apk" && it.name != "frequency_temp_.zip") it.delete() }
            
            val downloadFile = if (isZip) File(downloadDir, "frequency_temp_.zip") else File(downloadDir, "frequency_.apk")''',
    content
)

# And in the zip extraction:
content = re.sub(
    r'''            val finalFile = if \(isZip\) \{\n                val targetApkFile = File\(downloadDir, "frequency\.apk"\)''',
    r'''            val finalFile = if (isZip) {
                val targetApkFile = File(downloadDir, "frequency_.apk")''',
    content
)

with open(path, 'w', encoding='utf-8') as f:
    f.write(content)
