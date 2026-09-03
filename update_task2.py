path = r'C:\Users\david\.gemini\antigravity\brain\b36611a1-19ca-44f8-8a11-5c32790dc209\task.md'
with open(path, 'r', encoding='utf-8') as f:
    content = f.read()

content = content.replace('- [ ] Core Screens', '- [x] Core Screens')
content = content.replace('  - [ ] Update HomeScreen.kt', '  - [x] Update HomeScreen.kt')
content = content.replace('  - [ ] Update SearchScreen.kt', '  - [x] Update SearchScreen.kt')

with open(path, 'w', encoding='utf-8') as f:
    f.write(content)
