path = r'app/src/main/kotlin/com/david/frequency/ui/screens/search/SearchScreen.kt'
with open(path, 'r', encoding='utf-8') as f:
    content = f.read()

content = content.replace('import androidx.compose.material3.SearchBar', 'import androidx.compose.material3.SearchBar\nimport androidx.compose.material3.SearchBarDefaults')
content = content.replace('import com.david.frequency.ui.theme.FrequencyColorsDefaults\n', '')
content = content.replace('active', 'isSearching')

with open(path, 'w', encoding='utf-8') as f:
    f.write(content)
