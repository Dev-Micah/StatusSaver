package com.micahnyabuto.statussaver.ui.screens.videos

import android.net.Uri
import android.widget.VideoView
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
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
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.micahnyabuto.statussaver.data.local.StatusEntity
import com.micahnyabuto.statussaver.ui.screens.home.CategoriesBar
import com.micahnyabuto.statussaver.ui.screens.viewmodel.StatusViewModel
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.PlayerView

import androidx.core.net.toUri
import com.micahnyabuto.statussaver.ui.navigation.Destinations
import java.net.URLEncoder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoScreen(
    navController: NavController,
    viewModel: StatusViewModel = hiltViewModel()
) {
    val videoStatuses by viewModel.videoStatuses.collectAsState()


    Scaffold(
        topBar = {
            Column(
                modifier = Modifier.background(MaterialTheme.colorScheme.primary)
            ) {
                TopAppBar(
                    title = { Text("Status Saver - Videos") },
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
    ) { innerPadding ->
        LaunchedEffect(Unit) {
            viewModel.loadStatusesFromStorage()
        }

        if (videoStatuses.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(8.dp))
                Text("Loading...")
            }
        } else {
            val videos = videoStatuses.filter { it.fileType == "video" && it.filePath.isNotEmpty() }
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(videos) { status ->
                    VideoStatusItem(
                        status = status,
                        downloadProgress = viewModel.downloadProgress.collectAsState().value[status.filePath],
                        onDownloadClick = {
                            viewModel.saveStatus(status)
                        }
                    ){
                        val encodedPath = URLEncoder.encode(status.filePath, "UTF-8")
                        navController.navigate(Destinations.Details.detailsRoute(encodedPath))
                    }
                }
            }
        }
    }
}

@Composable
fun VideoStatusItem(
    status: StatusEntity,
    downloadProgress: Float?,
    onDownloadClick: () -> Unit,
    onItemClick: () -> Unit
) {
    val context = LocalContext.current
    // Create and remember an ExoPlayer instance for each video item
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(status.filePath))
            repeatMode = ExoPlayer.REPEAT_MODE_ALL
            prepare()
            //playWhenReady = false
        }
    }

    // Release ExoPlayer when composable leaves composition
    DisposableEffect(key1 = status.filePath) {
        onDispose {
            exoPlayer.release()
        }
    }
    Card (
        modifier = Modifier.clickable{
            onItemClick()
        }
    ){

    Column {
        AndroidView(
            factory = { ctx ->
                PlayerView(ctx).apply {
                    player = exoPlayer
                    useController = false // Hide controls if you want
                    layoutParams = android.view.ViewGroup.LayoutParams(
                        android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                        android.view.ViewGroup.LayoutParams.MATCH_PARENT
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .width(150.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
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