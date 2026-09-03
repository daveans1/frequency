path = 'app/src/main/res/values/strings.xml'
with open(path, 'r', encoding='utf-8') as f:
    content = f.read()

content = content.replace(
    '</resources>',
    '''    <string name="widget_lens">Frequency Lens</string>
    <string name="widget_lens_description">Full-bleed artwork widget with auto-fading controls.</string>
    <string name="widget_identify">Identify Song</string>
    <string name="widget_identify_description">Quick tap to instantly recognize music playing around you.</string>
    <string name="widget_mood">Mood Grid</string>
    <string name="widget_mood_description">Quick launch shortcuts for your favorite playlists.</string>
    <string name="recognize_music">Identify Song</string>
    <string name="recognition_history">Recognition History</string>
</resources>'''
)

with open(path, 'w', encoding='utf-8') as f:
    f.write(content)
