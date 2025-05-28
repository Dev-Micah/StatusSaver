package com.micahnyabuto.statussaver

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.micahnyabuto.statussaver.ui.components.RequestStoragePermissions
import com.micahnyabuto.statussaver.ui.navigation.AppNavHost
import com.micahnyabuto.statussaver.ui.navigation.BottomNavigation
import com.micahnyabuto.statussaver.ui.navigation.Destinations
import com.micahnyabuto.statussaver.ui.screens.settings.SettingsViewModel
import com.micahnyabuto.statussaver.ui.screens.viewmodel.StatusViewModel
import com.micahnyabuto.statussaver.ui.theme.StatusSaverTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val viewModel: StatusViewModel = hiltViewModel()
            val settingsViewModel: SettingsViewModel = hiltViewModel()
            val isDarkMode by settingsViewModel.isDarkMode.collectAsState()
            val needsFolderPermission by viewModel.needsFolderPermission.collectAsState()
            val scope = rememberCoroutineScope()

            val context = LocalContext.current
            val activity = context as Activity

            StatusSaverTheme(darkTheme = isDarkMode) {
                var permissionsGranted by remember { mutableStateOf(false) }

                RequestStoragePermissions { permissionsGranted = true }

                if (permissionsGranted) {
                    val navController = rememberNavController()
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = navBackStackEntry?.destination?.route ?: Destinations.Home::class.qualifiedName.orEmpty()

                    val showBottomNavigation = currentRoute !in listOf(
                        Destinations.Splash::class.qualifiedName,
                    )

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
                                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                                    ) {
                                        BottomNavigation.entries.forEachIndexed { index, navigationItem ->
                                            val isSelected by remember(currentRoute) {
                                                derivedStateOf { currentRoute == navigationItem.route::class.qualifiedName }
                                            }

                                            NavigationBarItem(
                                                selected = isSelected,
                                                icon = {
                                                    Column(
                                                        horizontalAlignment = Alignment.CenterHorizontally,
                                                        verticalArrangement = Arrangement.Center,
                                                        modifier = Modifier.padding(vertical = 8.dp)
                                                    ) {
                                                        Icon(
                                                            modifier = Modifier.size(24.dp)

                                                            ,
                                                            imageVector = (
                                                                    if (isSelected) navigationItem.selectedIcon
                                                                    else navigationItem.unselectedIcon
                                                                    ),
                                                            contentDescription = navigationItem.label
                                                        )
                                                        Spacer(modifier = Modifier.height(4.dp))
                                                        Text(
                                                            text = navigationItem.label,
                                                            style = MaterialTheme.typography.labelSmall.copy(
                                                                fontSize = 10.sp,
                                                                fontWeight = if (isSelected) FontWeight.SemiBold
                                                                else FontWeight.Normal
                                                            )
                                                        )
                                                    }
                                                },
                                                onClick = {
                                                    navController.navigate(navigationItem.route)
                                                },
                                                colors = NavigationBarItemDefaults.colors(
                                                    indicatorColor = MaterialTheme.colorScheme.surfaceColorAtElevation(
                                                        elevation = 0.dp
                                                    ),
                                                    selectedIconColor = MaterialTheme.colorScheme.primary,
                                                    selectedTextColor = MaterialTheme.colorScheme.primary,
                                                    unselectedIconColor = MaterialTheme.colorScheme.onSurface,
                                                    unselectedTextColor = MaterialTheme.colorScheme.onSurface
                                                )
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    ) { innerPadding ->
                        AppNavHost(
                            navController = navController,
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                }
            }
        }
    }
}