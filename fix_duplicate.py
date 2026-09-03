path = r'app/src/main/res/values/strings.xml'
with open(path, 'r', encoding='utf-8') as f:
    content = f.read()

content = content.replace('    <string name="cancel">Cancel</string>\n', '')

with open(path, 'w', encoding='utf-8') as f:
    f.write(content)
