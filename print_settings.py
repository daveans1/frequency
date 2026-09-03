path = r'app/src/main/kotlin/com/david/frequency/ui/screens/settings/AppearanceSettings.kt'
with open(path, 'r', encoding='utf-8') as f:
    lines = f.readlines()

for i, line in enumerate(lines):
    print(f"{i+1}: {line.strip()}")
