import os, glob

for file in glob.glob('app/src/main/res/layout/widget_*.xml'):
    with open(file, 'r', encoding='utf-8') as f:
        content = f.read()
    
    if 'android:theme="@style/Theme.Widget.frequency"' in content:
        content = content.replace('android:theme="@style/Theme.Widget.frequency"', '')
        with open(file, 'w', encoding='utf-8') as f:
            f.write(content)
        print(f"Fixed {file}")
