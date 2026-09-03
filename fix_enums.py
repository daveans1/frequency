import re

path = r'app/src/main/kotlin/com/david/frequency/ui/screens/settings/AppearanceSettings.kt'
with open(path, 'r', encoding='utf-8') as f:
    content = f.read()

# Let's find all Material3SettingsGroup and comment them out except the first one.
# Wait, parsing brackets in Python for Kotlin is hard. Let's just find the enum classes at the bottom of the original file and copy them!

# Extract enum classes
enums = re.search(r'(enum class DarkMode.*)', content, re.DOTALL)
if enums:
    enum_text = enums.group(1)
    
    # We will write the streamlined version and append the enums
    new_content = """package com.david.frequency.ui.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.david.frequency.LocalPlayerAwareWindowInsets
import com.david.frequency.R
import com.david.frequency.constants.*
import com.david.frequency.ui.component.*
import com.david.frequency.ui.utils.backToMain
import com.david.frequency.utils.rememberEnumPreference
import com.david.frequency.utils.rememberPreference
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppearanceSettings(navController: NavController) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    
    val (selectedFontValue, onSelectedFontChange) = rememberPreference(SelectedFontKey, defaultValue = AppFont.SYSTEM.value)
    val (ambientBackdrop, onAmbientBackdropChange) = rememberPreference(AmbientBackdropKey, defaultValue = false)
    val (slimNav, onSlimNavChange) = rememberPreference(SlimNavBarKey, defaultValue = false)
    val (floatingNavBar, onFloatingNavBarChange) = rememberPreference(FloatingNavBarKey, defaultValue = false)
    val (gridItemSize, onGridItemSizeChange) = rememberEnumPreference(GridItemsSizeKey, defaultValue = GridItemSize.Normal)

    var showFontDialog by remember { mutableStateOf(false) }
    var showGridSizeDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = { Text(stringResource(R.string.appearance)) },
                navigationIcon = {
                    IconButton(onClick = navController::navigateUp, onLongClick = navController::backToMain) {
                        Icon(
                            painter = painterResource(R.drawable.arrow_back),
                            contentDescription = null
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .windowInsetsPadding(LocalPlayerAwareWindowInsets.current)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Material3SettingsGroup(
                title = "Core Aesthetics",
                items = listOf(
                    Material3SettingsItem(
                        icon = painterResource(R.drawable.text_font),
                        title = "App Font",
                        subtitle = "Select typography for the app",
                        onClick = { showFontDialog = true }
                    ),
                    Material3SettingsItem(
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
                    )
                )
            )

            Material3SettingsGroup(
                title = "Navigation",
                items = listOf(
                    Material3SettingsItem(
                        icon = painterResource(R.drawable.nav_bar),
                        title = "Slim Navigation Bar",
                        subtitle = "Reduce the height of the bottom navigation bar",
                        trailing = {
                            Switch(checked = slimNav, onCheckedChange = onSlimNavChange)
                        },
                        onClick = { onSlimNavChange(!slimNav) }
                    ),
                    Material3SettingsItem(
                        icon = painterResource(R.drawable.floating_nav),
                        title = "Floating Navigation Bar",
                        subtitle = "Elevate the navigation bar",
                        trailing = {
                            Switch(checked = floatingNavBar, onCheckedChange = onFloatingNavBarChange)
                        },
                        onClick = { onFloatingNavBarChange(!floatingNavBar) }
                    )
                )
            )

            Material3SettingsGroup(
                title = "Lists & Cards",
                items = listOf(
                    Material3SettingsItem(
                        icon = painterResource(R.drawable.grid_view),
                        title = "Grid Item Size",
                        subtitle = "Adjust size of albums and artists in grids",
                        onClick = { showGridSizeDialog = true }
                    )
                )
            )
            
            Spacer(modifier = Modifier.height(24.dp))
        }

        if (showFontDialog) {
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
        }
    }
}
"""
    new_content += "\n\n" + enum_text
    
    with open(path, 'w', encoding='utf-8') as f:
        f.write(new_content)
