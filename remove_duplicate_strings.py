path = 'app/src/main/res/values/strings.xml'
with open(path, 'r', encoding='utf-8') as f:
    content = f.read()

content = content.replace('    <string name="recognize_music">Identify Song</string>\n', '')
content = content.replace('    <string name="recognition_history">Recognition History</string>\n', '')

with open(path, 'w', encoding='utf-8') as f:
    f.write(content)
