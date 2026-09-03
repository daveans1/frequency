path = r'app/src/main/kotlin/com/david/frequency/MainActivity.kt'
with open(path, 'r', encoding='utf-8') as f:
    content = f.read()

content = content.replace('import androidx.compose.ui.layout.ContentScale\n', '')
content = content.replace('import coil.compose.AsyncImage\n', 'import coil3.compose.AsyncImage\n')
content = content.replace('contentScale = ContentScale.Crop', 'contentScale = androidx.compose.ui.layout.ContentScale.Crop')
content = content.replace('val artworkUri = mediaMetadata?.artworkUri', 'val artworkUri = mediaMetadata?.thumbnailUrl')

with open(path, 'w', encoding='utf-8') as f:
    f.write(content)
