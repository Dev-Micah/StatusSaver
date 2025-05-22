package com.micahnyabuto.statussaver.ui.screens.videos

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.micahnyabuto.statussaver.ui.components.PermissionHandler
import com.micahnyabuto.statussaver.ui.screens.home.StatusItem
import com.micahnyabuto.statussaver.ui.screens.home.TopBar
import com.micahnyabuto.statussaver.ui.screens.viewmodel.StatusViewModel

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

    LazyVerticalGrid(
        columns = GridCells.Fixed(2), // 2 columns
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
            items(videoStatuses) { status ->
                StatusItem(status = status, onDownloadClick = {
                    viewModel.saveStatus(status)
                })
            }
        }


}
