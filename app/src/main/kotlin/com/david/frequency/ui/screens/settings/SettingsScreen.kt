/**
 * vivimusic Project (C) 2026
 * Licensed under GPL-3.0 | See git history for contributors
 */

package com.david.frequency.ui.screens.settings

import android.os.Build
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.david.frequency.BuildConfig
import com.david.frequency.LocalPlayerAwareWindowInsets
import com.david.frequency.R
import com.david.frequency.ui.component.IconButton
import com.david.frequency.ui.theme.FrequencyColors
import com.david.frequency.ui.component.AcousticBentoCard
import com.david.frequency.ui.component.AcousticTelemetryStrip
import com.david.frequency.ui.component.acousticGlass
import com.david.frequency.ui.component.Material3SettingsGroup
import com.david.frequency.ui.component.Material3SettingsItem
import com.david.frequency.ui.utils.backToMain
import com.david.frequency.vivimusic.updater.checkForUpdate
import com.david.frequency.vivimusic.updater.getAutoUpdateCheckSetting
import com.david.frequency.vivimusic.updater.getUpdateAvailableState
import com.david.frequency.vivimusic.updater.saveUpdateAvailableState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    scrollBehavior: TopAppBarScrollBehavior,
) {
    val context = LocalContext.current

    val autoUpdateSetting = remember { getAutoUpdateCheckSetting(context) }
    var isUpdateAvailable by remember { mutableStateOf(getUpdateAvailableState(context)) }

    var searchQuery by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue())
    }

    DisposableEffect(context) {
        val sharedPrefs = context.getSharedPreferences("settings", android.content.Context.MODE_PRIVATE)
        val listener = android.content.SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (key == "update_available") {
                isUpdateAvailable = getUpdateAvailableState(context)
            }
        }
        sharedPrefs.registerOnSharedPreferenceChangeListener(listener)
        isUpdateAvailable = getUpdateAvailableState(context)
        onDispose {
            sharedPrefs.unregisterOnSharedPreferenceChangeListener(listener)
        }
    }

    LaunchedEffect(Unit) {
        if (autoUpdateSetting) {
            checkForUpdate(
                context = context,
                onSuccess = { _, isAvailable, _, _, _, _, _, _ ->
                    saveUpdateAvailableState(context, isAvailable)
                },
                onError = {}
            )
        }
    }

    val searchResults = remember(searchQuery.text) {
        SettingsSearchRegistry.search(searchQuery.text, context)
    }

    Column(
        Modifier
            .windowInsetsPadding(LocalPlayerAwareWindowInsets.current.only(WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom))
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
    ) {
        Spacer(
            Modifier.windowInsetsPadding(
                LocalPlayerAwareWindowInsets.current.only(
                    WindowInsetsSides.Top
                )
            )
        )
        Text(
            text = stringResource(R.string.settings),
            style = MaterialTheme.typography.displaySmall.copy(
                fontWeight = FontWeight.SemiBold
            ),
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(start = 8.dp, top = 24.dp, bottom = 12.dp)
        )

        // Search Bar (Acoustic Glass)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 14.dp)
                .acousticGlass(cornerRadius = 50.dp, alpha = 0.75f)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.search),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Box(modifier = Modifier.weight(1f)) {
                    if (searchQuery.text.isEmpty()) {
                        Text(
                            text = stringResource(R.string.search),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                        )
                    }
                    BasicTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        singleLine = true,
                        textStyle = MaterialTheme.typography.bodyLarge.copy(
                            color = MaterialTheme.colorScheme.onSurface
                        ),
                        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                if (searchQuery.text.isNotEmpty()) {
                    IconButton(
                        onClick = { searchQuery = TextFieldValue("") },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.close),
                            contentDescription = stringResource(R.string.clear),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }

        if (searchQuery.text.isNotBlank()) {
            // Search Results Mode
            if (searchResults.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 48.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        painter = painterResource(R.drawable.search_off),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "No settings found",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                Material3SettingsGroup(
                    title = "Search Results (${searchResults.size})",
                    items = searchResults.map { result ->
                        Material3SettingsItem(
                            icon = painterResource(result.icon),
                            title = { Text(result.title) },
                            description = {
                                Text(
                                    text = "${result.category}${if (!result.description.isNullOrBlank()) " • ${result.description}" else ""}",
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            },
                            onClick = { navController.navigate(result.route) },
                            isExpressive = true,
                            descriptionBelow = true
                        )
                    }
                )
            }
        } else {
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
                                Text("v${BuildConfig.VERSION_NAME} • Updates, privacy, network")
                            }
                        },
                        onClick = { navController.navigate("settings/about") },
                        isExpressive = true,
                        descriptionBelow = true
                    )
                )
            )
        }

        Spacer(modifier = Modifier.height(50.dp))
    }

    TopAppBar(
        title = {},
        navigationIcon = {
            IconButton(
                onClick = navController::navigateUp,
                onLongClick = navController::backToMain
            ) {
                Icon(
                    painterResource(R.drawable.arrow_back),
                    contentDescription = null
                )
            }
        }
    )
}
