package com.micahnyabuto.statussaver.ui.navigation

sealed class Destinations(val route: String) {

    object Home: Destinations("home")


    object Saved: Destinations("saved")


    object Settings: Destinations("settings")



    object Videos: Destinations("videos")

    object Details: Destinations("details/{statusPath}")


    object Splash: Destinations("splash")


    object Main: Destinations("main")

    fun detailsRoute(statusPath: String) = "details/${statusPath}"

}