path = r'app/src/main/kotlin/com/david/frequency/ui/screens/settings/AppearanceSettings.kt'
with open(path, 'r', encoding='utf-8') as f:
    content = f.read()

content = content.replace('fun AppearanceSettings(navController: NavController) {', '''import android.app.Activity
fun AppearanceSettings(
    navController: NavController,
    scrollBehavior: TopAppBarScrollBehavior,
    activity: Activity,
    snackbarHostState: SnackbarHostState,
) {''')

with open(path, 'w', encoding='utf-8') as f:
    f.write(content)
