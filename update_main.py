path = 'app/src/main/kotlin/com/david/frequency/MainActivity.kt'
with open(path, 'r', encoding='utf-8') as f:
    content = f.read()

content = content.replace(
    'private const val ACTION_SEARCH = "com.david.frequency.action.SEARCH"',
    '''private const val ACTION_RECOGNITION = "com.david.frequency.action.RECOGNITION"\n        private const val ACTION_SEARCH = "com.david.frequency.action.SEARCH"\n        private const val ACTION_LIKED_SONGS = "com.david.frequency.action.LIKED_SONGS"'''
)

content = content.replace(
    '''val tabOpenedFromShortcut = remember {
                    when (intent?.action) {
                        ACTION_SEARCH -> NavigationTab.LIBRARY
                        ACTION_LIBRARY -> NavigationTab.SEARCH
                        else -> null
                    }
                }''',
    '''val tabOpenedFromShortcut = remember {
                    when (intent?.action) {
                        ACTION_SEARCH -> NavigationTab.SEARCH
                        ACTION_LIBRARY, ACTION_LIKED_SONGS -> NavigationTab.LIBRARY
                        else -> null
                    }
                }
                
                LaunchedEffect(intent?.action) {
                    when (intent?.action) {
                        ACTION_RECOGNITION -> {
                            navController.navigate("recognition") {
                                launchSingleTop = true
                            }
                        }
                    }
                }'''
)

with open(path, 'w', encoding='utf-8') as f:
    f.write(content)
