package com.example.musicapp.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

enum class AppScreens{
    MainScreen,
    AddSongScreen,
    ListScreen,
    PlayerScreen,
}

val auth = Firebase.auth

@Composable
fun MusicApp(
    appViewModel: AppViewModel = viewModel(),
    navController : NavHostController = rememberNavController()

){
    val splashScreenVisibility by appViewModel.splashScreenVisiblity.collectAsState()
    val currentUser = auth.currentUser
    currentUser?.let { appViewModel.setUser(it) }

    val user by appViewModel.user.collectAsState()

    if (splashScreenVisibility){
       SplashScreen()
    }
    else if (user==null){
        VerificationScreen(appViewModel = appViewModel)
    }
    else{
        Scaffold(
            topBar = {
                Row(
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
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (user==null) {
                        Icon(imageVector = Icons.Filled.AccountCircle,
                            contentDescription = "",
                            modifier = Modifier
                                .padding(20.dp)
                                .size(40.dp),

                            tint = Color.White
                            )
                    }else{
                        Card(
                            modifier = Modifier
                                .padding(20.dp)
                                .size(40.dp),
                            shape = RoundedCornerShape(50.dp)
                        ) {
                            AsyncImage(
                                model = auth.currentUser?.photoUrl,
                                modifier = Modifier
                                    .size(40.dp),
                                contentDescription = ""
                            )
                        }
                    }
                    Menu(navController = navController, appViewModel = appViewModel)

//                    Icon(imageVector = Icons.Filled.ExitToApp,
//                        modifier = Modifier
//                            .padding(20.dp)
//                            .size(40.dp)
//                            .clickable {
//                                appViewModel.setUser(null)
//                                auth.signOut()
//                            },
//                        tint = Color.White,
//                        contentDescription = "Log Out")
                }
            }
        ) {
            NavHost(navController = navController, startDestination = AppScreens.MainScreen.name, modifier = Modifier.padding(it)) {
                composable(route = AppScreens.MainScreen.name){
                    MainScreen(navHostController = navController, appViewModel = appViewModel)
                }
                composable(route = AppScreens.AddSongScreen.name){
                    AddSongScreen(appViewModel = appViewModel)
                }
                composable(route = AppScreens.ListScreen.name){
                    ListScreen(listScreenUiState = appViewModel.listScreenUiState, navHostController = navController, appViewModel = appViewModel)
                }
                composable(route = AppScreens.PlayerScreen.name){
                    PlayerScreen(playerScreenUiState = appViewModel.playerScreenUiState)
                }
            }
        }
    }
}


@Composable
fun Menu(
    navController: NavHostController,
    appViewModel: AppViewModel
){

    var visibility by remember {
        mutableStateOf(false)
    }

    if (!visibility) {
        Icon(
            imageVector = Icons.Filled.Menu,
            tint = Color.White,
            modifier = Modifier
                .padding(end = 10.dp)
                .clickable { visibility = true }
                .size(40.dp),
            contentDescription = "")
    }else
    {
        Icon(imageVector = Icons.Filled.Close,
            tint = Color.White,
            modifier = Modifier
                .padding(end = 10.dp)
                .clickable { visibility = false }
                .size(40.dp),
            contentDescription = "")
    }

    if (visibility) {

        Column(
            modifier = Modifier
                .background(Color.Transparent)
                .padding(top = 50.dp)
                .fillMaxSize(),
            horizontalAlignment = AbsoluteAlignment.Right
        ) {
            Column(
                modifier = Modifier
                    .width(200.dp)
                    .background(Color.DarkGray)
                    .padding(10.dp)
                    .fillMaxHeight()
            ) {
                Row (
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .padding(5.dp)
                            .clickable {
                                navController.navigate(route = AppScreens.AddSongScreen.name)
                                visibility = false
                            }
                    ){
                        Text(text = "Add Song",
                            textAlign = TextAlign.Center,
                            color = Color.White,
                            fontSize = 18.sp
                            )
                    }
                
                LogOutAlert(appViewModel = appViewModel)
                
            }
        }
   }


}

@Composable
fun LogOutAlert(
    appViewModel: AppViewModel
){
    var visibility by remember {
        mutableStateOf(false)
    }
    Row(
        modifier = Modifier.clickable { visibility = true },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(text = "Log Out",
            color = Color.White,
            textAlign = TextAlign.Center,
            fontSize = 18.sp)
    }

    if (visibility) {
        AlertDialog(onDismissRequest = { visibility = false },
            title = {
                Text(text = "Log Out?")
            },
            text = {
                Text(text = "Are you sure want to logout?")
            },
            dismissButton = {
                Button(onClick = { visibility = false }) {
                    Text(text = "Cancel")
                }
            },
            confirmButton = {
            Button(onClick = { appViewModel.setUser(null)
                auth.signOut() }) {
                Text(text = "Confirm")
            }
        })
    }


}