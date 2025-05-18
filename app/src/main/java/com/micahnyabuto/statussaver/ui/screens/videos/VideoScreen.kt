package com.micahnyabuto.statussaver.ui.screens.videos

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.micahnyabuto.statussaver.ui.screens.home.CategoriesBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoScreen(
    navController: NavController
){
    Scaffold (
        topBar = {
            Column(
                modifier = Modifier.background(MaterialTheme.colorScheme.primary)
            ) {
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
        Box(modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center){
            Text("VideoScreen")
        }
    }
}
@Preview(showBackground = true)
@Composable
fun VideosScreenPreview(){
    VideoScreen(
        navController = rememberNavController()
    )
}