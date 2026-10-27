package com.david.frequency.ui.screens.settings

import android.app.Activity
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.david.frequency.LocalPlayerAwareWindowInsets
import com.david.frequency.R
import com.david.frequency.constants.*
import com.david.frequency.ui.component.*
import com.david.frequency.ui.theme.FrequencyColors
import com.david.frequency.ui.utils.backToMain
import com.david.frequency.utils.rememberEnumPreference
import com.david.frequency.utils.rememberPreference

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppearanceSettings(
    navController: NavController,
    scrollBehavior: TopAppBarScrollBehavior,
    activity: Activity,
    snackbarHostState: SnackbarHostState,
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    val (darkMode, onDarkModeChange) = rememberEnumPreference(DarkModeKey, defaultValue = DarkMode.AUTO)
    val (pureBlack, onPureBlackChange) = rememberPreference(PureBlackKey, defaultValue = false)
    val (selectedFontValue) = rememberPreference(SelectedFontKey, defaultValue = AppFont.SYSTEM.value)
    val (slimNav, onSlimNavChange) = rememberPreference(SlimNavBarKey, defaultValue = false)
    val (floatingNavBar, onFloatingNavBarChange) = rememberPreference(FloatingNavBarKey, defaultValue = true)
    val (gridItemSize, onGridItemSizeChange) = rememberEnumPreference(GridItemsSizeKey, defaultValue = GridItemSize.SMALL)

    val isSystemDark = isSystemInDarkTheme()
    val isDark = when (darkMode) {
        DarkMode.AUTO -> isSystemDark
        DarkMode.ON -> true
        DarkMode.OFF -> false
    }

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
            // Group 1: Theme & Display Mode (Top Hero Card)
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLow
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        text = stringResource(R.string.theme_mode),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    // 3-way segmented pill selector
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceContainer)
                            .padding(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        ThemeModePill(
                            title = stringResource(R.string.dark_theme_follow_system),
                            icon = painterResource(R.drawable.sync),
                            selected = darkMode == DarkMode.AUTO,
                            onClick = { onDarkModeChange(DarkMode.AUTO) },
                            modifier = Modifier.weight(1f)
                        )
                        ThemeModePill(
                            title = stringResource(R.string.dark_theme_off),
                            icon = painterResource(R.drawable.contrast),
                            selected = darkMode == DarkMode.OFF,
                            onClick = { onDarkModeChange(DarkMode.OFF) },
                            modifier = Modifier.weight(1f)
                        )
                        ThemeModePill(
                            title = stringResource(R.string.dark_theme_on),
                            icon = painterResource(R.drawable.bedtime),
                            selected = darkMode == DarkMode.ON,
                            onClick = { onDarkModeChange(DarkMode.ON) },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    // Pure black switch (visible when Dark mode or Auto in dark is active)
                    AnimatedVisibility(
                        visible = isDark,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onPureBlackChange(!pureBlack) }
                                .padding(top = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = stringResource(R.string.pure_black),
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "Pure black background for AMOLED displays",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Switch(
                                checked = pureBlack,
                                onCheckedChange = onPureBlackChange
                            )
                        }
                    }
                }
            }

            // Gateway Item: Theme & Accent Palette
            Material3SettingsGroup(
                items = listOf(
                    Material3SettingsItem(
                        icon = painterResource(R.drawable.palette),
                        title = { Text(stringResource(R.string.theme_colors)) },
                        description = { Text("Material You wallpaper & custom palettes") },
                        trailingContent = {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(16.dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.primary)
                                )
                                Box(
                                    modifier = Modifier
                                        .size(14.dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.secondary)
                                )
                                Box(
                                    modifier = Modifier
                                        .size(12.dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.tertiary)
                                )
                            }
                        },
                        onClick = { navController.navigate("settings/appearance/theme") },
                        isExpressive = true,
                        descriptionBelow = true
                    )
                )
            )

            // Group 2: Typography & Artwork
            Material3SettingsGroup(
                title = "Typography & Artwork",
                items = listOf(
                    Material3SettingsItem(
                        icon = painterResource(R.drawable.alphabet_cyrillic),
                        title = { Text("App Font Studio") },
                        description = {
                            Text(
                                AppFont.fromValue(selectedFontValue).name
                                    .replace('_', ' ')
                                    .lowercase()
                                    .replaceFirstChar { it.uppercase() }
                            )
                        },
                        onClick = { navController.navigate("settings/appearance/font") },
                        isExpressive = true,
                        descriptionBelow = true
                    ),
                    Material3SettingsItem(
                        icon = painterResource(R.drawable.canvas_art),
                        title = { Text("Frequency Canvas") },
                        description = { Text("Animated video loops behind tracks") },
                        onClick = { navController.navigate("settings/appearance/canvas") },
                        isExpressive = true,
                        descriptionBelow = true
                    )
                )
            )

            // Group 3: Navigation Shell (The Sonic Dock)
            Material3SettingsGroup(
                title = "Navigation Shell",
                items = listOf(
                    Material3SettingsItem(
                        icon = painterResource(R.drawable.nav_bar),
                        title = { Text("Floating Sonic Dock") },
                        description = { Text("Suspended acoustic capsule navigation") },
                        trailingContent = {
                            Switch(checked = floatingNavBar, onCheckedChange = onFloatingNavBarChange)
                        },
                        onClick = { onFloatingNavBarChange(!floatingNavBar) },
                        isExpressive = true,
                        descriptionBelow = true
                    ),
                    Material3SettingsItem(
                        icon = painterResource(R.drawable.dock_to_top),
                        title = { Text("Compact Navigation Bar") },
                        description = { Text("Slimmer height for higher content density") },
                        trailingContent = {
                            Switch(checked = slimNav, onCheckedChange = onSlimNavChange)
                        },
                        onClick = { onSlimNavChange(!slimNav) },
                        isExpressive = true,
                        descriptionBelow = true
                    )
                )
            )

            // Group 4: Library & Lists
            Material3SettingsGroup(
                title = "Lists & Cards",
                items = listOf(
                    Material3SettingsItem(
                        icon = painterResource(R.drawable.grid_view),
                        title = { Text("Grid Item Size") },
                        description = { Text(gridItemSize.name.lowercase().replaceFirstChar { it.uppercase() }) },
                        onClick = { showGridSizeDialog = true },
                        isExpressive = true,
                        descriptionBelow = true
                    )
                )
            )

            val (sliderStyle, onSliderStyleChange) = rememberEnumPreference(SliderStyleKey, SliderStyle.DEFAULT)
            var showSliderStyleDialog by remember { mutableStateOf(false) }
            val (rotatingThumbnail, onRotatingThumbnailChange) = rememberPreference(RotatingThumbnailKey, defaultValue = true)
            val (playerBackground, onPlayerBackgroundChange) = rememberEnumPreference(PlayerBackgroundStyleKey, defaultValue = PlayerBackgroundStyle.GRADIENT)
            var showPlayerBgDialog by remember { mutableStateOf(false) }

            // Group 5: Player Visuals
            Material3SettingsGroup(
                title = "Player Visuals",
                items = listOf(
                    Material3SettingsItem(
                        icon = painterResource(R.drawable.gradient),
                        title = { Text("Player Background") },
                        description = {
                            Text(when (playerBackground) {
                                PlayerBackgroundStyle.DEFAULT       -> "Default"
                                PlayerBackgroundStyle.GRADIENT      -> "Gradient"
                                PlayerBackgroundStyle.BLUR          -> "Blur"
                                PlayerBackgroundStyle.GLOW_ANIMATED -> "Glow (Animated)"
                                PlayerBackgroundStyle.APPLE_MUSIC   -> "Apple Music"
                                PlayerBackgroundStyle.LIVE_MESH     -> "Live Mesh"
                            })
                        },
                        onClick = { showPlayerBgDialog = true },
                        isExpressive = true,
                        descriptionBelow = true
                    ),
                    Material3SettingsItem(
                        icon = painterResource(R.drawable.linear_scale),
                        title = { Text("Slider Style") },
                        description = { Text(sliderStyle.name.lowercase().replaceFirstChar { it.uppercase() }) },
                        onClick = { showSliderStyleDialog = true },
                        isExpressive = true,
                        descriptionBelow = true
                    ),
                    Material3SettingsItem(
                        icon = painterResource(R.drawable.album),
                        title = { Text("Rotating Thumbnail") },
                        description = { Text("Spin the album art while playing") },
                        trailingContent = {
                            Switch(checked = rotatingThumbnail, onCheckedChange = onRotatingThumbnailChange)
                        },
                        onClick = { onRotatingThumbnailChange(!rotatingThumbnail) },
                        isExpressive = true,
                        descriptionBelow = true
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

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun ThemeModePill(
    title: String,
    icon: Painter,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isLight = MaterialTheme.colorScheme.surface.luminance() > 0.5f
    val activeBg = if (isLight) FrequencyColors.ElectricTeal.copy(alpha = 0.16f) else FrequencyColors.ElectricAqua.copy(alpha = 0.22f)
    val activeBorder = if (isLight) FrequencyColors.ElectricTeal.copy(alpha = 0.5f) else FrequencyColors.ElectricAqua.copy(alpha = 0.6f)
    val activeText = if (isLight) FrequencyColors.ElectricTeal else FrequencyColors.SonicCyan
    val inactiveText = MaterialTheme.colorScheme.onSurfaceVariant

    val bgColor by animateColorAsState(targetValue = if (selected) activeBg else Color.Transparent, label = "pillBg")
    val textColor by animateColorAsState(targetValue = if (selected) activeText else inactiveText, label = "pillText")

    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(bgColor)
            .then(
                if (selected) Modifier.border(1.dp, activeBorder, CircleShape) else Modifier
            )
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                painter = icon,
                contentDescription = null,
                tint = textColor,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                color = textColor,
                maxLines = 1
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