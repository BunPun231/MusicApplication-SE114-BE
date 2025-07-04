package com.example.musicapplicationse114.ui.screen.library

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.layout.ContentScale
import androidx.compose.material.ripple.rememberRipple
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.musicapplicationse114.MainViewModel
import com.example.musicapplicationse114.Screen
import com.example.musicapplicationse114.common.enum.LoadStatus
import com.example.musicapplicationse114.ui.playerController.PlayerSharedViewModel
import com.example.musicapplicationse114.ui.screen.artists.ArtistsFollowingViewModel
import com.example.musicapplicationse114.ui.screen.home.HomeUiState
import com.example.musicapplicationse114.ui.screen.home.HomeViewModel
import com.example.musicapplicationse114.ui.screen.playlists.PlayListViewModel
import com.example.musicapplicationse114.ui.theme.MusicApplicationSE114Theme
import kotlinx.coroutines.delay

@Composable
fun LibraryScreen(
    navController: NavController,
    viewModel: LibraryViewModel = viewModel(),
    mainViewModel: MainViewModel,
    homeViewModel: HomeViewModel,
    artistsFollowingViewModel: ArtistsFollowingViewModel,
    playListViewModel: PlayListViewModel,
    sharedViewModel: PlayerSharedViewModel
) {
    val state by viewModel.uiState.collectAsState()
    var showLoading by remember { mutableStateOf(false) }
    val globalPlayerController = sharedViewModel.player

    LaunchedEffect(Unit) {
        viewModel.loadRecentlyPlayed()
        homeViewModel.loadFavoriteSong()
        homeViewModel.loadDownloadedSong()
        artistsFollowingViewModel.loadFollowedArtists()
        playListViewModel.loadPlaylist()
    }

    if (showLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(strokeWidth = 2.dp, color = Color.White)
        }
    }

    LaunchedEffect(showLoading) {
        if (showLoading) {
            delay(1000)
            showLoading = false
        }
    }


    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(start = 24.dp, end = 24.dp, top = 48.dp, bottom = 129.dp)
        ) {
            Text(
                "Thư viện",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(24.dp))

            LibraryGrid(
                state,
                homeViewModel,
                artistsFollowingViewModel,
                playListViewModel,
                onItemClick = { tile ->
                    when (tile.title) {
                        "Yêu thích" -> {
                            navController.navigate(Screen.LikedSong.route)
                            Log.d("LibraryScreen", "Navigated to Liked Songs")
                        }
                        "Nghệ sĩ" -> {
                            navController.navigate(Screen.ArtistFollow.route)
                            Log.d("LibraryScreen", "Navigated to Artists")
                        }
                        "Playlists" -> {
                            navController.navigate(Screen.Playlist.route)
                            Log.d("LibraryScreen", "Navigated to Playlists")
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Nghe gần đây", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text("Xem thêm", color = Color.White, fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (state.status is LoadStatus.Loading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(strokeWidth = 2.dp, color = Color.White)
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    items(state.recentlyPlayed) { song ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    sharedViewModel.setSongList(
                                        state.recentlyPlayed,
                                        state.recentlyPlayed.indexOf(song)
                                    )
                                    sharedViewModel.addRecentlyPlayed(song.id)
                                    Log.d("LibraryScreen", "Called addRecentlyPlayed for songId: ${song.id}")
                                    globalPlayerController.play(song)
                                    mainViewModel.setFullScreenPlayer(true)
                                    navController.navigate(Screen.Player.createRoute(song.id))
                                }
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AsyncImage(
                                model = song.thumbnail,
                                contentDescription = song.title,
                                modifier = Modifier.size(50.dp).clip(RoundedCornerShape(4.dp)),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(song.title, color = Color.White, fontSize = 16.sp)
                                Text(song.artistName, color = Color.Gray, fontSize = 14.sp)
                            }
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "More",
                                tint = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LibraryGrid(
    state: LibraryUiState,
    homeViewModel: HomeViewModel,
    artistsFollowingViewModel: ArtistsFollowingViewModel,
    playListViewModel: PlayListViewModel,
    onItemClick: (LibraryTile) -> Unit
) {
    val homeState = homeViewModel.uiState.collectAsState().value
    val playListState = playListViewModel.uiState.collectAsState().value
    val artistFollowingState = artistsFollowingViewModel.uiState.collectAsState().value
    val items = listOf(
        LibraryTile("Yêu thích", "${homeState.likeCount} songs", Icons.Default.Favorite),
        LibraryTile("Tải về", "${homeState.downloadCount} songs", Icons.Default.Download),
        LibraryTile("Playlists", "${playListState.playlistCount} playlists", Icons.Default.List),
        LibraryTile("Nghệ sĩ", "${artistFollowingState.followCount} artists", Icons.Default.Person)
    )

    val gradientBrush = Brush.verticalGradient(
        colors = listOf(Color(0xFF2D2D2D), Color(0xFF1A1A1A)), // Gradient từ xám đậm sang đen
        startY = 0f,
        endY = 300f
    )

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        for (i in items.chunked(2)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                for (item in i) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .shadow(4.dp, RoundedCornerShape(16.dp)) // Thêm shadow
                            .background(gradientBrush, RoundedCornerShape(16.dp)) // Gradient và bo góc mượt
                            .padding(16.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = rememberRipple(color = Color.White.copy(alpha = 0.3f)), // Hiệu ứng ripple
                                onClick = { onItemClick(item) }
                            )
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(item.title, color = Color.White, fontWeight = FontWeight.Bold)
                        Text(item.subtitle, color = Color.Gray, fontSize = 12.sp)
                    }
                }
                if (i.size < 2) Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

data class LibraryTile(val title: String, val subtitle: String, val icon: androidx.compose.ui.graphics.vector.ImageVector)