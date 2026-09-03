path = r'app/src/main/kotlin/com/david/frequency/ui/screens/HomeScreen.kt'
with open(path, 'r', encoding='utf-8') as f:
    content = f.read()

# I need to find the Card with duplicated colors
# Let's just use regex to replace it
import re
new_content = re.sub(
r'''        colors = CardDefaults.cardColors\(containerColor = Color.Transparent\),
        modifier = modifier\.fillMaxSize\(\)\.acousticGlass\(28\.dp\)\.neonGlow\(color = FrequencyColors\.ElectricAqua\.copy\(alpha = 0\.2f\), radius = 12\.dp\)\.clip\(RoundedCornerShape\(28\.dp\)\)
            \.combinedClickable\(
                onClick = onClick,
                onLongClick = \{
                    haptic\.performHapticFeedback\(HapticFeedbackType\.LongPress\)
                    if \(song != null\) \{
                        menuState\.show \{
                            YouTubeSongMenu\(
                                song = song,
                                navController = navController,
                                onDismiss = \{ menuState\.dismiss\(\) \}
                            \)
                        \}
                    \}
                \}
            \),
        colors = CardDefaults\.cardColors\(
            containerColor = Color\.Transparent,
        \),
        shape = RoundedCornerShape\(28\.dp\)''',
r'''        modifier = modifier.fillMaxSize().acousticGlass(28.dp).neonGlow(color = FrequencyColors.ElectricAqua.copy(alpha = 0.2f), radius = 12.dp).clip(RoundedCornerShape(28.dp))
            .combinedClickable(
                onClick = onClick,
                onLongClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    if (song != null) {
                        menuState.show {
                            YouTubeSongMenu(
                                song = song,
                                navController = navController,
                                onDismiss = { menuState.dismiss() }
                            )
                        }
                    }
                }
            ),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent,
        ),
        shape = RoundedCornerShape(28.dp)''', content)

with open(path, 'w', encoding='utf-8') as f:
    f.write(new_content)
