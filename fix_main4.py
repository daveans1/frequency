path = r'app/src/main/kotlin/com/david/frequency/MainActivity.kt'
with open(path, 'r', encoding='utf-8') as f:
    content = f.read()

content = content.replace('contentScale = ContentScale.Fit', 'contentScale = androidx.compose.ui.layout.ContentScale.Fit')
content = content.replace('contentScale = ContentScale.FillBounds', 'contentScale = androidx.compose.ui.layout.ContentScale.FillBounds')

with open(path, 'w', encoding='utf-8') as f:
    f.write(content)
