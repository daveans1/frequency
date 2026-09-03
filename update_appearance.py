path = 'app/src/main/kotlin/com/david/frequency/ui/screens/settings/AppearanceSettings.kt'
with open(path, 'r', encoding='utf-8') as f:
    content = f.read()

# 1. Import Color
import_find = 'import androidx.compose.ui.graphics.toArgb'
import_replace = 'import androidx.compose.ui.graphics.Color\nimport androidx.compose.ui.graphics.toArgb'
content = content.replace(import_find, import_replace)

# 2. Destructure onSelectedThemeColorChange
destruct_find = '''    val (selectedThemeColorInt) = rememberPreference(
        SelectedThemeColorKey,
        defaultValue = DefaultThemeColor.toArgb()
    )'''

destruct_replace = '''    val (selectedThemeColorInt, onSelectedThemeColorChange) = rememberPreference(
        SelectedThemeColorKey,
        defaultValue = DefaultThemeColor.toArgb()
    )'''
content = content.replace(destruct_find, destruct_replace)

# 3. Add Frequency Preset Item
item_find = '''                add(
                    Material3SettingsItem(
                        icon = painterResource(R.drawable.palette),
                        title = { Text(stringResource(R.string.theme)) },
                        onClick = { navController.navigate("settings/appearance/theme") },
                        isExpressive = true
                    )
                )'''

item_replace = '''                add(
                    Material3SettingsItem(
                        icon = painterResource(R.drawable.frequency_music_small_icon),
                        title = { Text(stringResource(R.string.frequency_vibe_preset)) },
                        description = { Text(stringResource(R.string.frequency_vibe_preset_desc)) },
                        onClick = {
                            onSelectedThemeColorChange(Color(0xFF20E0B0).toArgb())
                            onDynamicThemeChange(false)
                            onFloatingNavBarChange(true)
                            onUsePlayerV2Change(true)
                            onUseNewPlayerDesignChange(false)
                            onPlayerBackgroundChange(PlayerBackgroundStyle.GLOW_ANIMATED)
                            onSliderStyleChange(SliderStyle.WAVY)
                            onUseNewMiniPlayerDesignChange(true)
                            onUseExpressiveAlbumDesignChange(true)
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar(activity.getString(R.string.frequency_vibe_applied))
                            }
                        },
                        isExpressive = true,
                        descriptionBelow = true
                    )
                )
                add(
                    Material3SettingsItem(
                        icon = painterResource(R.drawable.palette),
                        title = { Text(stringResource(R.string.theme)) },
                        onClick = { navController.navigate("settings/appearance/theme") },
                        isExpressive = true
                    )
                )'''

content = content.replace(item_find, item_replace)

with open(path, 'w', encoding='utf-8') as f:
    f.write(content)
