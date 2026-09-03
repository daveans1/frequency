path = 'app/src/main/kotlin/com/david/frequency/playback/MusicService.kt'
with open(path, 'r', encoding='utf-8') as f:
    content = f.read()

content = content.replace('kotlinx.coroutines.flow.first(database.likedSongsByCreateDateAsc())', 'database.likedSongsByCreateDateAsc().first()')
content = content.replace('kotlinx.coroutines.flow.first(database.downloadedSongsByCreateDateAsc())', 'database.downloadedSongsByCreateDateAsc().first()')
content = content.replace('kotlinx.coroutines.flow.first(database.playlistSongs(playlistId))', 'database.playlistSongs(playlistId).first()')

with open(path, 'w', encoding='utf-8') as f:
    f.write(content)

path = 'app/src/main/kotlin/com/david/frequency/widget/MoodGridConfigActivity.kt'
with open(path, 'r', encoding='utf-8') as f:
    content = f.read()

content = content.replace('database.playlistsByNameAsc().first()', 'database.playlistsByNameAsc().first().map { it.playlist }')
if 'import kotlinx.coroutines.flow.first' not in content:
    content = content.replace('import kotlinx.coroutines.launch', 'import kotlinx.coroutines.flow.first\nimport kotlinx.coroutines.launch')

with open(path, 'w', encoding='utf-8') as f:
    f.write(content)
