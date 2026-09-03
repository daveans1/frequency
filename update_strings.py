path = 'app/src/main/res/values/frequency_strings.xml'
with open(path, 'r', encoding='utf-8') as f:
    content = f.read()

find_str = '    <!-- Theme Palette Names -->\n    <string name="palette_dynamic">Dynamic</string>'
replace_str = '''    <!-- Theme Palette Names -->
    <string name="palette_dynamic">Dynamic</string>
    <string name="palette_frequency">Frequency (Cyan)</string>
    <string name="frequency_vibe_preset">Frequency Signature Experience</string>
    <string name="frequency_vibe_preset_desc">Instantly tune interface: Cyber-cyan accent, floating acoustic dock &amp; ambient player</string>
    <string name="frequency_vibe_applied">Frequency aesthetic applied! Enjoy the vibe.</string>'''

if 'palette_frequency' not in content:
    content = content.replace(find_str, replace_str)

with open(path, 'w', encoding='utf-8') as f:
    f.write(content)
