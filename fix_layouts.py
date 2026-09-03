path1 = r'app/src/main/res/layout/widget_lens.xml'
with open(path1, 'r', encoding='utf-8') as f:
    content1 = f.read()

content1 = content1.replace('android:theme="@style/Theme.Widget.frequency"', '')
content1 = content1.replace('?android:attr/selectableItemBackgroundBorderless', '@android:color/transparent')

with open(path1, 'w', encoding='utf-8') as f:
    f.write(content1)

path2 = r'app/src/main/res/layout/widget_mood_grid.xml'
with open(path2, 'r', encoding='utf-8') as f:
    content2 = f.read()

content2 = content2.replace('android:theme="@style/Theme.Widget.frequency"', '')

with open(path2, 'w', encoding='utf-8') as f:
    f.write(content2)
