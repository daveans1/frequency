path = 'app/src/main/kotlin/com/david/frequency/MainActivity.kt'
with open(path, 'r', encoding='utf-8') as f:
    content = f.read()

content = content.replace(
    '''                LaunchedEffect(intent?.action) {
                    when (intent?.action) {
                        ACTION_RECOGNITION -> {
                            navController.navigate("recognition") {
                                launchSingleTop = true
                            }
                        }
                    }
                }''',
    '''                LaunchedEffect(intent?.action) {
                    when (intent?.action) {
                        ACTION_RECOGNITION -> {
                            navController.navigate("recognition") {
                                launchSingleTop = true
                            }
                        }
                        ACTION_LIKED_SONGS -> {
                            navController.navigate("local_playlist/LP_LIKED") {
                                launchSingleTop = true
                            }
                        }
                    }
                }'''
)

with open(path, 'w', encoding='utf-8') as f:
    f.write(content)
