package com.example.musicapp.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.widget.ContentLoadingProgressBar
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.musicapp.dataModel.Song
import com.example.musicapp.dataModel.categoryClass

@Composable
fun ListScreen(listScreenUiState: AppViewModel.ListScreenUiState,
               navHostController: NavHostController,
               appViewModel: AppViewModel
               ){

    Column(
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
            )
    ) {
        when (listScreenUiState) {
            is AppViewModel.ListScreenUiState.Error -> {
                ErrorScreen(appViewModel = appViewModel)
            }
            is AppViewModel.ListScreenUiState.Success->{
                LazyColumn(
                    contentPadding = PaddingValues(10.dp),
                ) {
                    item {
                        ShowCategory(category = listScreenUiState.category)
                    }
                    items(listScreenUiState.list){
                        SongCard(song = it, appViewModel = appViewModel, navHostController = navHostController)
                    }
                }
            }
            else -> {
                Loading()
            }
        }
    }

}

@Composable
fun ErrorScreen(appViewModel: AppViewModel){
    Column(
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
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 30.dp),
            horizontalArrangement = Arrangement.Absolute.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(text = ":(", fontSize = 100.sp, color = Color.White)
            Text(text = "Oops an error has occurred!!!", fontSize = 30.sp, color = Color.White)
        }
    }
}

@Composable
fun Loading(){
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
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        CircularProgressIndicator(color = Color.Green)
        Text(text = "Loading", color = Color.Green)
    }
}

@Composable
fun SongCard(song :Song,appViewModel: AppViewModel,navHostController: NavHostController){
    Box(modifier = Modifier.fillMaxWidth()
        .clickable {
            appViewModel.setPlayerUiState(song = song)
            navHostController.navigate(route = AppScreens.PlayerScreen.name)
        }
    ){
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            AsyncImage(model = song.songImageUrl,
                contentDescription = "",
                modifier = Modifier.size(50.dp)
                )
            Column(
                modifier = Modifier.padding(10.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = song.title, color = Color.Green, fontSize = 13.sp)
                Text(text = song.artist, color = Color.White, fontSize = 11.sp)
            }
        }
    }
}

@Composable
fun ShowCategory(category: categoryClass){
    Column (
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ){
        AsyncImage(model = category.imageUrl,
            contentDescription = "",
            modifier = Modifier.size(250.dp)
            )
        Text(text = category.name, color = Color.White, fontSize = 20.sp, fontFamily = FontFamily.Serif)
    }
}