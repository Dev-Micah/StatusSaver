package com.micahnyabuto.statussaver.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.micahnyabuto.statussaver.ui.screens.splash.SplashScreen

@Composable
fun AppNavHost(
    modifier: Modifier= Modifier
){
    val navController = rememberNavController()
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Destinations.Splash.route
    ) {
        composable (Destinations.Splash.route){
            SplashScreen(navController = navController)
        }
        composable (Destinations.Main.route){
            MainNavGraph()
        }

    }
}