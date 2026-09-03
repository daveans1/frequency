path = r'C:\Users\david\.gemini\antigravity\brain\b36611a1-19ca-44f8-8a11-5c32790dc209\task.md'
with open(path, 'r', encoding='utf-8') as f:
    content = f.read()

content = content.replace('- [ ] Test Build (Verify cyber-acoustic compilation)', '- [x] Test Build (Verify cyber-acoustic compilation)')

with open(path, 'w', encoding='utf-8') as f:
    f.write(content)
