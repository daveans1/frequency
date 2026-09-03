path = r'app/src/main/kotlin/com/david/frequency/ui/screens/settings/AppearanceSettings.kt'
with open(path, 'r', encoding='utf-8') as f:
    content = f.read()

content = content.replace('R.drawable.ic_widget_lens', 'R.drawable.routine_theme')

with open(path, 'w', encoding='utf-8') as f:
    f.write(content)
