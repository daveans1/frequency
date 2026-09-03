path = 'app/src/main/kotlin/com/david/frequency/playback/MusicService.kt'
with open(path, 'r', encoding='utf-8') as f:
    content = f.read()

import_find = 'import com.david.frequency.widget.LensWidgetReceiver'
import_replace = 'import com.david.frequency.widget.LensWidgetReceiver\nimport com.david.frequency.widget.MoodGridWidgetReceiver'

if 'import com.david.frequency.widget.MoodGridWidgetReceiver' not in content:
    content = content.replace(import_find, import_replace)

action_find = '''            LensWidgetReceiver.ACTION_TOGGLE_CONTROLS -> {
                showLensControlsTemporarily()
            }
        }

        return super.onStartCommand(intent, flags, startId)'''

action_replace = '''            LensWidgetReceiver.ACTION_TOGGLE_CONTROLS -> {
                showLensControlsTemporarily()
            }
            MoodGridWidgetReceiver.ACTION_PLAY_MOOD -> {
                val playlistId = intent.getStringExtra(MoodGridWidgetReceiver.EXTRA_PLAYLIST_ID)
                if (playlistId != null) {
                    scope.launch {
                        val songs = if (playlistId == com.david.frequency.db.entities.PlaylistEntity.LIKED_PLAYLIST_ID) {
                            kotlinx.coroutines.flow.first(database.songDao().likedSongsByCreateDateAsc())
                        } else if (playlistId == com.david.frequency.db.entities.PlaylistEntity.DOWNLOADED_PLAYLIST_ID) {
                            kotlinx.coroutines.flow.first(database.songDao().downloadedSongsByCreateDateAsc())
                        } else {
                            kotlinx.coroutines.flow.first(database.playlistDao().playlistSongs(playlistId)).map { it.song }
                        }
                        
                        if (songs.isNotEmpty()) {
                            val items = songs.shuffled().map { it.toMediaItem() }
                            withContext(Dispatchers.Main) {
                                player.setMediaItems(items)
                                player.play()
                            }
                        }
                    }
                }
            }
        }

        return super.onStartCommand(intent, flags, startId)'''

if 'MoodGridWidgetReceiver.ACTION_PLAY_MOOD' not in content:
    content = content.replace(action_find, action_replace)

with open(path, 'w', encoding='utf-8') as f:
    f.write(content)
