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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.micahnyabuto.statussaver.ui.navigation.AppNavHost
import com.micahnyabuto.statussaver.ui.navigation.BottomNavigation
import com.micahnyabuto.statussaver.ui.navigation.Destinations
import com.micahnyabuto.statussaver.ui.screens.settings.SettingsViewModel
import com.micahnyabuto.statussaver.ui.screens.viewmodel.StatusViewModel
import com.micahnyabuto.statussaver.ui.theme.StatusSaverTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: StatusViewModel = hiltViewModel()

            val context = LocalContext.current
            val activity = context as Activity

            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.OpenDocumentTree()
            ) { uri: Uri? ->
                uri?.let {
                    // Persist access permissions
                    context.contentResolver.takePersistableUriPermission(
                        it,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                    )
                    // Save URI to shared prefs or notify ViewModel
                    viewModel.saveSafUri(it)
                }
            }

             // Launch the picker somewhere in UI:
          Button(onClick = { launcher.launch(null) }) {
                Text("Pick WhatsApp Status Folder")
           }

            LaunchedEffect(viewModel.safUri.collectAsState().value) {
                if (viewModel.safUri.value != null) {
                    viewModel.loadStatusesFromStorage()
                }
            }
            val settingsViewModel : SettingsViewModel =hiltViewModel()
            val isDarkMode by settingsViewModel.isDarkMode.collectAsState()
            StatusSaverTheme (
                darkTheme = isDarkMode
            ){
                val navController =rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route ?: Destinations.Home::class.qualifiedName.orEmpty()

                // Don't Show bottom navigation only when on....
                val showBottomNavigation = currentRoute !in listOf(
                    Destinations.Splash::class.qualifiedName,

                )
                Scaffold (
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
                ){innerpadding ->
                    AppNavHost(
                        modifier = Modifier.padding(innerpadding),
                        navController =  navController
                    )

                }

                }
            }

        }
}


