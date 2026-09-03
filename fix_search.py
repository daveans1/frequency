path = r'app/src/main/kotlin/com/david/frequency/ui/screens/search/SearchScreen.kt'
with open(path, 'r', encoding='utf-8') as f:
    content = f.read()

import re

# Add imports
if 'import com.david.frequency.ui.component.acousticGlass' not in content:
    content = content.replace('import androidx.compose.material3.SearchBar', 'import androidx.compose.material3.SearchBar\nimport com.david.frequency.ui.component.acousticGlass\nimport com.david.frequency.ui.component.neonGlow\nimport com.david.frequency.ui.theme.FrequencyColors')

content = re.sub(
    r'SearchBarDefaults\.colors\([\s\S]*?\),',
    r'SearchBarDefaults.colors(containerColor = Color.Transparent, dividerColor = Color.Transparent),',
    content
)

# the modifier is like:
# modifier = Modifier
#     .fillMaxWidth()
#     .padding(horizontal = searchBarHorizontalPadding)
#     .padding(top = searchBarTopPadding)
content = re.sub(
    r'\.padding\(top = searchBarTopPadding\)',
    r'.padding(top = searchBarTopPadding).acousticGlass(28.dp, alpha = 0.5f, borderColor = if (active) FrequencyColors.ElectricAqua else FrequencyColors.GlassBorder, borderWidth = if (active) 2.dp else 1.dp).let { if (active) it.neonGlow(color = FrequencyColors.ElectricAqua.copy(alpha = 0.2f), radius = 12.dp) else it }',
    content
)

with open(path, 'w', encoding='utf-8') as f:
    f.write(content)
