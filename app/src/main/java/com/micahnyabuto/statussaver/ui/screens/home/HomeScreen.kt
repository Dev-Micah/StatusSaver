package com.micahnyabuto.statussaver.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.micahnyabuto.statussaver.data.local.StatusEntity
import com.micahnyabuto.statussaver.ui.navigation.Destinations
import com.micahnyabuto.statussaver.ui.screens.viewmodel.StatusViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: StatusViewModel =hiltViewModel()
){
    val statuses by viewModel.statuses.collectAsState()
    Scaffold(
        topBar = {
            Column(
                modifier = Modifier.background(MaterialTheme.colorScheme.primary)
            ) {
                TopAppBar(
                    title = { Text("Status Saver -Images") },
                    actions = {
                        IconButton(onClick = {}) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "Share App"
                            )
                        }
                    },
                    colors = TopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = Color.White,
                        navigationIconContentColor = Color.White,
                        actionIconContentColor = Color.White,
                        scrolledContainerColor = Color.White
                    )
                )
                CategoriesBar(navController = navController)
            }
        }
    ) { innerpadding ->
        LaunchedEffect(Unit) {
            viewModel.loadStatusesFromStorage()
        }


        if (statuses.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(8.dp))
                Text("Loading...")
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerpadding),

                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(statuses) { status ->
                    StatusItem(
                        status = status,
                        downloadProgress = viewModel.downloadProgress.collectAsState().value[status.filePath],
                        onDownloadClick = {
                            viewModel.saveStatus(status)
                        })
                }
            }
        }

    }
}

@Composable
fun CategoriesBar(
    navController: NavController
) {
    Row(
        modifier = Modifier
            .height(50.dp)
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextButton(onClick = {navController.navigate(Destinations.Home.route)},
            shape = RoundedCornerShape(2.dp)
        ) {
            Text("IMAGES",
                style = MaterialTheme.typography.bodyLarge.copy(
                    Color.White
                ))
        }
        TextButton(onClick = {navController.navigate(Destinations.Videos.route)},
            shape = RoundedCornerShape(2.dp)) {
            Text("VIDEOS",
                style = MaterialTheme.typography.bodyLarge.copy(
                    Color.White
                ))
        }
    }
}

@Composable
fun StatusItem(
    status: StatusEntity,
    downloadProgress: Float?,
    onDownloadClick: () -> Unit
) {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp)
    ) {
        Column {
            if (status.fileType == "image") {
                Image(
                    painter = rememberAsyncImagePainter(status.filePath),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .width(20.dp)
                        .height(200.dp),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center)
                {
                    Text("Failed to load")

                }

            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                if (downloadProgress != null) {
                    LinearProgressIndicator(
                        progress = downloadProgress,
                        modifier = Modifier.fillMaxWidth()
                    )
                } else {
                    IconButton(
                        onClick = onDownloadClick,
                        modifier = Modifier.align(Alignment.CenterEnd)
                    ) {
                        Icon(Icons.Default.Download, contentDescription = "Download")
                    }
                }
            }
        }
    }
}

