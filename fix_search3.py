path = r'app/src/main/kotlin/com/david/frequency/ui/screens/search/SearchScreen.kt'
with open(path, 'r', encoding='utf-8') as f:
    content = f.read()

import re

# Add imports if they don't exist
if 'import com.david.frequency.ui.component.acousticGlass' not in content:
    content = content.replace('import androidx.compose.material3.SearchBar', 'import androidx.compose.material3.SearchBar\nimport com.david.frequency.ui.component.acousticGlass\nimport com.david.frequency.ui.component.neonGlow\nimport com.david.frequency.ui.theme.FrequencyColors')

content = re.sub(
r'''                    colors = SearchBarDefaults\.colors\(
                        containerColor = if \(pureBlack\) Color\(0xFF181818\) else MaterialTheme\.colorScheme\.surfaceContainerHigh
                    \),
                    modifier = Modifier
                        \.fillMaxWidth\(\)
                        \.padding\(horizontal = searchBarHorizontalPadding\)
                        \.padding\(top = searchBarTopPadding\)''',
r'''                    colors = SearchBarDefaults.colors(containerColor = Color.Transparent, dividerColor = Color.Transparent),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = searchBarHorizontalPadding)
                        .padding(top = searchBarTopPadding).acousticGlass(28.dp, alpha = 0.5f, borderColor = if (searchActive) FrequencyColors.ElectricAqua else FrequencyColors.GlassBorder, borderWidth = if (searchActive) 2.dp else 1.dp).let { if (searchActive) it.neonGlow(color = FrequencyColors.ElectricAqua.copy(alpha = 0.2f), radius = 12.dp) else it }''', content)

# I also need to replace the categories
content = re.sub(
r'''                            Surface\(
                                color = MaterialTheme\.colorScheme\.surfaceVariant\.copy\(alpha = 0\.5f\),
                                shape = RoundedCornerShape\(16\.dp\),
                                modifier = Modifier
                                    \.weight\(1f\)
                                    \.aspectRatio\(1\.5f\)
                                    \.clickable \{
                                        navController\.navigate\("mood/$\{mood\.name\}"\)
                                    \}
                            \) \{''',
r'''                            Surface(
                                color = Color.Transparent,
                                shape = RoundedCornerShape(16.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1.5f)
                                    .acousticGlass(16.dp)
                                    .clickable {
                                        navController.navigate("mood/")
                                    }
                            ) {''', content)


with open(path, 'w', encoding='utf-8') as f:
    f.write(content)
