path = r'app/src/main/kotlin/com/david/frequency/ui/screens/search/SearchScreen.kt'
with open(path, 'r', encoding='utf-8') as f:
    content = f.read()

content = content.replace(
'''                                .height(64.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(MaterialTheme.colorScheme.surfaceContainer)''',
'''                                .height(64.dp)
                                .acousticGlass(10.dp)'''
)

with open(path, 'w', encoding='utf-8') as f:
    f.write(content)
