package com.micahnyabuto.statussaver.ui.navigation

import kotlinx.serialization.Serializable

sealed class Destinations {
    @Serializable
    object Home

    @Serializable
    object Saved

    @Serializable
    object Settings

    @Serializable
    object Images

    @Serializable
    object Videos

    @Serializable
    object Splash

    @Serializable
    object Main
}