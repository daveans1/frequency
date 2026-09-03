path = r'app/src/main/res/xml/shortcuts.xml'
with open(path, 'r', encoding='utf-8') as f:
    content = f.read()

content = content.replace('android:targetPackage="com.david.frequency"', '')
with open(path, 'w', encoding='utf-8') as f:
    f.write(content)
