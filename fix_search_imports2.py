path = r'app/src/main/kotlin/com/david/frequency/ui/screens/search/SearchScreen.kt'
with open(path, 'r', encoding='utf-8') as f:
    lines = f.readlines()

new_lines = []
has_searchbar_defaults = False
for line in lines:
    if 'import androidx.compose.material3.SearchBarDefaults' in line:
        if not has_searchbar_defaults:
            has_searchbar_defaults = True
            new_lines.append(line)
        continue
    new_lines.append(line)

with open(path, 'w', encoding='utf-8') as f:
    f.writelines(new_lines)
