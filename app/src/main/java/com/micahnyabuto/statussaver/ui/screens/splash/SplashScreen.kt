package com.micahnyabuto.statussaver.ui.screens.splash

import android.window.SplashScreenView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.micahnyabuto.statussaver.R

@Composable
fun SplashScreen(){
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
            color = Color.White
        )
    }



}
