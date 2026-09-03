path = r'C:\Users\david\.gemini\antigravity\brain\b36611a1-19ca-44f8-8a11-5c32790dc209\task.md'
with open(path, 'r', encoding='utf-8') as f:
    content = f.read()

content = content.replace('- [ ] Foundation & Themes\n  - [ ] Update Theme.kt', '- [x] Foundation & Themes\n  - [x] Update Theme.kt')
content = content.replace('- [ ] Appearance Settings Overhaul (AppearanceSettings.kt)\n  - [ ] Add "Ambient Album Backdrop" toggle (default: false).\n  - [ ] Delete obsolete toggles (dynamicTheme, pureBlackMiniPlayer, etc).\n  - [ ] Reorganize into clean categories ("Core Aesthetics", "Navigation", "Lists & Cards").', '- [x] Appearance Settings Overhaul (AppearanceSettings.kt)\n  - [x] Add "Ambient Album Backdrop" toggle (default: false).\n  - [x] Delete obsolete toggles (dynamicTheme, pureBlackMiniPlayer, etc).\n  - [x] Reorganize into clean categories ("Core Aesthetics", "Navigation", "Lists & Cards").')

with open(path, 'w', encoding='utf-8') as f:
    f.write(content)
