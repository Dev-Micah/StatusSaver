package com.micahnyabuto.statussaver.ui.screens.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.micahnyabuto.statussaver.R
import com.micahnyabuto.statussaver.ui.navigation.Destinations
import com.micahnyabuto.statussaver.ui.theme.SecondaryColor
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController: NavController
){
    LaunchedEffect(Unit) {
        delay(2000)
        navController.navigate(Destinations.Main){
            popUpTo(Destinations.Splash){
                inclusive =true
            }
        }
    }
    Box(
        modifier = Modifier.fillMaxSize()
            .background(Color(0xFF075E54)),
        contentAlignment = Alignment.Center
    ){
        Image(
            painter = painterResource(id = R.drawable.savericon),
            contentDescription = "Icon",
            modifier = Modifier.size(120.dp)


        )
    }
    Box(
        modifier = Modifier.fillMaxSize()
            .padding(bottom = 100.dp),
        contentAlignment = Alignment.BottomCenter
    ){
        LinearProgressIndicator(
            modifier = Modifier.fillMaxWidth(0.5f)
                .height(4.dp),
            color = Color.White
        )
    }

}

