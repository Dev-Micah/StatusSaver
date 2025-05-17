package com.micahnyabuto.statussaver.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.micahnyabuto.statussaver.ui.screens.home.HomeScreen
import com.micahnyabuto.statussaver.ui.screens.saved.SavedScreen
import com.micahnyabuto.statussaver.ui.screens.settings.SettingsScreen
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
            SplashScreen(
                onSplashFinished = {
                    navController.navigate(Destinations.Home){
                        popUpTo(Destinations.Splash){
                            inclusive =true
                        }
                    }
                }
            )
        }
        composable <Destinations.Home>{
            HomeScreen(
                navController =navController
            )
        }
        composable <Destinations.Saved>{
            SavedScreen(
                navController =navController
            )
        }
        composable <Destinations.Settings>{
            SettingsScreen(
                navController =navController
            )
        }
    }
}