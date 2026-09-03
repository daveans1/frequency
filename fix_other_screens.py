import sys
import glob

files = [
    'app/src/main/kotlin/com/david/frequency/ui/screens/playlist/AutoPlaylistScreen.kt',
    'app/src/main/kotlin/com/david/frequency/ui/screens/playlist/CachePlaylistScreen.kt',
    'app/src/main/kotlin/com/david/frequency/ui/screens/playlist/TopPlaylistScreen.kt',
]

for file in files:
    content = open(file, encoding='utf-8').read()

    # Fix logic for songs?.all
    old_str_1 = '''                } else if (songs?.all {
                        downloads[it.song.id]?.state == Download.STATE_QUEUED ||
                                downloads[it.song.id]?.state == Download.STATE_DOWNLOADING ||
                                downloads[it.song.id]?.state == Download.STATE_COMPLETED
                    } == true
                ) {'''
    new_str_1 = '''                } else if (songs?.any {
                        downloads[it.song.id]?.state == Download.STATE_QUEUED ||
                                downloads[it.song.id]?.state == Download.STATE_DOWNLOADING
                    } == true
                ) {'''
    content = content.replace(old_str_1, new_str_1)

    # Fix logic for songs.all (CachePlaylistScreen)
    old_str_2 = '''                } else if (songs.all {
                        downloads[it.song.id]?.state == Download.STATE_QUEUED ||
                                downloads[it.song.id]?.state == Download.STATE_DOWNLOADING ||
                                downloads[it.song.id]?.state == Download.STATE_COMPLETED
                    }
                ) {'''
    new_str_2 = '''                } else if (songs.any {
                        downloads[it.song.id]?.state == Download.STATE_QUEUED ||
                                downloads[it.song.id]?.state == Download.STATE_DOWNLOADING
                    }
                ) {'''
    content = content.replace(old_str_2, new_str_2)
    
    # Fix onDownload remove
    old_remove_1 = '''                                    Download.STATE_DOWNLOADING -> {
                                        songs.forEach { song ->
                                            DownloadService.sendRemoveDownload(
                                                context,
                                                ExoDownloadService::class.java,
                                                song.song.id,
                                                false
                                            )
                                        }
                                    }'''
    new_remove_1 = '''                                    Download.STATE_DOWNLOADING -> {
                                        val downloads = downloadUtil.downloads.value
                                        songs.forEach { song ->
                                            if (downloads[song.song.id]?.state != Download.STATE_COMPLETED) {
                                                DownloadService.sendRemoveDownload(
                                                    context,
                                                    ExoDownloadService::class.java,
                                                    song.song.id,
                                                    false
                                                )
                                            }
                                        }
                                    }'''
    content = content.replace(old_remove_1, new_remove_1)
    
    old_remove_2 = '''                                    Download.STATE_DOWNLOADING -> {
                                        songs?.forEach { song ->
                                            DownloadService.sendRemoveDownload(
                                                context,
                                                ExoDownloadService::class.java,
                                                song.song.id,
                                                false
                                            )
                                        }
                                    }'''
    new_remove_2 = '''                                    Download.STATE_DOWNLOADING -> {
                                        val downloads = downloadUtil.downloads.value
                                        songs?.forEach { song ->
                                            if (downloads[song.song.id]?.state != Download.STATE_COMPLETED) {
                                                DownloadService.sendRemoveDownload(
                                                    context,
                                                    ExoDownloadService::class.java,
                                                    song.song.id,
                                                    false
                                                )
                                            }
                                        }
                                    }'''
    content = content.replace(old_remove_2, new_remove_2)
    
    open(file, 'w', encoding='utf-8').write(content)
