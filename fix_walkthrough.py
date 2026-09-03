path = r'C:\Users\david\.gemini\antigravity\brain\b36611a1-19ca-44f8-8a11-5c32790dc209\walkthrough.md'
import re
with open(path, 'r', encoding='utf-8') as f:
    content = f.read()

# Remove the line mentioning AOT Baseline Profile
content = re.sub(r'- \*\*AOT Baseline Profile Bundling\*\*:.*?\n', '', content)

with open(path, 'w', encoding='utf-8') as f:
    f.write(content)
