package com.example.musicapp.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.musicapp.dataModel.categories
import com.example.musicapp.dataModel.categoryClass
import org.checkerframework.checker.units.qual.C
import java.util.Locale.Category

@Composable
fun MainScreen(
    navHostController: NavHostController,
    appViewModel: AppViewModel
){

    LazyVerticalGrid(columns = GridCells.Adaptive(minSize = 150.dp),
        contentPadding = PaddingValues(10.dp),
        modifier = Modifier.fillMaxSize()
            .background(
            brush = Brush.horizontalGradient(
                colors = listOf(
                    Color(0xFF000000),
                    Color(0xffc89116)
                ),
                startX = 0f,
                endX = 1500f
            )),
        ) {
        items(categories.categoryList()){
            CategoryCard(category = it, appViewModel = appViewModel, navHostController = navHostController)
        }
    }

}

@Composable
fun CategoryCard(category:categoryClass,
                 appViewModel: AppViewModel,
                 navHostController: NavHostController
                 ){
    Box(
        modifier = Modifier.size(210.dp)
            .clickable {
                appViewModel.readCategorySongFromDatabase(category=category)
                navHostController.navigate(route = AppScreens.ListScreen.name)
            }
            .padding(10.dp),
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(model = category.imageUrl, contentDescription = "")
            Text(text = category.name,
                textAlign = TextAlign.Center,
                color = Color.White,
                fontWeight = FontWeight.SemiBold
                )
        }
    }
}