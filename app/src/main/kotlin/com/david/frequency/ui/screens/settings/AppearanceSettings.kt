package com.david.frequency.ui.screens.settings

import android.app.Activity
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
fun AppearanceSettings(
    navController: NavController,
    scrollBehavior: TopAppBarScrollBehavior,
    activity: Activity,
    snackbarHostState: SnackbarHostState,
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    
    val (selectedFontValue, onSelectedFontChange) = rememberPreference(SelectedFontKey, defaultValue = AppFont.SYSTEM.value)
    val (slimNav, onSlimNavChange) = rememberPreference(SlimNavBarKey, defaultValue = false)
    val (floatingNavBar, onFloatingNavBarChange) = rememberPreference(FloatingNavBarKey, defaultValue = false)
    val (gridItemSize, onGridItemSizeChange) = rememberEnumPreference(GridItemsSizeKey, defaultValue = GridItemSize.SMALL)

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
                        icon = null,
                        title = { Text("App Font") },
                        description = { Text("Select typography for the app") },
                        onClick = { showFontDialog = true }
                    )
                )
            )

            Material3SettingsGroup(
                title = "Navigation",
                items = listOf(
                    Material3SettingsItem(
                        icon = painterResource(R.drawable.nav_bar),
                        title = { Text("Slim Navigation Bar") },
                        description = { Text("Use a slimmer bottom navigation bar") },
                        trailingContent = {
                            Switch(checked = slimNav, onCheckedChange = onSlimNavChange)
                        },
                        onClick = { onSlimNavChange(!slimNav) }
                    ),
                    Material3SettingsItem(
                        icon = painterResource(R.drawable.nav_bar),
                        title = { Text("Floating Navigation Bar") },
                        description = { Text("Make the navigation bar float above content") },
                        trailingContent = {
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
                        title = { Text("Grid Item Size") },
                        description = { Text("Adjust size of albums and artists in grids") },
                        onClick = { showGridSizeDialog = true }
                    )
                )
            )

            val (sliderStyle, onSliderStyleChange) = rememberEnumPreference(SliderStyleKey, SliderStyle.DEFAULT)
            var showSliderStyleDialog by remember { mutableStateOf(false) }
            val (rotatingThumbnail, onRotatingThumbnailChange) = rememberPreference(RotatingThumbnailKey, defaultValue = true)
            val (playerBackground, onPlayerBackgroundChange) = rememberEnumPreference(PlayerBackgroundStyleKey, defaultValue = PlayerBackgroundStyle.GRADIENT)
            var showPlayerBgDialog by remember { mutableStateOf(false) }

            Material3SettingsGroup(
                title = "Player",
                items = listOf(
                    Material3SettingsItem(
                        icon = painterResource(R.drawable.gradient),
                        title = { Text("Player Background") },
                        description = {
                            Text(when (playerBackground) {
                                PlayerBackgroundStyle.DEFAULT   -> "Default"
                                PlayerBackgroundStyle.GRADIENT  -> "Gradient"
                                PlayerBackgroundStyle.BLUR      -> "Blur"
                                PlayerBackgroundStyle.GLOW_ANIMATED -> "Glow (Animated)"
                                PlayerBackgroundStyle.APPLE_MUSIC   -> "Apple Music"
                                PlayerBackgroundStyle.LIVE_MESH     -> "Live Mesh"
                            })
                        },
                        onClick = { showPlayerBgDialog = true }
                    ),
                    Material3SettingsItem(
                        icon = painterResource(R.drawable.linear_scale),
                        title = { Text("Slider Style") },
                        description = { Text(sliderStyle.name.lowercase().replaceFirstChar { it.uppercase() }) },
                        onClick = { showSliderStyleDialog = true }
                    ),
                    Material3SettingsItem(
                        icon = painterResource(R.drawable.album),
                        title = { Text("Rotating Thumbnail") },
                        description = { Text("Spin the album art while playing") },
                        trailingContent = {
                            Switch(checked = rotatingThumbnail, onCheckedChange = onRotatingThumbnailChange)
                        },
                        onClick = { onRotatingThumbnailChange(!rotatingThumbnail) }
                    )
                )
            )

            if (showPlayerBgDialog) {
                EnumDialog(
                    title = "Player Background",
                    values = PlayerBackgroundStyle.entries,
                    current = playerBackground,
                    onSelect = {
                        onPlayerBackgroundChange(it)
                        showPlayerBgDialog = false
                    },
                    onDismiss = { showPlayerBgDialog = false },
                    valueText = {
                        when (it) {
                            PlayerBackgroundStyle.DEFAULT       -> "Default"
                            PlayerBackgroundStyle.GRADIENT      -> "Gradient"
                            PlayerBackgroundStyle.BLUR          -> "Blur"
                            PlayerBackgroundStyle.GLOW_ANIMATED -> "Glow (Animated)"
                            PlayerBackgroundStyle.APPLE_MUSIC   -> "Apple Music"
                            PlayerBackgroundStyle.LIVE_MESH     -> "Live Mesh"
                        }
                    }
                )
            }

            if (showSliderStyleDialog) {
                EnumDialog(
                    title = "Slider Style",
                    values = SliderStyle.entries,
                    current = sliderStyle,
                    onSelect = { 
                        onSliderStyleChange(it)
                        showSliderStyleDialog = false 
                    },
                    onDismiss = { showSliderStyleDialog = false },
                    valueText = { it.name.lowercase().replaceFirstChar { char -> char.uppercase() } }
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
        }

        if (showFontDialog) {
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
        }
    }
}


enum class DarkMode {
    ON,
    OFF,
    AUTO,
}

enum class NavigationTab {
    HOME,
    SEARCH,
    LIBRARY,
}

enum class LyricsPosition {
    LEFT,
    CENTER,
    RIGHT,
}

enum class PlayerTextAlignment {
    SIDED,
    CENTER,
}


enum class PlayerDesignOption {
    CLASSIC
    ,NEW
    ,V2
}

enum class MiniPlayerDesignOption {
    CLASSIC,
    NEW,
    APPLE
}