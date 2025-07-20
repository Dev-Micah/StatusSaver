package com.micahnyabuto.statussaver.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.micahnyabuto.statussaver.ui.screens.splash.SplashScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier= Modifier
){
    //val navController = rememberNavController()
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Destinations.Splash
    ) {
        composable <Destinations.Splash>{
            SplashScreen(navController = navController)
        }
        composable <Destinations.Main>{
            MainNavGraph()
        }

    }
}