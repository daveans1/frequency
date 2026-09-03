path = r'app/src/main/kotlin/com/david/frequency/MainActivity.kt'
with open(path, 'r', encoding='utf-8') as f:
    content = f.read()

import re

if 'import com.david.frequency.constants.AmbientBackdropKey' not in content:
    content = content.replace('import com.david.frequency.constants.SelectedThemeColorKey', 'import com.david.frequency.constants.SelectedThemeColorKey\nimport com.david.frequency.constants.AmbientBackdropKey\nimport coil.compose.AsyncImage\nimport androidx.compose.ui.layout.ContentScale\nimport androidx.compose.ui.draw.blur\nimport androidx.compose.ui.draw.drawWithContent\nimport androidx.compose.ui.graphics.BlendMode\nimport androidx.compose.ui.graphics.Brush')

# Add ambientBackdrop preference
content = content.replace(
'''        val (selectedThemeColorInt) = rememberPreference(SelectedThemeColorKey, defaultValue = DefaultThemeColor.toArgb())
        val selectedThemeColor = remember(selectedThemeColorInt) { Color(selectedThemeColorInt) }

        vivimusicTheme(''',
'''        val (ambientBackdrop) = rememberPreference(AmbientBackdropKey, defaultValue = false)
        val (selectedThemeColorInt) = rememberPreference(SelectedThemeColorKey, defaultValue = DefaultThemeColor.toArgb())
        val selectedThemeColor = remember(selectedThemeColorInt) { Color(selectedThemeColorInt) }

        vivimusicTheme('''
)

# Modify BoxWithConstraints background and add AsyncImage inside
content = content.replace(
'''            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxSize()
                    .background(if (pureBlack) Color.Black else MaterialTheme.colorScheme.surface)
            ) {''',
'''            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                if (ambientBackdrop) {
                    val mediaMetadata by playerConnection?.mediaMetadata?.collectAsState() ?: mutableStateOf(null)
                    val artworkUri = mediaMetadata?.artworkUri
                    if (artworkUri != null) {
                        AsyncImage(
                            model = artworkUri,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .blur(100.dp)
                                .drawWithContent {
                                    drawContent()
                                    drawRect(
                                        color = Color(0xAA0A0E1A), // Fade it heavily into DeepAbyss
                                        size = size
                                    )
                                }
                        )
                    }
                }
'''
)

with open(path, 'w', encoding='utf-8') as f:
    f.write(content)
