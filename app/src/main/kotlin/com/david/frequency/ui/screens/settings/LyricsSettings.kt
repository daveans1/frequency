/**
 * vivimusic Project (C) 2026
 * Licensed under GPL-3.0 | See git history for contributors
 */

package com.david.frequency.ui.screens.settings

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.david.frequency.LocalPlayerAwareWindowInsets
import com.david.frequency.R
import com.david.frequency.constants.AppleMusicLyricsBlurKey
import com.david.frequency.constants.EnableBetterLyricsKey
import com.david.frequency.constants.EnableKugouKey
import com.david.frequency.constants.EnableLyricsThumbnailPlayPauseKey
import com.david.frequency.constants.EnableLrcLibKey
import com.david.frequency.constants.EnableMusixmatchKey
import com.david.frequency.constants.EnablePaxsenixKey
import com.david.frequency.constants.EnableSimpMusicKey
import com.david.frequency.constants.EnableYouLyPlusKey
import com.david.frequency.constants.LyricsAnimationStyle
import com.david.frequency.constants.LyricsAnimationStyleKey
import com.david.frequency.constants.LyricsClickKey
import com.david.frequency.constants.LyricsGlowEffectKey
import com.david.frequency.constants.LyricsLineSpacingKey
import com.david.frequency.constants.LyricsProviderOrderKey
import com.david.frequency.constants.LyricsScrollKey
import com.david.frequency.constants.LyricsStandardBlurKey
import com.david.frequency.constants.LyricsTextPositionKey
import com.david.frequency.constants.LyricsTextSizeKey
import com.david.frequency.constants.SwipeLyricsKey
import com.david.frequency.lyrics.LyricsProviderRegistry
import androidx.compose.material3.Slider
import androidx.compose.runtime.mutableFloatStateOf
import com.david.frequency.ui.component.DefaultDialog
import com.david.frequency.ui.component.DraggableLyricsProviderItem
import com.david.frequency.ui.component.DraggableLyricsProviderList
import com.david.frequency.ui.component.EnumDialog
import com.david.frequency.ui.component.IconButton
import com.david.frequency.ui.component.Material3SettingsGroup
import com.david.frequency.ui.component.Material3SettingsItem
import com.david.frequency.ui.utils.backToMain
import com.david.frequency.utils.rememberEnumPreference
import com.david.frequency.utils.rememberPreference
import racra.compose.smooth_corner_rect_library.AbsoluteSmoothCornerShape
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LyricsSettings(
    navController: NavController,
    scrollBehavior: TopAppBarScrollBehavior,
) {
    val context = LocalContext.current

    // Providers preferences
    val (enableKugou, onEnableKugouChange) = rememberPreference(key = EnableKugouKey, defaultValue = true)
    val (enableLrclib, onEnableLrclibChange) = rememberPreference(key = EnableLrcLibKey, defaultValue = true)
    val (enableBetterLyrics, onEnableBetterLyricsChange) = rememberPreference(key = EnableBetterLyricsKey, defaultValue = true)
    val (enableMusixmatch, onEnableMusixmatchChange) = rememberPreference(key = EnableMusixmatchKey, defaultValue = true)
    val (enableSimpMusic, onEnableSimpMusicChange) = rememberPreference(key = EnableSimpMusicKey, defaultValue = true)
    val (enableYouLyPlus, onEnableYouLyPlusChange) = rememberPreference(key = EnableYouLyPlusKey, defaultValue = true)
    val (enablePaxsenix, onEnablePaxsenixChange) = rememberPreference(key = EnablePaxsenixKey, defaultValue = true)
    val (lyricsProviderOrder, onLyricsProviderOrderChange) = rememberPreference(
        key = LyricsProviderOrderKey,
        defaultValue = "",
    )

    // Visual preferences
    val (lyricsPosition, onLyricsPositionChange) = rememberEnumPreference(
        LyricsTextPositionKey,
        defaultValue = LyricsPosition.LEFT
    )
    val (lyricsClick, onLyricsClickChange) = rememberPreference(LyricsClickKey, defaultValue = true)
    val (lyricsScroll, onLyricsScrollChange) = rememberPreference(LyricsScrollKey, defaultValue = true)
    val (lyricsAnimationStyle, onLyricsAnimationStyleChange) = rememberEnumPreference(
        LyricsAnimationStyleKey,
        defaultValue = LyricsAnimationStyle.APPLE_V2
    )
    val (lyricsTextSize, onLyricsTextSizeChange) = rememberPreference(LyricsTextSizeKey, defaultValue = 24f)
    val (lyricsLineSpacing, onLyricsLineSpacingChange) = rememberPreference(LyricsLineSpacingKey, defaultValue = 1.3f)
    val (lyricsGlowEffect, onLyricsGlowEffectChange) = rememberPreference(LyricsGlowEffectKey, defaultValue = false)
    val (appleMusicLyricsBlur, onAppleMusicLyricsBlurChange) = rememberPreference(AppleMusicLyricsBlurKey, defaultValue = true)
    val (lyricsStandardBlur, onLyricsStandardBlurChange) = rememberPreference(LyricsStandardBlurKey, defaultValue = false)
    val (swipeLyrics, onSwipeLyricsChange) = rememberPreference(SwipeLyricsKey, defaultValue = false)
    val (enableLyricsThumbnailPlayPause, onEnableLyricsThumbnailPlayPauseChange) = rememberPreference(
        EnableLyricsThumbnailPlayPauseKey,
        defaultValue = false
    )

    var showLyricsPositionDialog by rememberSaveable { mutableStateOf(false) }
    var showLyricsAnimationStyleDialog by rememberSaveable { mutableStateOf(false) }
    var showLyricsTextSizeDialog by rememberSaveable { mutableStateOf(false) }
    var showLyricsLineSpacingDialog by rememberSaveable { mutableStateOf(false) }
    var showProviderPriorityDialog by rememberSaveable { mutableStateOf(false) }

    if (showLyricsPositionDialog) {
        EnumDialog(
            onDismiss = { showLyricsPositionDialog = false },
            onSelect = {
                onLyricsPositionChange(it)
                showLyricsPositionDialog = false
            },
            title = stringResource(R.string.lyrics_text_position),
            current = lyricsPosition,
            values = LyricsPosition.values().toList(),
            valueText = {
                when (it) {
                    LyricsPosition.LEFT -> stringResource(R.string.left)
                    LyricsPosition.CENTER -> stringResource(R.string.center)
                    LyricsPosition.RIGHT -> stringResource(R.string.right)
                }
            }
        )
    }

    if (showLyricsAnimationStyleDialog) {
        EnumDialog(
            onDismiss = { showLyricsAnimationStyleDialog = false },
            onSelect = {
                onLyricsAnimationStyleChange(it)
                showLyricsAnimationStyleDialog = false
            },
            title = stringResource(R.string.lyrics_animation_style),
            current = lyricsAnimationStyle,
            values = LyricsAnimationStyle.values().toList(),
            valueText = {
                when (it) {
                    LyricsAnimationStyle.NONE -> stringResource(R.string.none)
                    LyricsAnimationStyle.FADE -> stringResource(R.string.fade)
                    LyricsAnimationStyle.GLOW -> stringResource(R.string.glow)
                    LyricsAnimationStyle.SLIDE -> stringResource(R.string.slide)
                    LyricsAnimationStyle.KARAOKE -> stringResource(R.string.karaoke)
                    LyricsAnimationStyle.VIVIMUSIC_1 -> stringResource(R.string.frequency_1)
                    LyricsAnimationStyle.APPLE -> stringResource(R.string.apple_music_style)
                    LyricsAnimationStyle.APPLE_V2 -> stringResource(R.string.apple_music_style_letter)
                    LyricsAnimationStyle.LYRICS_V2 -> stringResource(R.string.lyrics_v2_fluid)
                    LyricsAnimationStyle.METRO_LYRICS -> stringResource(R.string.lyrics_animation_metro)
                }
            }
        )
    }

    if (showLyricsTextSizeDialog) {
        var tempTextSize by remember { mutableFloatStateOf(lyricsTextSize) }
        DefaultDialog(
            onDismiss = { 
                tempTextSize = lyricsTextSize
                showLyricsTextSizeDialog = false 
            },
            buttons = {
                TextButton(
                    onClick = { 
                        tempTextSize = 24f
                    }
                ) {
                    Text(stringResource(R.string.reset))
                }
                Spacer(modifier = Modifier.weight(1f))
                TextButton(
                    onClick = { 
                        tempTextSize = lyricsTextSize
                        showLyricsTextSizeDialog = false 
                    }
                ) {
                    Text(stringResource(android.R.string.cancel))
                }
                TextButton(
                    onClick = { 
                        onLyricsTextSizeChange(tempTextSize)
                        showLyricsTextSizeDialog = false 
                    }
                ) {
                    Text(stringResource(android.R.string.ok))
                }
            }
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.lyrics_text_size),
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Text(
                    text = "${tempTextSize.roundToInt()} sp",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Slider(
                    value = tempTextSize,
                    onValueChange = { tempTextSize = it },
                    valueRange = 16f..36f,
                    steps = 19,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }

    if (showLyricsLineSpacingDialog) {
        var tempLineSpacing by remember { mutableFloatStateOf(lyricsLineSpacing) }
        DefaultDialog(
            onDismiss = { 
                tempLineSpacing = lyricsLineSpacing
                showLyricsLineSpacingDialog = false 
            },
            buttons = {
                TextButton(
                    onClick = { 
                        tempLineSpacing = 1.3f
                    }
                ) {
                    Text(stringResource(R.string.reset))
                }
                Spacer(modifier = Modifier.weight(1f))
                TextButton(
                    onClick = { 
                        tempLineSpacing = lyricsLineSpacing
                        showLyricsLineSpacingDialog = false 
                    }
                ) {
                    Text(stringResource(android.R.string.cancel))
                }
                TextButton(
                    onClick = { 
                        onLyricsLineSpacingChange(tempLineSpacing)
                        showLyricsLineSpacingDialog = false 
                    }
                ) {
                    Text(stringResource(android.R.string.ok))
                }
            }
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.lyrics_line_spacing),
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Text(
                    text = "${String.format("%.1f", tempLineSpacing)}x",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Slider(
                    value = tempLineSpacing,
                    onValueChange = { tempLineSpacing = it },
                    valueRange = 1.0f..2.5f,
                    steps = 14,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }

    if (showProviderPriorityDialog) {
        val defaultOrder = LyricsProviderRegistry.getDefaultProviderOrder()
        val userToggleable = setOf("YouLyPlus", "Paxsenix", "BetterLyrics", "Musixmatch", "SimpMusic", "LrcLib", "Kugou")
        val enabledProviders = setOfNotNull(
            "LrcLib".takeIf { enableLrclib },
            "Kugou".takeIf { enableKugou },
            "BetterLyrics".takeIf { enableBetterLyrics },
            "Musixmatch".takeIf { enableMusixmatch },
            "SimpMusic".takeIf { enableSimpMusic },
            "YouLyPlus".takeIf { enableYouLyPlus },
            "Paxsenix".takeIf { enablePaxsenix },
        )

        val savedOrder = LyricsProviderRegistry.deserializeProviderOrder(lyricsProviderOrder)
        val normalizedOrder = savedOrder + defaultOrder.filter { it !in savedOrder }
        val lyricsIcon = painterResource(R.drawable.lyrics)
        val draggableItems = remember { mutableStateListOf<DraggableLyricsProviderItem>() }

        LaunchedEffect(normalizedOrder, enabledProviders) {
            val orderedEnabled = normalizedOrder.filter { it in enabledProviders }
            draggableItems.clear()
            draggableItems.addAll(
                orderedEnabled.map { name ->
                    DraggableLyricsProviderItem(
                        id = name,
                        name = LyricsProviderRegistry.getDisplayName(name),
                        icon = lyricsIcon,
                    )
                }
            )
        }

        val cardShape = AbsoluteSmoothCornerShape(30.dp, 60)
        val blockShape = AbsoluteSmoothCornerShape(22.dp, 60)
        val actionShape = AbsoluteSmoothCornerShape(18.dp, 60)

        BasicAlertDialog(onDismissRequest = { showProviderPriorityDialog = false }) {
            Surface(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .widthIn(max = 360.dp),
                shape = cardShape,
                color = MaterialTheme.colorScheme.surfaceContainerHigh,
                tonalElevation = 8.dp,
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 18.dp, vertical = 18.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                ) {
                    Surface(
                        shape = blockShape,
                        color = MaterialTheme.colorScheme.surfaceContainer,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Column(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 14.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp),
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    shape = AbsoluteSmoothCornerShape(12.dp, 60),
                                    color = MaterialTheme.colorScheme.secondaryContainer,
                                    modifier = Modifier.size(36.dp),
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            painter = painterResource(R.drawable.lyrics),
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.onSecondaryContainer,
                                            modifier = Modifier.size(20.dp),
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.size(10.dp))
                                Text(
                                    text = stringResource(R.string.lyrics_provider_priority),
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurface,
                                )
                            }
                            Text(
                                text = stringResource(R.string.lyrics_provider_priority_desc),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }

                    DraggableLyricsProviderList(
                        items = draggableItems,
                        onItemsReordered = { newOrder ->
                            draggableItems.clear()
                            draggableItems.addAll(newOrder)
                        },
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Surface(
                            shape = actionShape,
                            color = MaterialTheme.colorScheme.surfaceContainer,
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp),
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .clickable {
                                        onLyricsProviderOrderChange("")
                                        showProviderPriorityDialog = false
                                    }
                                    .padding(horizontal = 12.dp),
                            ) {
                                Text(
                                    text = stringResource(R.string.reset),
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.error,
                                )
                            }
                        }

                        Surface(
                            shape = actionShape,
                            color = MaterialTheme.colorScheme.surfaceContainer,
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp),
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .clickable { showProviderPriorityDialog = false }
                                    .padding(horizontal = 12.dp),
                            ) {
                                Text(
                                    text = stringResource(android.R.string.cancel),
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                        }

                        Surface(
                            shape = actionShape,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .weight(1.2f)
                                .height(44.dp),
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .clickable {
                                        val newOrderIds = draggableItems.map { it.id }
                                        val remaining = normalizedOrder.filter { it !in newOrderIds }
                                        val fullNewOrder = newOrderIds + remaining
                                        onLyricsProviderOrderChange(LyricsProviderRegistry.serializeProviderOrder(fullNewOrder))
                                        showProviderPriorityDialog = false
                                    }
                                    .padding(horizontal = 12.dp),
                            ) {
                                Text(
                                    text = stringResource(android.R.string.ok),
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.onPrimary,
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    Column(
        Modifier
            .windowInsetsPadding(
                LocalPlayerAwareWindowInsets.current.only(
                    WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom
                )
            )
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
    ) {
        Spacer(
            Modifier.windowInsetsPadding(
                LocalPlayerAwareWindowInsets.current.only(WindowInsetsSides.Top)
            )
        )

        // Section 1: Providers & Priority
        Material3SettingsGroup(
            title = stringResource(R.string.lyrics_providers),
            items = listOf(
                Material3SettingsItem(
                    icon = painterResource(R.drawable.lyrics),
                    title = { Text(stringResource(R.string.lyrics_provider_priority)) },
                    description = { Text(stringResource(R.string.lyrics_provider_priority_desc)) },
                    onClick = { showProviderPriorityDialog = true },
                    isExpressive = true,
                    descriptionBelow = true
                ),
                Material3SettingsItem(
                    title = { Text("YouLyPlus") },
                    trailingContent = {
                        Switch(
                            checked = enableYouLyPlus,
                            onCheckedChange = onEnableYouLyPlusChange,
                            thumbContent = {
                                Icon(
                                    painter = painterResource(
                                        id = if (enableYouLyPlus) R.drawable.check else R.drawable.close
                                    ),
                                    contentDescription = null,
                                    modifier = Modifier.size(SwitchDefaults.IconSize)
                                )
                            }
                        )
                    },
                    onClick = { onEnableYouLyPlusChange(!enableYouLyPlus) },
                    isExpressive = true
                ),
                Material3SettingsItem(
                    title = { Text("Musixmatch") },
                    trailingContent = {
                        Switch(
                            checked = enableMusixmatch,
                            onCheckedChange = onEnableMusixmatchChange,
                            thumbContent = {
                                Icon(
                                    painter = painterResource(
                                        id = if (enableMusixmatch) R.drawable.check else R.drawable.close
                                    ),
                                    contentDescription = null,
                                    modifier = Modifier.size(SwitchDefaults.IconSize)
                                )
                            }
                        )
                    },
                    onClick = { onEnableMusixmatchChange(!enableMusixmatch) },
                    isExpressive = true
                ),
                Material3SettingsItem(
                    title = { Text("Better Lyrics") },
                    trailingContent = {
                        Switch(
                            checked = enableBetterLyrics,
                            onCheckedChange = onEnableBetterLyricsChange,
                            thumbContent = {
                                Icon(
                                    painter = painterResource(
                                        id = if (enableBetterLyrics) R.drawable.check else R.drawable.close
                                    ),
                                    contentDescription = null,
                                    modifier = Modifier.size(SwitchDefaults.IconSize)
                                )
                            }
                        )
                    },
                    onClick = { onEnableBetterLyricsChange(!enableBetterLyrics) },
                    isExpressive = true
                ),
                Material3SettingsItem(
                    title = { Text("LrcLib") },
                    trailingContent = {
                        Switch(
                            checked = enableLrclib,
                            onCheckedChange = onEnableLrclibChange,
                            thumbContent = {
                                Icon(
                                    painter = painterResource(
                                        id = if (enableLrclib) R.drawable.check else R.drawable.close
                                    ),
                                    contentDescription = null,
                                    modifier = Modifier.size(SwitchDefaults.IconSize)
                                )
                            }
                        )
                    },
                    onClick = { onEnableLrclibChange(!enableLrclib) },
                    isExpressive = true
                ),
                Material3SettingsItem(
                    title = { Text("KuGou") },
                    trailingContent = {
                        Switch(
                            checked = enableKugou,
                            onCheckedChange = onEnableKugouChange,
                            thumbContent = {
                                Icon(
                                    painter = painterResource(
                                        id = if (enableKugou) R.drawable.check else R.drawable.close
                                    ),
                                    contentDescription = null,
                                    modifier = Modifier.size(SwitchDefaults.IconSize)
                                )
                            }
                        )
                    },
                    onClick = { onEnableKugouChange(!enableKugou) },
                    isExpressive = true
                ),
                Material3SettingsItem(
                    title = { Text("SimpMusic") },
                    trailingContent = {
                        Switch(
                            checked = enableSimpMusic,
                            onCheckedChange = onEnableSimpMusicChange,
                            thumbContent = {
                                Icon(
                                    painter = painterResource(
                                        id = if (enableSimpMusic) R.drawable.check else R.drawable.close
                                    ),
                                    contentDescription = null,
                                    modifier = Modifier.size(SwitchDefaults.IconSize)
                                )
                            }
                        )
                    },
                    onClick = { onEnableSimpMusicChange(!enableSimpMusic) },
                    isExpressive = true
                ),
                Material3SettingsItem(
                    title = { Text("PaxSenix") },
                    trailingContent = {
                        Switch(
                            checked = enablePaxsenix,
                            onCheckedChange = onEnablePaxsenixChange,
                            thumbContent = {
                                Icon(
                                    painter = painterResource(
                                        id = if (enablePaxsenix) R.drawable.check else R.drawable.close
                                    ),
                                    contentDescription = null,
                                    modifier = Modifier.size(SwitchDefaults.IconSize)
                                )
                            }
                        )
                    },
                    onClick = { onEnablePaxsenixChange(!enablePaxsenix) },
                    isExpressive = true
                ),
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Section 2: AI Lyrics Translation
        Material3SettingsGroup(
            title = stringResource(R.string.ai_lyrics_translation),
            items = listOf(
                Material3SettingsItem(
                    icon = painterResource(R.drawable.translate),
                    title = { Text(stringResource(R.string.ai_lyrics_translation)) },
                    description = { Text("Configure OpenRouter, DeepL, OpenAI, Claude, or Gemini") },
                    onClick = { navController.navigate("settings/ai") },
                    isExpressive = true,
                    descriptionBelow = true
                )
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Section 3: Visuals & Styling
        Material3SettingsGroup(
            title = stringResource(R.string.lyrics),
            items = listOfNotNull(
                Material3SettingsItem(
                    icon = painterResource(R.drawable.lyrics),
                    title = { Text(stringResource(R.string.lyrics_text_position)) },
                    description = {
                        Text(
                            when (lyricsPosition) {
                                LyricsPosition.LEFT -> stringResource(R.string.left)
                                LyricsPosition.CENTER -> stringResource(R.string.center)
                                LyricsPosition.RIGHT -> stringResource(R.string.right)
                            }
                        )
                    },
                    onClick = { showLyricsPositionDialog = true },
                    isExpressive = true
                ),
                Material3SettingsItem(
                    icon = painterResource(R.drawable.lyrics),
                    title = { Text(stringResource(R.string.lyrics_animation_style)) },
                    description = {
                        Text(
                            when (lyricsAnimationStyle) {
                                LyricsAnimationStyle.NONE -> stringResource(R.string.none)
                                LyricsAnimationStyle.FADE -> stringResource(R.string.fade)
                                LyricsAnimationStyle.GLOW -> stringResource(R.string.glow)
                                LyricsAnimationStyle.SLIDE -> stringResource(R.string.slide)
                                LyricsAnimationStyle.KARAOKE -> stringResource(R.string.karaoke)
                                LyricsAnimationStyle.VIVIMUSIC_1 -> stringResource(R.string.frequency_1)
                                LyricsAnimationStyle.APPLE -> stringResource(R.string.apple_music_style)
                                LyricsAnimationStyle.APPLE_V2 -> stringResource(R.string.apple_music_style_letter)
                                LyricsAnimationStyle.LYRICS_V2 -> stringResource(R.string.lyrics_v2_fluid)
                                LyricsAnimationStyle.METRO_LYRICS -> stringResource(R.string.lyrics_animation_metro)
                            }
                        )
                    },
                    onClick = { showLyricsAnimationStyleDialog = true },
                    isExpressive = true,
                    descriptionBelow = true
                ),
                Material3SettingsItem(
                    icon = painterResource(R.drawable.lyrics),
                    title = { Text(stringResource(R.string.lyrics_glow_effect)) },
                    description = { Text(stringResource(R.string.lyrics_glow_effect_desc)) },
                    trailingContent = {
                        Switch(
                            checked = lyricsGlowEffect,
                            onCheckedChange = onLyricsGlowEffectChange,
                            thumbContent = {
                                Icon(
                                    painter = painterResource(
                                        id = if (lyricsGlowEffect) R.drawable.check else R.drawable.close
                                    ),
                                    contentDescription = null,
                                    modifier = Modifier.size(SwitchDefaults.IconSize)
                                )
                            }
                        )
                    },
                    onClick = { onLyricsGlowEffectChange(!lyricsGlowEffect) },
                    isExpressive = true,
                    descriptionBelow = true
                ),
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && lyricsAnimationStyle == LyricsAnimationStyle.VIVIMUSIC_1) {
                    Material3SettingsItem(
                        icon = painterResource(R.drawable.lyrics),
                        title = { Text(stringResource(R.string.apple_music_lyrics_blur)) },
                        description = { Text(stringResource(R.string.apple_music_lyrics_blur_desc)) },
                        trailingContent = {
                            Switch(
                                checked = appleMusicLyricsBlur,
                                onCheckedChange = onAppleMusicLyricsBlurChange,
                                thumbContent = {
                                    Icon(
                                        painter = painterResource(
                                            id = if (appleMusicLyricsBlur) R.drawable.check else R.drawable.close
                                        ),
                                        contentDescription = null,
                                        modifier = Modifier.size(SwitchDefaults.IconSize)
                                    )
                                }
                            )
                        },
                        onClick = { onAppleMusicLyricsBlurChange(!appleMusicLyricsBlur) },
                        isExpressive = true,
                        descriptionBelow = true
                    )
                } else null,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    Material3SettingsItem(
                        icon = painterResource(R.drawable.lyrics),
                        title = { Text(stringResource(R.string.standard_lyrics_blur)) },
                        description = { Text(stringResource(R.string.apple_music_lyrics_blur_desc)) },
                        trailingContent = {
                            Switch(
                                checked = lyricsStandardBlur,
                                onCheckedChange = onLyricsStandardBlurChange,
                                thumbContent = {
                                    Icon(
                                        painter = painterResource(
                                            id = if (lyricsStandardBlur) R.drawable.check else R.drawable.close
                                        ),
                                        contentDescription = null,
                                        modifier = Modifier.size(SwitchDefaults.IconSize)
                                    )
                                }
                            )
                        },
                        onClick = { onLyricsStandardBlurChange(!lyricsStandardBlur) },
                        isExpressive = true,
                        descriptionBelow = true
                    )
                } else null,
                Material3SettingsItem(
                    icon = painterResource(R.drawable.lyrics),
                    title = { Text(stringResource(R.string.lyrics_text_size)) },
                    description = { Text("${lyricsTextSize.roundToInt()} sp") },
                    onClick = { showLyricsTextSizeDialog = true },
                    isExpressive = true
                ),
                Material3SettingsItem(
                    icon = painterResource(R.drawable.lyrics),
                    title = { Text(stringResource(R.string.lyrics_line_spacing)) },
                    description = { Text("${String.format("%.1f", lyricsLineSpacing)}x") },
                    onClick = { showLyricsLineSpacingDialog = true },
                    isExpressive = true
                )
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Section 4: Behavior & Gestures
        Material3SettingsGroup(
            title = stringResource(R.string.general),
            items = listOf(
                Material3SettingsItem(
                    icon = painterResource(R.drawable.lyrics),
                    title = { Text(stringResource(R.string.lyrics_click_change)) },
                    trailingContent = {
                        Switch(
                            checked = lyricsClick,
                            onCheckedChange = onLyricsClickChange,
                            thumbContent = {
                                Icon(
                                    painter = painterResource(
                                        id = if (lyricsClick) R.drawable.check else R.drawable.close
                                    ),
                                    contentDescription = null,
                                    modifier = Modifier.size(SwitchDefaults.IconSize)
                                )
                            }
                        )
                    },
                    onClick = { onLyricsClickChange(!lyricsClick) },
                    isExpressive = true
                ),
                Material3SettingsItem(
                    icon = painterResource(R.drawable.lyrics),
                    title = { Text(stringResource(R.string.lyrics_auto_scroll)) },
                    trailingContent = {
                        Switch(
                            checked = lyricsScroll,
                            onCheckedChange = onLyricsScrollChange,
                            thumbContent = {
                                Icon(
                                    painter = painterResource(
                                        id = if (lyricsScroll) R.drawable.check else R.drawable.close
                                    ),
                                    contentDescription = null,
                                    modifier = Modifier.size(SwitchDefaults.IconSize)
                                )
                            }
                        )
                    },
                    onClick = { onLyricsScrollChange(!lyricsScroll) },
                    isExpressive = true
                ),
                Material3SettingsItem(
                    icon = painterResource(R.drawable.swipe),
                    title = { Text(stringResource(R.string.lyrics_swipe_to_change_song)) },
                    description = { Text(stringResource(R.string.lyrics_swipe_to_change_song_desc)) },
                    trailingContent = {
                        Switch(
                            checked = swipeLyrics,
                            onCheckedChange = onSwipeLyricsChange,
                            thumbContent = {
                                Icon(
                                    painter = painterResource(
                                        id = if (swipeLyrics) R.drawable.check else R.drawable.close
                                    ),
                                    contentDescription = null,
                                    modifier = Modifier.size(SwitchDefaults.IconSize)
                                )
                            }
                        )
                    },
                    onClick = { onSwipeLyricsChange(!swipeLyrics) },
                    isExpressive = true,
                    descriptionBelow = true
                ),
                Material3SettingsItem(
                    icon = painterResource(R.drawable.play),
                    title = { Text(stringResource(R.string.lyrics_thumbnail_play_pause)) },
                    description = { Text(stringResource(R.string.lyrics_thumbnail_play_pause_desc)) },
                    trailingContent = {
                        Switch(
                            checked = enableLyricsThumbnailPlayPause,
                            onCheckedChange = onEnableLyricsThumbnailPlayPauseChange,
                            thumbContent = {
                                Icon(
                                    painter = painterResource(
                                        id = if (enableLyricsThumbnailPlayPause) R.drawable.check else R.drawable.close
                                    ),
                                    contentDescription = null,
                                    modifier = Modifier.size(SwitchDefaults.IconSize)
                                )
                            }
                        )
                    },
                    onClick = { onEnableLyricsThumbnailPlayPauseChange(!enableLyricsThumbnailPlayPause) },
                    isExpressive = true,
                    descriptionBelow = true
                ),
                Material3SettingsItem(
                    icon = painterResource(R.drawable.language_korean_latin),
                    title = { Text(stringResource(R.string.lyrics_romanization)) },
                    description = { Text("Korean Hangul, Japanese Romaji, Chinese Pinyin") },
                    onClick = { navController.navigate("settings/content/romanization") },
                    isExpressive = true,
                    descriptionBelow = true
                )
            )
        )

        Spacer(modifier = Modifier.height(24.dp))
    }

    TopAppBar(
        title = { Text(stringResource(R.string.lyrics)) },
        navigationIcon = {
            IconButton(
                onClick = navController::navigateUp,
                onLongClick = navController::backToMain,
            ) {
                Icon(
                    painterResource(R.drawable.arrow_back),
                    contentDescription = null,
                )
            }
        },
        scrollBehavior = scrollBehavior
    )
}
