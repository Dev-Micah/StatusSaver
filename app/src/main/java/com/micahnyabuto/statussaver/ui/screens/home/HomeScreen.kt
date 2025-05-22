package com.micahnyabuto.statussaver.ui.screens.home

<<<<<<< HEAD

=======
>>>>>>> 9bb36235ce0dee77733dcc94a50b483f6d73c33f
import android.net.Uri
import android.widget.VideoView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.micahnyabuto.statussaver.data.local.StatusEntity
import com.micahnyabuto.statussaver.ui.components.PermissionHandler
import com.micahnyabuto.statussaver.ui.navigation.Destinations
<<<<<<< HEAD
import com.micahnyabuto.statussaver.ui.viewmodel.StatusViewModel
=======
import com.micahnyabuto.statussaver.ui.screens.viewmodel.StatusViewModel
import com.micahnyabuto.statussaver.ui.theme.PrimaryLightColor
import com.micahnyabuto.statussaver.ui.theme.SecondaryColor
>>>>>>> 9bb36235ce0dee77733dcc94a50b483f6d73c33f
import java.io.File

@Composable
fun HomeScreen(
    navController: NavController
){
    TopBar(
        navController = navController
    )
    ImageScreen()
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
        TextButton(onClick = {navController.navigate(Destinations.Home)},
            shape = RoundedCornerShape(2.dp)
        ) {
            Text("IMAGES",
                style = MaterialTheme.typography.bodyLarge.copy(
                    Color.White
                ))
        }
        TextButton(onClick = {navController.navigate(Destinations.Videos)},
            shape = RoundedCornerShape(2.dp)) {
            Text("VIDEOS",
                style = MaterialTheme.typography.bodyLarge.copy(
                    Color.White
                ))
        }
    }
}
@Composable
fun ImageScreen(
    viewModel: StatusViewModel = hiltViewModel()
) {
    val imageStatuses by viewModel.imageStatuses.collectAsState()

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
    ){
        items(imageStatuses) { status ->
            StatusItem(status = status, onDownloadClick = {
                viewModel.saveStatus(status)
            })
        }
    }
}
@Composable
fun StatusItem(
    status: StatusEntity,
    viewModel: StatusViewModel=hiltViewModel(),
    onDownloadClick: () -> Unit
) {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column {
            if (status.fileType == "image") {
                Image(
                    painter = rememberAsyncImagePainter(status.filePath),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                )
            } else {
                AndroidView(
                    factory = { context ->
                        VideoView(context).apply {
                            setVideoURI(Uri.parse(status.filePath))
                            setOnPreparedListener { it.isLooping = true; start() }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                )
            }

            IconButton(
                onClick = { viewModel.downloadStatus(context, File(status.filePath) ,status.fileType) },
                modifier = Modifier.align(Alignment.End)
            ) {
                Icon(Icons.Default.Download, contentDescription = "Download")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    navController: NavController
){
    Scaffold (

        topBar = {
            Column (
                modifier = Modifier.background(MaterialTheme.colorScheme.primary)


            ){
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
                CategoriesBar(
                    navController = navController
                )
            }

        }
    ){ innerpadding ->



    }
}
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview(){
    HomeScreen(
        navController = rememberNavController()
    )
}