path = r'app/src/main/kotlin/com/david/frequency/widget/MoodGridConfigActivity.kt'
with open(path, 'r', encoding='utf-8') as f:
    content = f.read()

content = content.replace('context.getSharedPreferences("mood_grid_widget_", Context.MODE_PRIVATE)', 'context.getSharedPreferences("mood_grid_widget_", Context.MODE_PRIVATE)')
content = content.replace('playlist?.name ?: "Tap to select\\nMood "', 'playlist?.name ?: "Tap to select\\nMood "')

with open(path, 'w', encoding='utf-8') as f:
    f.write(content)
