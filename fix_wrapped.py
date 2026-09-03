path = r'app/src/main/kotlin/com/david/frequency/ui/screens/HomeScreen.kt'
with open(path, 'r', encoding='utf-8') as f:
    content = f.read()

import re

# Fix wrapped_card
content = re.sub(
    r'modifier = Modifier\s*\.fillMaxWidth\(\)\s*\.padding\(16\.dp\)',
    r'modifier = Modifier.fillMaxWidth().padding(16.dp).acousticGlass(16.dp)',
    content
)
content = re.sub(
    r'containerColor = MaterialTheme\.colorScheme\.surfaceVariant',
    r'containerColor = Color.Transparent',
    content
)

with open(path, 'w', encoding='utf-8') as f:
    f.write(content)
