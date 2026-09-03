path = r'app/src/main/res/layout/widget_lens.xml'
with open(path, 'r', encoding='utf-8') as f:
    content = f.read()

content = content.replace('<View\n            android:layout_width="match_parent"', '<FrameLayout\n            android:layout_width="match_parent"')
content = content.replace('android:background="@drawable/widget_lens_gradient" />', 'android:background="@drawable/widget_lens_gradient" />')

content = content.replace('<Space\n                    android:layout_width="0dp"', '<FrameLayout\n                    android:layout_width="0dp"')
content = content.replace('android:layout_weight="1" />', 'android:layout_weight="1" />')

with open(path, 'w', encoding='utf-8') as f:
    f.write(content)
