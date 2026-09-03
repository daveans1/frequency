path = r'app/src/main/kotlin/com/david/frequency/constants/PreferenceKeys.kt'
with open(path, 'r', encoding='utf-8') as f:
    content = f.read()

content = content.replace('const val AmbientBackdropKey = "ambient_backdrop"', 'val AmbientBackdropKey = booleanPreferencesKey("ambient_backdrop")')

with open(path, 'w', encoding='utf-8') as f:
    f.write(content)
