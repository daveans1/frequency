path = r'app/src/main/kotlin/com/david/frequency/ui/screens/search/SearchScreen.kt'
with open(path, 'r', encoding='utf-8') as f:
    lines = f.readlines()

new_lines = []
for line in lines:
    if 'import com.david.frequency.ui.theme.FrequencyColorsDefaults' in line:
        continue
    if 'import androidx.compose.material3.SearchBar' in line:
        new_lines.append('import androidx.compose.material3.SearchBar\n')
        new_lines.append('import androidx.compose.material3.SearchBarDefaults\n')
        continue
    new_lines.append(line)

with open(path, 'w', encoding='utf-8') as f:
    f.writelines(new_lines)
