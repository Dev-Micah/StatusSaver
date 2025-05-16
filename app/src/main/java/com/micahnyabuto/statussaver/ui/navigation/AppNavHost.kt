package com.micahnyabuto.statussaver.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.micahnyabuto.statussaver.ui.screens.home.HomeScreen
import com.micahnyabuto.statussaver.ui.screens.saved.SavedScreen
import com.micahnyabuto.statussaver.ui.screens.settings.SettingsScreen

@Composable
fun AppNavHost(
    modifier: Modifier= Modifier
){
    val navController= rememberNavController()
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Destinations.Home
    ) {
        composable <Destinations.Home>{
            HomeScreen()
        }
        composable <Destinations.Saved>{
            SavedScreen()
        }
        composable <Destinations.Settings>{
            SettingsScreen()
        }
    }
}