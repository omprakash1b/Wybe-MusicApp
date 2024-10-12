package com.example.musicapp.ui.theme

//import androidx.compose.ui.text.input.KeyboardType.Companion.Uri
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer

@Composable
fun VideoPlayer(videoUri: String) {
    val context = LocalContext.current

     //uri = Uri.parse("https://example.com/your_video.mp4")

    // Create ExoPlayer instance
    val exoPlayer = remember {
        ExoPlayer.Builder(context)
            .build().apply {
            setMediaItem(MediaItem.fromUri(Uri.parse(videoUri)))
            prepare()
            playWhenReady = true // Start playing automatically
        }
    }

    // Dispose ExoPlayer when the composable is removed from the composition
    DisposableEffect(
//        AndroidView(
//            factory = {
//                PlayerView(context).apply {
//                    player = exoPlayer
//                    useController = true // Show default playback controls
//                }
//            },
//            modifier = Modifier.fillMaxSize()
//        )
        Unit
    ) {
        onDispose {
            exoPlayer.release()
        }

    }




}

