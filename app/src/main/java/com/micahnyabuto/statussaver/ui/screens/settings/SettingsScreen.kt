package com.micahnyabuto.statussaver.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController
){
    Scaffold (
        topBar = {
            Column(
                modifier = Modifier.background(MaterialTheme.colorScheme.primary)
            ) {
                TopAppBar(

                    title = { Text("Status Saver") },

                    actions = {
                        IconButton(
                            onClick = {}
                        ) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Notifications"
                            )
                        }
                        IconButton(onClick = {}) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "Share App"
                            )
                        }
                    },
                    colors = TopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = White,
                        navigationIconContentColor = White,
                        actionIconContentColor = White,
                        scrolledContainerColor = White
                    )
                )
            }

        }
    ){ innerpadding ->
        SettingsScreenContent(
            navController,
            viewModel = viewModel()
        )

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreenContent(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = hiltViewModel()
){
    val isDarkMode by viewModel.isDarkMode.collectAsState()
    Scaffold (
        topBar = {
            TopAppBar(
                title = {
                    Text("Settings",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        ) )
                }
            )
        },


        ) { PaddingValues ->
        Column(
            modifier = Modifier
                .padding(PaddingValues)
                .padding(8.dp)
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Enable DarkTheme")

                Switch(
                    checked = isDarkMode,
                    onCheckedChange = { viewModel.toggleTheme(it)}
                )


            }
            Spacer(Modifier.height(12.dp))
            HorizontalDivider()
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Allow Notifications")

                Switch(
                    checked = false,
                    onCheckedChange = {  }
                )

            }


        }

    }

}