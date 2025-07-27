package com.micahnyabuto.statussaver.ui.navigation

sealed class Destinations(val route: String) {

    object Home: Destinations("home")


    object Saved: Destinations("saved")


    object Settings: Destinations("settings")

    object Images: Destinations("images")


    object Videos: Destinations("videos")


    object Splash: Destinations("splash")


    object Main: Destinations("main")
}