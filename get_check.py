path = r'app/src/main/kotlin/com/david/frequency/vivimusic/updater/frequencyupdater.kt'
with open(path, 'r', encoding='utf-8') as f:
    content = f.read()

import re
match = re.search(r'(suspend fun checkForUpdate.*?)(?=\n\n|\Z)', content, re.DOTALL)
if match:
    # Just print the first 2500 characters of the function
    print(match.group(1)[:2500])
else:
    print("Not found")
