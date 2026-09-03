path = r'app/src/main/res/values/strings.xml'
with open(path, 'r', encoding='utf-8') as f:
    content = f.read()

if 'name="cancel"' not in content:
    content = content.replace('</resources>', '    <string name="cancel">Cancel</string>\n</resources>')
    with open(path, 'w', encoding='utf-8') as f:
        f.write(content)
