package com.micahnyabuto.statussaver.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

enum class BottomNavigation(
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val route: Any
) {
    Home(
        label = "Home",
        selectedIcon = Icons.Default.Home,
        unselectedIcon = Icons.Default.Home,
        route = Destinations.Home
    ),
    Saved(
        label = "Saved",
        selectedIcon = Icons.Default.Image,
        unselectedIcon = Icons.Default.Image,
        route = Destinations.Saved
    ),
    Settings(
        label = "Settings",
        selectedIcon = Icons.Default.Settings,
        unselectedIcon = Icons.Default.Settings,
        route = Destinations.Settings
    ),
}