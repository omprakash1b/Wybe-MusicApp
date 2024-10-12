package com.example.musicapp.ui.theme

import android.content.Context
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import coil.compose.AsyncImage
import com.example.musicapp.R


@Composable
fun PlayerScreen(playerScreenUiState: AppViewModel.PlayerScreenUiState){
    when(playerScreenUiState){
        is AppViewModel.PlayerScreenUiState.Success->{
            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFF000000),
                                Color(0xffc89116)
                            ),
                            startX = 0f,
                            endX = 1500f
                        )
                    ),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            )
            {
                Card(
                    modifier = Modifier.padding(10.dp)
                ) {
                    AsyncImage(model = playerScreenUiState.song.songImageUrl,
                        contentDescription = "",
                        modifier = Modifier.fillMaxWidth()
                        )
                }
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = playerScreenUiState.song.title, fontSize = 20.sp, color = Color.Green)
                    Text(text = playerScreenUiState.song.artist, fontSize = 16.sp, color = Color.White)
                    Text(text = playerScreenUiState.song.subtitle, fontSize = 16.sp, color = Color.White)
                }
                val context = LocalContext.current
                Player(context = context, videoUri = playerScreenUiState.song.songUrl)
            }
        }
        else->{
            Loading()
        }
    }
}

@Composable
fun Player(context: Context,videoUri:String){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFF000000),
                        Color(0xffc89116)
                    ),
                    startX = 0f,
                    endX = 1500f
                )
            ),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
       // Text(text = "Hello This is the main Screen", color = Color.White)
        //val context = LocalContext.current
        //val videoUri = "https://firebasestorage.googleapis.com/v0/b/musicapp-8c145.appspot.com/o/songs%2FTheme%20of%20Kalki.mp3?alt=media&token=cf83f6ce-d6d1-464e-ab5a-adf68faa8045"
        val pause = remember {
            mutableStateOf(false)
        }

        var currentValue by remember {
            mutableFloatStateOf(0f)
        }


        Column(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),

            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //VideoPlayer(videoUri = "https://firebasestorage.googleapis.com/v0/b/musicapp-8c145.appspot.com/o/songs%2FTheme%20of%20Kalki.mp3?alt=media&token=cf83f6ce-d6d1-464e-ab5a-adf68faa8045")

            val exoPlayer = remember {
                ExoPlayer.Builder(context)
                    .build().apply {
                        setMediaItem(MediaItem.fromUri(Uri.parse(videoUri)))
                        prepare()
                        playWhenReady = true // Start playing automatically
                    }
            }

           DisposableEffect (

               Unit
           ){

                   onDispose {
                       exoPlayer.release()
                   }

               }


            val duration = exoPlayer.duration.toFloat()

            currentValue = exoPlayer.currentPosition.toFloat()/duration

            Slider(
                modifier = Modifier.weight(1f),
                value =  currentValue,
                onValueChange = {currentValue = it },
                onValueChangeFinished = {
                    // Seek to the new position when the user releases the slider
                    val targetPosition = (duration *currentValue).toLong()
                    exoPlayer.seekTo(targetPosition)
                },
                valueRange = 0f..100f
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                Image(
                    painter = painterResource(id = R.drawable.back),
                    contentDescription ="",
                    modifier = Modifier.size(40.dp)

                    )

                if (pause.value) {

                    Image(painter = painterResource(id = R.drawable.play),
                        contentDescription = "",
                        modifier = Modifier
                            .size(60.dp)
                            .clickable {
                                exoPlayer.play()
                                pause.value = false
                            }
                        )
                    
                }else{

                    Image(painter = painterResource(id = R.drawable.pause),
                        contentDescription = "",
                        modifier = Modifier
                            .size(60.dp)
                            .clickable {
                                exoPlayer.pause()
                                pause.value = true
                            }
                    )
                }


                Image(
                    painter = painterResource(id = R.drawable.forward),
                    contentDescription ="",
                    modifier = Modifier.size(40.dp)

                )


            }
        }

    }
}