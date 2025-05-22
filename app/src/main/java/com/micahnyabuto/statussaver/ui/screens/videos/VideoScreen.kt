package com.micahnyabuto.statussaver.ui.screens.videos

import StatusViewModel
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.micahnyabuto.statussaver.ui.components.PermissionHandler
import com.micahnyabuto.statussaver.ui.screens.home.StatusItem
import com.micahnyabuto.statussaver.ui.screens.home.TopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoScreen(
    viewModel: StatusViewModel = hiltViewModel(),
    navController: NavController
){
    val videoStatuses by viewModel.videoStatuses.collectAsState()
    TopBar(
        navController =navController
    )

    PermissionHandler {
        viewModel.loadStatusesFromStorage()
    }

        LazyColumn {
            items(videoStatuses) { status ->
                StatusItem(status = status, onDownloadClick = {
                    viewModel.saveStatus(status)
                })
            }
        }


}
