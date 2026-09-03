path = 'app/src/main/kotlin/com/david/frequency/ui/screens/settings/SettingsScreen.kt'
with open(path, 'r', encoding='utf-8') as f:
    content = f.read()

# Add new imports
new_imports = '''import com.david.frequency.ui.theme.FrequencyColors
import com.david.frequency.ui.component.AcousticBentoCard
import com.david.frequency.ui.component.AcousticTelemetryStrip
import com.david.frequency.ui.component.acousticGlass'''

if 'import com.david.frequency.ui.theme.FrequencyColors' not in content:
    content = content.replace('import com.david.frequency.ui.component.IconButton', f'import com.david.frequency.ui.component.IconButton\n{new_imports}')

# Replace the search bar surface with acoustic glass styling
old_search_bar = '''        // Search Bar
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            shape = RoundedCornerShape(50),
            color = MaterialTheme.colorScheme.surfaceContainerLow,
        ) {'''

new_search_bar = '''        // Search Bar (Acoustic Glass)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 14.dp)
                .acousticGlass(cornerRadius = 50.dp, alpha = 0.75f)
        ) {'''

content = content.replace(old_search_bar, new_search_bar)

# Replace the else block (the 7 primary category groups) with Bento Grid + Studio Sub-Modules
old_categories = '''        } else {
            // The 7 Primary Category Groups
            Material3SettingsGroup(
                itemMinHeight = 64.dp,
                items = listOf(
                    Material3SettingsItem(
                        icon = painterResource(R.drawable.palette),
                        title = { Text(stringResource(R.string.appearance)) },
                        description = { Text("Theme, fonts, mini-player, layout") },
                        onClick = { navController.navigate("settings/appearance") },
                        isExpressive = true,
                        descriptionBelow = true
                    ),
                    Material3SettingsItem(
                        icon = painterResource(R.drawable.play),
                        title = { Text(stringResource(R.string.player_and_audio)) },
                        description = { Text("Quality (320kbps), equalizer, data saver") },
                        onClick = { navController.navigate("settings/player") },
                        isExpressive = true,
                        descriptionBelow = true
                    ),
                    Material3SettingsItem(
                        icon = painterResource(R.drawable.lyrics),
                        title = { Text(stringResource(R.string.lyrics)) },
                        description = { Text("YouLyPlus, AI translation, style, animations") },
                        onClick = { navController.navigate("settings/lyrics") },
                        isExpressive = true,
                        descriptionBelow = true
                    ),
                    Material3SettingsItem(
                        icon = painterResource(R.drawable.language),
                        title = { Text(stringResource(R.string.content)) },
                        description = { Text("Languages, curation, auto-playlists, artist page") },
                        onClick = { navController.navigate("settings/content") },
                        isExpressive = true,
                        descriptionBelow = true
                    ),
                    Material3SettingsItem(
                        icon = painterResource(R.drawable.google),
                        title = { Text(stringResource(R.string.account)) },
                        description = { Text("YouTube Music, Discord, Last.fm, Listen Together") },
                        onClick = { navController.navigate("settings/account") },
                        isExpressive = true,
                        descriptionBelow = true
                    ),
                    Material3SettingsItem(
                        icon = painterResource(R.drawable.storage),
                        title = { Text(stringResource(R.string.storage)) },
                        description = { Text("Cache management, backup & restore, imports") },
                        onClick = { navController.navigate("settings/storage") },
                        isExpressive = true,
                        descriptionBelow = true
                    ),
                    Material3SettingsItem(
                        icon = painterResource(if (isUpdateAvailable) R.drawable.frequencynotification else R.drawable.info),
                        title = { Text(stringResource(R.string.about)) },
                        description = {
                            if (isUpdateAvailable) {
                                Text(
                                    text = stringResource(R.string.update_available),
                                    color = MaterialTheme.colorScheme.error
                                )
                            } else {
                                Text("v • Updates, privacy, network")
                            }
                        },
                        onClick = { navController.navigate("settings/about") },
                        isExpressive = true,
                        descriptionBelow = true
                    )
                )
            )
        }'''

new_categories = '''        } else {
            // 1. Telemetry Strip
            AcousticTelemetryStrip(
                modifier = Modifier.padding(bottom = 16.dp),
                statusText = "Audio Engine: 320kbps Lossless Ready • Axion DSP Active"
            )

            // 2. Bento Console Grid (2x2 Matrix)
            Text(
                text = "STUDIO MODULES",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = FrequencyColors.SonicCyan,
                letterSpacing = 1.2.sp,
                modifier = Modifier.padding(start = 6.dp, bottom = 10.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                AcousticBentoCard(
                    title = stringResource(R.string.player_and_audio),
                    subtitle = "Quality (320k), EQ, crossfade",
                    icon = painterResource(R.drawable.play),
                    accentColor = FrequencyColors.ElectricAqua,
                    onClick = { navController.navigate("settings/player") },
                    modifier = Modifier.weight(1f).height(138.dp)
                )

                AcousticBentoCard(
                    title = stringResource(R.string.appearance),
                    subtitle = "Theme, fonts, glass layout",
                    icon = painterResource(R.drawable.palette),
                    accentColor = FrequencyColors.SonicCyan,
                    onClick = { navController.navigate("settings/appearance") },
                    modifier = Modifier.weight(1f).height(138.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                AcousticBentoCard(
                    title = stringResource(R.string.account),
                    subtitle = "YTM, Discord, Last.fm, sync",
                    icon = painterResource(R.drawable.google),
                    accentColor = FrequencyColors.DeepResonance,
                    onClick = { navController.navigate("settings/account") },
                    modifier = Modifier.weight(1f).height(138.dp)
                )

                AcousticBentoCard(
                    title = stringResource(R.string.storage),
                    subtitle = "Cache, backups & imports",
                    icon = painterResource(R.drawable.storage),
                    accentColor = FrequencyColors.FrequencyBlue,
                    onClick = { navController.navigate("settings/storage") },
                    modifier = Modifier.weight(1f).height(138.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 3. Sub-Console Module Rack
            Text(
                text = "ENGINE PREFERENCES",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = FrequencyColors.SonicCyan,
                letterSpacing = 1.2.sp,
                modifier = Modifier.padding(start = 6.dp, bottom = 10.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .acousticGlass(cornerRadius = 20.dp, alpha = 0.85f)
            ) {
                Material3SettingsGroup(
                    itemMinHeight = 64.dp,
                    items = listOf(
                        Material3SettingsItem(
                            icon = painterResource(R.drawable.lyrics),
                            title = { Text(stringResource(R.string.lyrics), color = FrequencyColors.SonicWhite) },
                            description = { Text("YouLyPlus, AI translation, style", color = FrequencyColors.SonicMuted) },
                            onClick = { navController.navigate("settings/lyrics") },
                            isExpressive = true,
                            descriptionBelow = true
                        ),
                        Material3SettingsItem(
                            icon = painterResource(R.drawable.language),
                            title = { Text(stringResource(R.string.content), color = FrequencyColors.SonicWhite) },
                            description = { Text("Languages, curation, auto-playlists", color = FrequencyColors.SonicMuted) },
                            onClick = { navController.navigate("settings/content") },
                            isExpressive = true,
                            descriptionBelow = true
                        ),
                        Material3SettingsItem(
                            icon = painterResource(if (isUpdateAvailable) R.drawable.frequencynotification else R.drawable.info),
                            title = { Text(stringResource(R.string.about), color = FrequencyColors.SonicWhite) },
                            description = {
                                if (isUpdateAvailable) {
                                    Text(
                                        text = stringResource(R.string.update_available),
                                        color = MaterialTheme.colorScheme.error
                                    )
                                } else {
                                    Text("Frequency v • Firmware & updates", color = FrequencyColors.SonicMuted)
                                }
                            },
                            onClick = { navController.navigate("settings/about") },
                            isExpressive = true,
                            descriptionBelow = true
                        )
                    )
                )
            }
        }'''

content = content.replace(old_categories, new_categories)

with open(path, 'w', encoding='utf-8') as f:
    f.write(content)
print("Updated SettingsScreen.kt successfully!")
