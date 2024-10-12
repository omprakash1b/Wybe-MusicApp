package com.example.musicapp.ui.theme

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.musicapp.dataModel.Song
import com.google.firebase.Firebase
import com.google.firebase.storage.storage

@Composable
fun AddSongScreen(
    appViewModel: AppViewModel
){
    var title by remember {
        mutableStateOf("")
    }
    var artist by remember {
        mutableStateOf("")
    }
//    var artists by remember {
//        mutableStateOf(emptyList<String>())
//    }
    var subtitle by remember {
        mutableStateOf("")
    }
    var songUrl by remember {
        mutableStateOf("")
    }
    var imageUrl by remember {
        mutableStateOf("")
    }

    val context = LocalContext.current

    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black),
        verticalArrangement = Arrangement.spacedBy(5.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        TextField(value = title, onValueChange = { title = it},
            placeholder = {
                Text(text = "Title")
            }
            )

        TextField(value = artist , onValueChange = { artist=it},
            placeholder = {
                Text(text = "Artist")
            }
            )

        TextField(value = subtitle, onValueChange ={subtitle = it} ,
            placeholder = {
                Text(text = "Subtitle")
            }
            )

        TextField(value = songUrl, onValueChange = {songUrl = it},
            placeholder = {
                Text(text = "Song Url")
            }
            )

        TextField(value = imageUrl, onValueChange = {imageUrl = it},
            placeholder = {
                Text(text = "Image Url")
            }
            )

        var music by remember {
            mutableStateOf<Uri?>(null)
        }
        val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) {
            music = it
        }

        songUrl = Firebase.storage.reference.child("songs")
            .child("Tere Bina").downloadUrl.toString()

       Button(onClick = {
           launcher.launch("audio/*")
       }) {
          Text(text = "Upload Song")
       }
    
        Button(onClick = { 
            if (music != null) {
            songUrl = appViewModel.uploadSongToStorage(uri = music, context = context, title = title)
            }
        }) {
            Text(text = "Upload")
        }

        Button(onClick = {
            appViewModel.addSongToDatabase(Song(
                title = title,
                artist = artist,
                subtitle = subtitle,
                songUrl = songUrl,
                songImageUrl = imageUrl
            ))

            

        }) {
            Text(text = "Add Song")
        }
    }
}
