import re

path = 'app/src/main/kotlin/com/david/frequency/ui/screens/settings/PlayerSettings.kt'
with open(path, 'r', encoding='utf-8') as f:
    content = f.read()

# Make sure Toast is imported
if 'import android.widget.Toast' not in content:
    content = content.replace('import androidx.compose.ui.platform.LocalContext', 'import androidx.compose.ui.platform.LocalContext\nimport android.widget.Toast\nimport androidx.compose.material3.Icon')

enum_dialog1 = '''        EnumDialog(
            onDismiss = { showStreamingAudioQualityDialog = false },
            onSelect = {
                onStreamingAudioQualityChange(it)
                showStreamingAudioQualityDialog = false
            },
            title = stringResource(R.string.streaming_audio_quality),
            current = streamingAudioQuality,
            values = AudioQuality.entries.toList(),
            valueText = {
                when (it) {
                    AudioQuality.MAX -> stringResource(R.string.audio_quality_max)
                    AudioQuality.HIGH -> stringResource(R.string.audio_quality_high)
                    AudioQuality.MEDIUM -> stringResource(R.string.audio_quality_medium)
                    AudioQuality.LOW -> stringResource(R.string.audio_quality_low)
                    AudioQuality.AUTO -> stringResource(R.string.audio_quality_auto)
                }
            }
        )'''

enum_dialog1_rep = '''        EnumDialog(
            onDismiss = { showStreamingAudioQualityDialog = false },
            onSelect = {
                onStreamingAudioQualityChange(it)
                showStreamingAudioQualityDialog = false
            },
            title = stringResource(R.string.streaming_audio_quality),
            current = streamingAudioQuality,
            values = AudioQuality.entries.toList(),
            valueText = {
                when (it) {
                    AudioQuality.MAX -> stringResource(R.string.audio_quality_max)
                    AudioQuality.HIGH -> stringResource(R.string.audio_quality_high)
                    AudioQuality.MEDIUM -> stringResource(R.string.audio_quality_medium)
                    AudioQuality.LOW -> stringResource(R.string.audio_quality_low)
                    AudioQuality.AUTO -> stringResource(R.string.audio_quality_auto)
                }
            },
            trailingContent = {
                if (it == AudioQuality.MAX) {
                    IconButton(
                        onClick = {
                            Toast.makeText(context, R.string.audio_quality_max_info, Toast.LENGTH_SHORT).show()
                        }
                    ) {
                        Icon(painter = painterResource(R.drawable.info), contentDescription = null)
                    }
                }
            }
        )'''

enum_dialog2 = '''        EnumDialog(
            onDismiss = { showDownloadAudioQualityDialog = false },
            onSelect = {
                onDownloadAudioQualityChange(it)
                showDownloadAudioQualityDialog = false
            },
            title = stringResource(R.string.download_audio_quality),
            current = downloadAudioQuality,
            values = listOf(AudioQuality.MAX, AudioQuality.HIGH, AudioQuality.MEDIUM, AudioQuality.LOW),
            valueText = {
                when (it) {
                    AudioQuality.MAX -> stringResource(R.string.audio_quality_max)
                    AudioQuality.HIGH -> stringResource(R.string.audio_quality_high)
                    AudioQuality.MEDIUM -> stringResource(R.string.audio_quality_medium)
                    AudioQuality.LOW -> stringResource(R.string.audio_quality_low)
                    AudioQuality.AUTO -> "" // Should not happen
                }
            }
        )'''

enum_dialog2_rep = '''        EnumDialog(
            onDismiss = { showDownloadAudioQualityDialog = false },
            onSelect = {
                onDownloadAudioQualityChange(it)
                showDownloadAudioQualityDialog = false
            },
            title = stringResource(R.string.download_audio_quality),
            current = downloadAudioQuality,
            values = listOf(AudioQuality.MAX, AudioQuality.HIGH, AudioQuality.MEDIUM, AudioQuality.LOW),
            valueText = {
                when (it) {
                    AudioQuality.MAX -> stringResource(R.string.audio_quality_max)
                    AudioQuality.HIGH -> stringResource(R.string.audio_quality_high)
                    AudioQuality.MEDIUM -> stringResource(R.string.audio_quality_medium)
                    AudioQuality.LOW -> stringResource(R.string.audio_quality_low)
                    AudioQuality.AUTO -> "" // Should not happen
                }
            },
            trailingContent = {
                if (it == AudioQuality.MAX) {
                    IconButton(
                        onClick = {
                            Toast.makeText(context, R.string.audio_quality_max_info, Toast.LENGTH_SHORT).show()
                        }
                    ) {
                        Icon(painter = painterResource(R.drawable.info), contentDescription = null)
                    }
                }
            }
        )'''

content = content.replace(enum_dialog1, enum_dialog1_rep)
content = content.replace(enum_dialog2, enum_dialog2_rep)

with open(path, 'w', encoding='utf-8') as f:
    f.write(content)

print("Updated PlayerSettings.kt successfully.")
