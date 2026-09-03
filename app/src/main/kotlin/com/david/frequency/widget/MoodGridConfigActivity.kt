package com.david.frequency.widget

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.david.frequency.R
import com.david.frequency.db.MusicDatabase
import com.david.frequency.db.entities.PlaylistEntity
import com.david.frequency.ui.theme.vivimusicTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MoodGridConfigActivity : ComponentActivity() {

    @Inject
    lateinit var database: MusicDatabase

    private var appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Set the result to CANCELED. This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED)
        
        val intent = intent
        val extras = intent.extras
        if (extras != null) {
            appWidgetId = extras.getInt(
                AppWidgetManager.EXTRA_APPWIDGET_ID, 
                AppWidgetManager.INVALID_APPWIDGET_ID
            )
        }
        
        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish()
            return
        }

        setContent {
            vivimusicTheme {
                val scope = rememberCoroutineScope()
                var playlists by remember { mutableStateOf<List<PlaylistEntity>>(emptyList()) }
                
                LaunchedEffect(Unit) {
                    // Get all playlists
                    val allPlaylists = database.playlistsByNameAsc().first().map { it.playlist }
                    // Add system playlists manually
                    val liked = PlaylistEntity(id = PlaylistEntity.LIKED_PLAYLIST_ID, name = "Liked Songs")
                    val downloaded = PlaylistEntity(id = PlaylistEntity.DOWNLOADED_PLAYLIST_ID, name = "Downloads")
                    
                    val combined = listOf(liked, downloaded) + allPlaylists
                    playlists = combined
                }
                
                var selectedTiles by remember { mutableStateOf(mutableMapOf<Int, PlaylistEntity>()) }
                var configuringTileIndex by remember { mutableStateOf<Int?>(null) }
                
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("Configure Mood Grid") },
                            actions = {
                                TextButton(
                                    onClick = {
                                        saveConfigurationAndFinish(selectedTiles)
                                    },
                                    enabled = selectedTiles.size == 4
                                ) {
                                    Text("Save")
                                }
                            }
                        )
                    }
                ) { padding ->
                    if (configuringTileIndex == null) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(padding)
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("Select a playlist for each tile:")
                            Spacer(modifier = Modifier.height(24.dp))
                            
                            // 2x2 grid representation for config
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                                TileConfigButton(
                                    index = 0,
                                    playlist = selectedTiles[0],
                                    onClick = { configuringTileIndex = 0 }
                                )
                                TileConfigButton(
                                    index = 1,
                                    playlist = selectedTiles[1],
                                    onClick = { configuringTileIndex = 1 }
                                )
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                                TileConfigButton(
                                    index = 2,
                                    playlist = selectedTiles[2],
                                    onClick = { configuringTileIndex = 2 }
                                )
                                TileConfigButton(
                                    index = 3,
                                    playlist = selectedTiles[3],
                                    onClick = { configuringTileIndex = 3 }
                                )
                            }
                        }
                    } else {
                        // Playlist picker
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(padding)
                        ) {
                            items(playlists) { playlist ->
                                ListItem(
                                    headlineContent = { Text(playlist.name) },
                                    modifier = Modifier.clickable {
                                        val newMap = selectedTiles.toMutableMap()
                                        newMap[configuringTileIndex!!] = playlist
                                        selectedTiles = newMap
                                        configuringTileIndex = null
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
    
    @Composable
    fun TileConfigButton(index: Int, playlist: PlaylistEntity?, onClick: () -> Unit) {
        Card(
            modifier = Modifier
                .size(140.dp, 100.dp)
                .clickable(onClick = onClick),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = playlist?.name ?: "Tap to select\nMood ",
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }

    private fun saveConfigurationAndFinish(tiles: Map<Int, PlaylistEntity>) {
        val context = this@MoodGridConfigActivity
        
        // Save to SharedPreferences
        val prefs = context.getSharedPreferences("mood_grid_widget_", Context.MODE_PRIVATE)
        prefs.edit().apply {
            putString("tile_0_id", tiles[0]?.id)
            putString("tile_0_name", tiles[0]?.name)
            putString("tile_1_id", tiles[1]?.id)
            putString("tile_1_name", tiles[1]?.name)
            putString("tile_2_id", tiles[2]?.id)
            putString("tile_2_name", tiles[2]?.name)
            putString("tile_3_id", tiles[3]?.id)
            putString("tile_3_name", tiles[3]?.name)
            apply()
        }
        
        // Update the widget
        val appWidgetManager = AppWidgetManager.getInstance(context)
        MoodGridWidgetReceiver.updateAppWidget(context, appWidgetManager, appWidgetId)
        
        // Make sure we pass back the original appWidgetId
        val resultValue = Intent()
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        setResult(RESULT_OK, resultValue)
        finish()
    }
}
