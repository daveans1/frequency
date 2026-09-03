path = 'app/src/main/kotlin/com/david/frequency/ui/screens/settings/PlayerSettings.kt'
with open(path, 'r', encoding='utf-8') as f:
    content = f.read()

content = content.replace('onClick = {\n                            Toast.makeText', 'onClick = {\n                            Toast.makeText', 1).replace('''                        IconButton(
                            onClick = {
                                Toast.makeText(context, R.string.audio_quality_max_info, Toast.LENGTH_SHORT).show()
                            }
                        ) {''', '''                        IconButton(
                            onClick = {
                                Toast.makeText(context, R.string.audio_quality_max_info, Toast.LENGTH_SHORT).show()
                            },
                            onLongClick = {}
                        ) {''')

with open(path, 'w', encoding='utf-8') as f:
    f.write(content)
