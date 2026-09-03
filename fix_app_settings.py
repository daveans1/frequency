import re

path = r'app/src/main/kotlin/com/david/frequency/ui/screens/settings/AppearanceSettings.kt'
with open(path, 'r', encoding='utf-8') as f:
    content = f.read()

# I need to fix the Material3SettingsItem and EnumDialog calls
content = content.replace(
'''                    Material3SettingsItem(
                        icon = painterResource(R.drawable.text_font),
                        title = "App Font",
                        subtitle = "Select typography for the app",
                        onClick = { showFontDialog = true }
                    ),''',
'''                    Material3SettingsItem(
                        icon = painterResource(R.drawable.text_font),
                        title = { Text("App Font") },
                        description = { Text("Select typography for the app") },
                        onClick = { showFontDialog = true }
                    ),'''
)

content = content.replace(
'''                    Material3SettingsItem(
                        icon = painterResource(R.drawable.routine_theme),
                        title = "Ambient Album Backdrop",
                        subtitle = "Blur the playing album art into the app's background. Disabling this enforces pure Obsidian Black.",
                        trailing = {
                            Switch(
                                checked = ambientBackdrop,
                                onCheckedChange = onAmbientBackdropChange
                            )
                        },
                        onClick = { onAmbientBackdropChange(!ambientBackdrop) }
                    )''',
'''                    Material3SettingsItem(
                        icon = painterResource(R.drawable.routine_theme),
                        title = { Text("Ambient Album Backdrop") },
                        description = { Text("Blur the playing album art into the app's background. Disabling this enforces pure Obsidian Black.") },
                        trailingContent = {
                            Switch(
                                checked = ambientBackdrop,
                                onCheckedChange = onAmbientBackdropChange
                            )
                        },
                        onClick = { onAmbientBackdropChange(!ambientBackdrop) }
                    )'''
)

content = content.replace(
'''                    Material3SettingsItem(
                        icon = painterResource(R.drawable.nav_bar),
                        title = "Slim Navigation Bar",
                        subtitle = "Reduce the height of the bottom navigation bar",
                        trailing = {
                            Switch(checked = slimNav, onCheckedChange = onSlimNavChange)
                        },
                        onClick = { onSlimNavChange(!slimNav) }
                    ),''',
'''                    Material3SettingsItem(
                        icon = painterResource(R.drawable.nav_bar),
                        title = { Text("Slim Navigation Bar") },
                        description = { Text("Reduce the height of the bottom navigation bar") },
                        trailingContent = {
                            Switch(checked = slimNav, onCheckedChange = onSlimNavChange)
                        },
                        onClick = { onSlimNavChange(!slimNav) }
                    ),'''
)

content = content.replace(
'''                    Material3SettingsItem(
                        icon = painterResource(R.drawable.floating_nav),
                        title = "Floating Navigation Bar",
                        subtitle = "Elevate the navigation bar",
                        trailing = {
                            Switch(checked = floatingNavBar, onCheckedChange = onFloatingNavBarChange)
                        },
                        onClick = { onFloatingNavBarChange(!floatingNavBar) }
                    )''',
'''                    Material3SettingsItem(
                        icon = painterResource(R.drawable.floating_nav),
                        title = { Text("Floating Navigation Bar") },
                        description = { Text("Elevate the navigation bar") },
                        trailingContent = {
                            Switch(checked = floatingNavBar, onCheckedChange = onFloatingNavBarChange)
                        },
                        onClick = { onFloatingNavBarChange(!floatingNavBar) }
                    )'''
)

content = content.replace(
'''                    Material3SettingsItem(
                        icon = painterResource(R.drawable.grid_view),
                        title = "Grid Item Size",
                        subtitle = "Adjust size of albums and artists in grids",
                        onClick = { showGridSizeDialog = true }
                    )''',
'''                    Material3SettingsItem(
                        icon = painterResource(R.drawable.grid_view),
                        title = { Text("Grid Item Size") },
                        description = { Text("Adjust size of albums and artists in grids") },
                        onClick = { showGridSizeDialog = true }
                    )'''
)

content = content.replace(
'''        if (showFontDialog) {
            EnumDialog(
                title = "App Font",
                options = AppFont.entries.toTypedArray(),
                selectedOption = AppFont.fromValue(selectedFontValue),
                onOptionSelected = { onSelectedFontChange(it.value) },
                onDismissRequest = { showFontDialog = false },
                optionName = { it.name }
            )
        }

        if (showGridSizeDialog) {
            EnumDialog(
                title = "Grid Item Size",
                options = GridItemSize.entries.toTypedArray(),
                selectedOption = gridItemSize,
                onOptionSelected = onGridItemSizeChange,
                onDismissRequest = { showGridSizeDialog = false },
                optionName = { it.name }
            )
        }''',
'''        if (showFontDialog) {
            EnumDialog<AppFont>(
                title = "App Font",
                values = AppFont.entries,
                current = AppFont.fromValue(selectedFontValue),
                onSelect = { onSelectedFontChange(it.value) },
                onDismiss = { showFontDialog = false },
                valueText = { it.name }
            )
        }

        if (showGridSizeDialog) {
            EnumDialog<GridItemSize>(
                title = "Grid Item Size",
                values = GridItemSize.entries,
                current = gridItemSize,
                onSelect = onGridItemSizeChange,
                onDismiss = { showGridSizeDialog = false },
                valueText = { it.name }
            )
        }'''
)

# And fix loating_nav to ic_floating_nav if floating_nav doesn't exist
content = content.replace('R.drawable.floating_nav', 'R.drawable.nav_bar')

with open(path, 'w', encoding='utf-8') as f:
    f.write(content)
