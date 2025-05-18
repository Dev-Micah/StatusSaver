package com.micahnyabuto.statussaver.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.micahnyabuto.statussaver.ui.navigation.Destinations
import com.micahnyabuto.statussaver.ui.theme.PrimaryLightColor
import com.micahnyabuto.statussaver.ui.theme.SecondaryColor

@Composable
fun HomeScreen(
    navController: NavController
){
    TopBar(
        navController = navController
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenContent(

){

}
@Composable
fun CategoriesBar(
    navController: NavController
) {
    Row(
        modifier = Modifier
            .height(50.dp)
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextButton(onClick = {navController.navigate(Destinations.Home)},
            shape = RoundedCornerShape(2.dp)
        ) {
            Text("IMAGES",
                style = MaterialTheme.typography.bodyLarge.copy(
                    Color.White
                ))
        }
        TextButton(onClick = {navController.navigate(Destinations.Videos)},
            shape = RoundedCornerShape(2.dp)) {
            Text("VIDEOS",
                style = MaterialTheme.typography.bodyLarge.copy(
                    Color.White
                ))
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    navController: NavController
){
    Scaffold (

        topBar = {
            Column (
                modifier = Modifier.background(MaterialTheme.colorScheme.primary)


            ){
                TopAppBar(
                    title = { Text("Status Saver") },

                    actions = {
                        IconButton(
                            onClick = {}
                        ) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Notifications"
                            )
                        }
                        IconButton(onClick = {}) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "Share App"
                            )
                        }
                    },
                    colors = TopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = White,
                        navigationIconContentColor = White,
                        actionIconContentColor = White,
                        scrolledContainerColor = White
                    )
                )
                CategoriesBar(
                    navController = navController
                )
            }

        }
    ){ innerpadding ->



    }
}
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview(){
    HomeScreen(
        navController = rememberNavController()
    )
}