path = r'app/src/main/kotlin/com/david/frequency/constants/PreferenceKeys.kt'
with open(path, 'a', encoding='utf-8') as f:
    f.write('\nconst val AmbientBackdropKey = "ambient_backdrop"\n')
