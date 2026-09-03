path = r'app/src/main/kotlin/com/david/frequency/ui/screens/HomeScreen.kt'
with open(path, 'r', encoding='utf-8') as f:
    content = f.read()

import re

# Fix DailyDiscoverCard
content = re.sub(
    r'modifier = modifier\s*\.fillMaxSize\(\)\s*\.clip\(RoundedCornerShape\(28\.dp\)\)',
    r'modifier = modifier.fillMaxSize().acousticGlass(28.dp).neonGlow(color = FrequencyColors.ElectricAqua.copy(alpha = 0.2f), radius = 12.dp).clip(RoundedCornerShape(28.dp))',
    content
)

# And make the Card transparent
content = content.replace(
'''    Card(
        modifier = modifier.fillMaxSize().acousticGlass(28.dp).neonGlow(color = FrequencyColors.ElectricAqua.copy(alpha = 0.2f), radius = 12.dp).clip(RoundedCornerShape(28.dp))
            .combinedClickable(''',
'''    Card(
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        modifier = modifier.fillMaxSize().acousticGlass(28.dp).neonGlow(color = FrequencyColors.ElectricAqua.copy(alpha = 0.2f), radius = 12.dp).clip(RoundedCornerShape(28.dp))
            .combinedClickable('''
)

with open(path, 'w', encoding='utf-8') as f:
    f.write(content)
