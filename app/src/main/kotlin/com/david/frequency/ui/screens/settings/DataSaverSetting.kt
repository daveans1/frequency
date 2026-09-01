/**
 * vivimusic Project (C) 2026
 * Licensed under GPL-3.0 | See git history for contributors
 */

package com.david.frequency.ui.screens.settings

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.david.frequency.LocalPlayerAwareWindowInsets
import com.david.frequency.R
import com.david.frequency.constants.AlbumCanvasEnabledKey
import com.david.frequency.constants.CanvasThumbnailAnimationKey
import com.david.frequency.constants.DataSaverBackupAlbumCanvasKey
import com.david.frequency.constants.DataSaverBackupArtistBgVideoKey
import com.david.frequency.constants.DataSaverBackupArtistVideoKey
import com.david.frequency.constants.DataSaverBackupCanvasKey
import com.david.frequency.constants.DataSaverKey
import com.david.frequency.constants.ShowArtistBackgroundVideoKey
import com.david.frequency.constants.ShowArtistVideoKey
import com.david.frequency.ui.component.IconButton
import com.david.frequency.ui.component.ModernSwitch
import com.david.frequency.ui.utils.backToMain
import com.david.frequency.utils.dataStore
import com.david.frequency.utils.rememberPreference
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DataSaverSetting(
    navController: NavController,
    scrollBehavior: TopAppBarScrollBehavior,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val dataSaver by rememberPreference(DataSaverKey, defaultValue = false)

    fun toggleDataSaver(enable: Boolean) {
        scope.launch {
            context.dataStore.edit { prefs ->
                if (enable) {
                    // Step 1: Backup current values before overriding
                    prefs[DataSaverBackupCanvasKey]        = prefs[CanvasThumbnailAnimationKey] ?: true
                    prefs[DataSaverBackupArtistVideoKey]   = prefs[ShowArtistVideoKey] ?: true
                    prefs[DataSaverBackupArtistBgVideoKey] = prefs[ShowArtistBackgroundVideoKey] ?: true
                    prefs[DataSaverBackupAlbumCanvasKey]   = prefs[AlbumCanvasEnabledKey] ?: false
                    // Step 2: Force everything off
                    prefs[CanvasThumbnailAnimationKey]  = false
                    prefs[ShowArtistVideoKey]           = false
                    prefs[ShowArtistBackgroundVideoKey] = false
                    prefs[AlbumCanvasEnabledKey]        = false
                    prefs[DataSaverKey]                 = true
                } else {
                    // Restore user's original values from backup
                    prefs[CanvasThumbnailAnimationKey]  = prefs[DataSaverBackupCanvasKey] ?: true
                    prefs[ShowArtistVideoKey]           = prefs[DataSaverBackupArtistVideoKey] ?: true
                    prefs[ShowArtistBackgroundVideoKey] = prefs[DataSaverBackupArtistBgVideoKey] ?: true
                    prefs[AlbumCanvasEnabledKey]        = prefs[DataSaverBackupAlbumCanvasKey] ?: false
                    prefs[DataSaverKey]                 = false
                }
            }
        }
    }

    // Animated card color based on Data Saver state
    val containerColor by animateColorAsState(
        targetValue = if (dataSaver) {
            MaterialTheme.colorScheme.primaryContainer
        } else {
            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        },
        label = "dataSaverContainerColor"
    )

    val contentColor = if (dataSaver) {
        MaterialTheme.colorScheme.onPrimaryContainer
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }

    Column(
        Modifier
            .windowInsetsPadding(LocalPlayerAwareWindowInsets.current)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
    ) {
        // Description
        Text(
            text = stringResource(R.string.data_saver_desc),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 24.dp, top = 16.dp)
        )

        // Main toggle capsule card
        Card(
            onClick = { toggleDataSaver(!dataSaver) },
            shape = RoundedCornerShape(50),
            colors = CardDefaults.cardColors(containerColor = containerColor),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.data_saver),
                    style = MaterialTheme.typography.titleMedium,
                    color = contentColor
                )
                ModernSwitch(
                    checked = dataSaver,
                    onCheckedChange = { toggleDataSaver(it) }
                )
            }
        }

        // Individual Toggles
        val (canvasThumbnailAnimation, onCanvasThumbnailAnimationChange) = rememberPreference(key = CanvasThumbnailAnimationKey, defaultValue = true)
        val (showArtistVideo, onShowArtistVideoChange) = rememberPreference(key = ShowArtistVideoKey, defaultValue = true)
        val (showArtistBackgroundVideo, onShowArtistBackgroundVideoChange) = rememberPreference(key = ShowArtistBackgroundVideoKey, defaultValue = true)
        val (albumCanvasEnabled, onAlbumCanvasEnabledChange) = rememberPreference(key = AlbumCanvasEnabledKey, defaultValue = false)

        com.david.frequency.ui.component.Material3SettingsGroup(
            title = stringResource(R.string.data_saver_turns_off_header),
            items = listOf(
                com.david.frequency.ui.component.Material3SettingsItem(
                    icon = painterResource(R.drawable.canvas_art),
                    title = { Text(stringResource(R.string.data_saver_player_canvas)) },
                    trailingContent = {
                        androidx.compose.material3.Switch(
                            checked = canvasThumbnailAnimation,
                            onCheckedChange = onCanvasThumbnailAnimationChange,
                            enabled = !dataSaver
                        )
                    },
                    onClick = { if (!dataSaver) onCanvasThumbnailAnimationChange(!canvasThumbnailAnimation) },
                    enabled = !dataSaver,
                    isExpressive = true
                ),
                com.david.frequency.ui.component.Material3SettingsItem(
                    icon = painterResource(R.drawable.slow_motion_video),
                    title = { Text(stringResource(R.string.data_saver_artist_video)) },
                    trailingContent = {
                        androidx.compose.material3.Switch(
                            checked = showArtistVideo,
                            onCheckedChange = onShowArtistVideoChange,
                            enabled = !dataSaver
                        )
                    },
                    onClick = { if (!dataSaver) onShowArtistVideoChange(!showArtistVideo) },
                    enabled = !dataSaver,
                    isExpressive = true
                ),
                com.david.frequency.ui.component.Material3SettingsItem(
                    icon = painterResource(R.drawable.slow_motion_video),
                    title = { Text(stringResource(R.string.data_saver_artist_bg_video)) },
                    trailingContent = {
                        androidx.compose.material3.Switch(
                            checked = showArtistBackgroundVideo,
                            onCheckedChange = onShowArtistBackgroundVideoChange,
                            enabled = !dataSaver
                        )
                    },
                    onClick = { if (!dataSaver) onShowArtistBackgroundVideoChange(!showArtistBackgroundVideo) },
                    enabled = !dataSaver,
                    isExpressive = true
                ),
                com.david.frequency.ui.component.Material3SettingsItem(
                    icon = painterResource(R.drawable.image),
                    title = { Text(stringResource(R.string.data_saver_album_canvas)) },
                    trailingContent = {
                        androidx.compose.material3.Switch(
                            checked = albumCanvasEnabled,
                            onCheckedChange = onAlbumCanvasEnabledChange,
                            enabled = !dataSaver
                        )
                    },
                    onClick = { if (!dataSaver) onAlbumCanvasEnabledChange(!albumCanvasEnabled) },
                    enabled = !dataSaver,
                    isExpressive = true
                )
            )
        )

        Spacer(modifier = Modifier.height(36.dp))
    }

    TopAppBar(
        title = { Text(stringResource(R.string.data_saver)) },
        navigationIcon = {
            IconButton(
                onClick = navController::navigateUp,
                onLongClick = navController::backToMain,
            ) {
                Icon(
                    painter = painterResource(R.drawable.arrow_back),
                    contentDescription = null,
                )
            }
        },
        scrollBehavior = scrollBehavior
    )
}

