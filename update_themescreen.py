path = 'app/src/main/kotlin/com/david/frequency/ui/screens/settings/ThemeScreen.kt'
with open(path, 'r', encoding='utf-8') as f:
    content = f.read()

find_str = '''val PaletteColors = listOf(
    ThemePalette(R.string.palette_dynamic, Color.Transparent), // Sentinel for System/Dynamic colors'''

replace_str = '''val PaletteColors = listOf(
    ThemePalette(R.string.palette_dynamic, Color.Transparent), // Sentinel for System/Dynamic colors
    ThemePalette(R.string.palette_frequency, Color(0xFF20E0B0)), // Frequency Signature (Electric Aqua / Cyber Cyan)'''

content = content.replace(find_str, replace_str)

with open(path, 'w', encoding='utf-8') as f:
    f.write(content)
