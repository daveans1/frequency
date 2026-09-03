path = 'app/src/main/kotlin/com/david/frequency/playback/MusicService.kt'
with open(path, 'r', encoding='utf-8') as f:
    content = f.read()

content = content.replace('database.songDao().likedSongsByCreateDateAsc()', 'database.likedSongsByCreateDateAsc()')
content = content.replace('database.songDao().downloadedSongsByCreateDateAsc()', 'database.downloadedSongsByCreateDateAsc()')
content = content.replace('database.playlistDao().playlistSongs(playlistId)', 'database.playlistSongs(playlistId)')

with open(path, 'w', encoding='utf-8') as f:
    f.write(content)

path = 'app/src/main/kotlin/com/david/frequency/widget/MoodGridConfigActivity.kt'
with open(path, 'r', encoding='utf-8') as f:
    content = f.read()

content = content.replace('database.playlistDao().playlistsByNameAsc()', 'database.playlistsByNameAsc()')
content = content.replace('ViviTheme', 'vivimusicTheme')
content = content.replace('import com.david.frequency.ui.theme.ViviTheme', 'import com.david.frequency.ui.theme.vivimusicTheme\nimport androidx.compose.runtime.Composable')

with open(path, 'w', encoding='utf-8') as f:
    f.write(content)

path = 'app/src/main/kotlin/com/david/frequency/widget/RecognitionWidgetActivity.kt'
with open(path, 'r', encoding='utf-8') as f:
    content = f.read()

content = content.replace('permissions: Array<out String>', 'permissions: Array<String>')
content = content.replace('status.song.title', 'status.result.title')
content = content.replace('status.song.artist', 'status.result.artist')
content = content.replace('status.song.thumbnailUrl', 'status.result.coverArtHqUrl')

with open(path, 'w', encoding='utf-8') as f:
    f.write(content)
