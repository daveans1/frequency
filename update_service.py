path = 'app/src/main/kotlin/com/david/frequency/playback/MusicService.kt'
with open(path, 'r', encoding='utf-8') as f:
    content = f.read()

# Add import
if 'import com.david.frequency.widget.LensWidgetReceiver' not in content:
    content = content.replace('import com.david.frequency.widget.MusicWidgetReceiver', 'import com.david.frequency.widget.MusicWidgetReceiver\nimport com.david.frequency.widget.LensWidgetReceiver')

# Replace onStartCommand
on_start_find = '''    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            MusicWidgetReceiver.ACTION_PLAY_PAUSE -> {
                if (player.isPlaying) player.pause() else player.play()
                updateWidgetUI(player.isPlaying)
            }
            MusicWidgetReceiver.ACTION_LIKE -> {
                toggleLike()
            }
            MusicWidgetReceiver.ACTION_NEXT -> {
                player.seekToNext()
                updateWidgetUI(player.isPlaying)
            }
            MusicWidgetReceiver.ACTION_PREVIOUS -> {
                player.seekToPrevious()
                updateWidgetUI(player.isPlaying)
            }
            MusicWidgetReceiver.ACTION_UPDATE_WIDGET -> {
                updateWidgetUI(player.isPlaying)
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }'''

on_start_replace = '''    private var lensControlsTimerJob: kotlinx.coroutines.Job? = null

    private fun showLensControlsTemporarily() {
        widgetManager.setLensControlsVisible(true)
        updateWidgetUI(player.isPlaying)
        lensControlsTimerJob?.cancel()
        lensControlsTimerJob = scope.launch {
            kotlinx.coroutines.delay(3000)
            widgetManager.setLensControlsVisible(false)
            updateWidgetUI(player.isPlaying)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            MusicWidgetReceiver.ACTION_PLAY_PAUSE, LensWidgetReceiver.ACTION_PLAY_PAUSE -> {
                if (player.isPlaying) player.pause() else player.play()
                if (intent.action == LensWidgetReceiver.ACTION_PLAY_PAUSE) showLensControlsTemporarily()
                else updateWidgetUI(player.isPlaying)
            }
            MusicWidgetReceiver.ACTION_LIKE, LensWidgetReceiver.ACTION_LIKE -> {
                toggleLike()
                if (intent.action == LensWidgetReceiver.ACTION_LIKE) showLensControlsTemporarily()
            }
            MusicWidgetReceiver.ACTION_NEXT, LensWidgetReceiver.ACTION_NEXT -> {
                player.seekToNext()
                if (intent.action == LensWidgetReceiver.ACTION_NEXT) showLensControlsTemporarily()
                else updateWidgetUI(player.isPlaying)
            }
            MusicWidgetReceiver.ACTION_PREVIOUS, LensWidgetReceiver.ACTION_PREVIOUS -> {
                player.seekToPrevious()
                if (intent.action == LensWidgetReceiver.ACTION_PREVIOUS) showLensControlsTemporarily()
                else updateWidgetUI(player.isPlaying)
            }
            MusicWidgetReceiver.ACTION_UPDATE_WIDGET, LensWidgetReceiver.ACTION_UPDATE_LENS_WIDGET -> {
                updateWidgetUI(player.isPlaying)
            }
            LensWidgetReceiver.ACTION_TOGGLE_CONTROLS -> {
                showLensControlsTemporarily()
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }'''

content = content.replace(on_start_find, on_start_replace)

with open(path, 'w', encoding='utf-8') as f:
    f.write(content)
