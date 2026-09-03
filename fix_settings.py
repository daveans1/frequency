path = r'app/src/main/kotlin/com/david/frequency/ui/screens/settings/AppearanceSettings.kt'
with open(path, 'r', encoding='utf-8') as f:
    content = f.read()

# Fix AppearanceSettings @Composable duplication
content = content.replace('@Composable\n@OptIn', '@OptIn')
content = content.replace('@Composable\n\n@OptIn', '\n@OptIn')
content = content.replace('@Composable\n@Composable', '@Composable')
content = content.replace('@Composable\r\n@Composable', '@Composable')

with open(path, 'w', encoding='utf-8') as f:
    f.write(content)
