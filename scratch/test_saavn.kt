
import com.music.jiosaavn.SaavnService
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    val songs = SaavnService.searchSongs("Never Gonna Give You Up").getOrThrow()
    val song = songs.first()
    println("Song ID: ${song.id}")
    
    val url320 = SaavnService.getBestStreamUrl(song.id, "320kbps")
    println("320kbps URL: $url320")
    
    val url160 = SaavnService.getBestStreamUrl(song.id, "160kbps")
    println("160kbps URL: $url160")
}

