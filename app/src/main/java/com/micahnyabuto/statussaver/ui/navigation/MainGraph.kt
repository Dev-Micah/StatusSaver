package com.micahnyabuto.statussaver.ui.navigation

import android.annotation.SuppressLint
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.micahnyabuto.statussaver.ui.components.RequestStoragePermissions
import com.micahnyabuto.statussaver.ui.screens.home.HomeScreen
import com.micahnyabuto.statussaver.ui.screens.saved.SavedScreen
import com.micahnyabuto.statussaver.ui.screens.settings.SettingsScreen
import com.micahnyabuto.statussaver.ui.screens.settings.SettingsViewModel
import com.micahnyabuto.statussaver.ui.screens.videos.VideoPlayerScreen
import com.micahnyabuto.statussaver.ui.screens.videos.VideoScreen
import com.micahnyabuto.statussaver.ui.screens.viewmodel.StatusViewModel
import com.micahnyabuto.statussaver.ui.theme.StatusSaverTheme
import kotlinx.coroutines.launch
import java.net.URLDecoder

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainNavGraph(){
    val navController= rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route.orEmpty()

    val showBottomNavigation = currentRoute !in listOf(
        Destinations.Splash.route,
    )
    val viewModel: StatusViewModel = hiltViewModel()
    val settingsViewModel: SettingsViewModel = hiltViewModel()
    val isDarkMode by settingsViewModel.isDarkMode.collectAsState()
    val needsFolderPermission by viewModel.needsFolderPermission.collectAsState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current


    StatusSaverTheme(darkTheme = isDarkMode) {
        var permissionsGranted by remember { mutableStateOf(false) }

        RequestStoragePermissions { permissionsGranted = true }

        if (permissionsGranted) {

            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.OpenDocumentTree()
            ) { uri: Uri? ->
                uri?.let {
                    scope.launch {
                        viewModel.saveSafUri(it)
                    }
                }
            }

            LaunchedEffect(needsFolderPermission) {
                if (needsFolderPermission) {
                    launcher.launch(null)
                }
            }
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                contentWindowInsets = WindowInsets.navigationBars.only(WindowInsetsSides.Horizontal),
                bottomBar = {
                    if (showBottomNavigation) {
                        Column {
                            HorizontalDivider(thickness = 1.dp)
                            NavigationBar(
                                tonalElevation = 0.dp,
                                containerColor = MaterialTheme.colorScheme.surface
                            ) {
                                BottomNavigation.entries.forEach { navigationItem ->

                                    val isSelected = currentRoute == navigationItem.route

                                    NavigationBarItem(
                                        selected = isSelected,
                                        icon = {
                                            Icon(
                                                imageVector = if (isSelected) navigationItem.selectedIcon else navigationItem.unselectedIcon,
                                                contentDescription = navigationItem.label
                                            )
                                        },
                                        label = {
                                            Text(
                                                text = navigationItem.label,
                                                style = MaterialTheme.typography.labelSmall.copy(
                                                    fontSize = 10.sp,
                                                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                                                )
                                            )
                                        },
                                        onClick = {
                                            if (currentRoute != navigationItem.route) {
                                                navController.navigate(navigationItem.route)
                                            }
                                        },
                                        colors = NavigationBarItemDefaults.colors(
                                            indicatorColor = MaterialTheme.colorScheme.surfaceColorAtElevation(
                                                elevation = 0.dp
                                            ),
                                            selectedIconColor = MaterialTheme.colorScheme.primary,
                                            selectedTextColor = MaterialTheme.colorScheme.primary,
                                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    )
                                }
                            }

                        }
                    }
                }
            ) {
                NavHost(
                    startDestination = Destinations.Home.route,
                    navController = navController
                ) {

                    composable(Destinations.Home.route) {
                        HomeScreen(
                            navController = navController
                        )
                    }
                    composable(Destinations.Saved.route) {
                        SavedScreen()
                    }
                    composable(Destinations.Settings.route) {
                        SettingsScreen(
                            navController = navController
                        )
                    }
                    composable(Destinations.Videos.route) {
                        VideoScreen(
                            navController = navController
                        )
                    }
                    // In your NavHost setup, add a composable route for VideoPlayerScreen
                    composable(
                        route= Destinations.Details.route,
                        arguments = listOf(navArgument("statusPath") { type = NavType.StringType })
                    ) { backStackEntry ->
                        // Decode the path!
                        val encodedPath = backStackEntry.arguments?.getString("statusPath") ?: ""
                        val statusPath = URLDecoder.decode(encodedPath, "UTF-8")
                        val statuses by viewModel.statuses.collectAsState()

                        LaunchedEffect(Unit) {
                            viewModel.loadStatusesFromStorage()
                        }

                        // Find by filePath, not fileType
                        val status = statuses.find { it.filePath == statusPath }
                        if (status != null) {
                            VideoPlayerScreen(
                                statusPath = statusPath, // or status.filePath
                                onBack = { navController.popBackStack() },
                                onDownloadClick = { /* implement download logic */ },
                                onShareClick = { /* implement share logic */ }
                            )
                        } else {
                            Box (
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ){
                                Text("Couldn't play video")
                            }
                        }
                    }
                }
            }
        }
    }
}