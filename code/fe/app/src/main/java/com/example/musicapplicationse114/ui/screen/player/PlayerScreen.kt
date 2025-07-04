package com.example.musicapplicationse114.ui.screen.player

import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.musicapplicationse114.MainViewModel
import com.example.musicapplicationse114.common.enum.LoadStatus
import com.example.musicapplicationse114.model.SongResponse
import com.example.musicapplicationse114.model.getCurrentLyric
import com.example.musicapplicationse114.model.parseLyrics
import com.example.musicapplicationse114.ui.playerController.PlayerSharedViewModel
import kotlinx.coroutines.delay
import com.example.musicapplicationse114.ui.screen.comment.CommentDialog
import com.example.musicapplicationse114.ui.components.SongOptionsMenu
import com.example.musicapplicationse114.ui.screen.artist.ArtistViewModel
import com.example.musicapplicationse114.ui.screen.home.HomeViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PlayerScreen(
    navController: NavController,
    songId: Long,
    homeViewModel: HomeViewModel = hiltViewModel(),
    viewModel: PlayerViewModel = hiltViewModel(),
    mainViewModel: MainViewModel,
    sharedViewModel: PlayerSharedViewModel,
    artistViewModel: ArtistViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val globalPlayerController = sharedViewModel.player
    val playerState by globalPlayerController.state.collectAsState()
    val state by viewModel.uiState.collectAsState()
    var isSongEnded by remember { mutableStateOf(false) }

    // State for dialogs
    var showCommentDialog by remember { mutableStateOf(false) }

    // Hiển thị thông báo lỗi nếu có
    LaunchedEffect(state.status) {
        if (state.status is LoadStatus.Error && state.toggle) {
            Toast.makeText(context, state.errorMes, Toast.LENGTH_SHORT).show()
        }
        else if(state.status is LoadStatus.Success && state.toggle) {
            Toast.makeText(context, state.successMes, Toast.LENGTH_SHORT).show()
        }
    }


    // Handle share URL
    LaunchedEffect(state.shareUrl) {
        state.shareUrl?.let { url ->
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, url)
                putExtra(Intent.EXTRA_SUBJECT, "Check out this song!")
            }
            val chooser = Intent.createChooser(shareIntent, "Share song")
            context.startActivity(chooser)
            viewModel.clearShareUrl()
        }
    }


    LaunchedEffect(songId) {
        viewModel.loadSongById(songId)
    }

    LaunchedEffect(playerState.position, playerState.duration, playerState.isPlaying) {
        if (playerState.duration > 0 && playerState.position >= playerState.duration - 100) {
            isSongEnded = true
            delay(200)
            isSongEnded = false
        }
    }

    playerState.currentSong?.let { song ->
        PlayerContent(
            song = song,
            navController = navController,
            homeViewModel = homeViewModel,
            viewModel = viewModel,
            mainViewModel = mainViewModel,
            sharedViewModel = sharedViewModel,
            currentPosition = playerState.position,
            duration = playerState.duration,
            isPlaying = playerState.isPlaying,
            isLooping = playerState.isLooping,
            onTogglePlay = { globalPlayerController.toggle() },
            onSeek = { globalPlayerController.seekTo(it) },
            onToggleLoop = { globalPlayerController.setLooping(!playerState.isLooping) },
            onNext = {
                globalPlayerController.nextSong(context)
                sharedViewModel.setSongList(globalPlayerController.getSongList(), globalPlayerController.getCurrentIndex())
                sharedViewModel.addRecentlyPlayed(song.id)
            },
            onPrevious = {
                globalPlayerController.previousSong(context)
                sharedViewModel.setSongList(globalPlayerController.getSongList(), globalPlayerController.getCurrentIndex())
                sharedViewModel.addRecentlyPlayed(song.id)
            },
            upNextSong = sharedViewModel.getUpNext(),
            isSongEnded = isSongEnded
        )
    } ?: run {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            when (val status = state.status) {
                is LoadStatus.Loading -> CircularProgressIndicator(strokeWidth = 2.dp,
                    color = Color.White)
                is LoadStatus.Error -> Text("Lỗi: ${status.description}", color = Color.Red)
                else -> Text("Không tìm thấy bài hát", color = Color.White)
            }
        }
    }

    // Comment Dialog
    if (showCommentDialog) {
        playerState.currentSong?.let { song ->
            CommentDialog(
                song = song,
                onDismiss = { showCommentDialog = false }
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PlayerContent(
    song: SongResponse,
    navController: NavController,
    homeViewModel: HomeViewModel,
    viewModel: PlayerViewModel,
    mainViewModel: MainViewModel,
    sharedViewModel: PlayerSharedViewModel,
    currentPosition: Long,
    duration: Long,
    isPlaying: Boolean,
    isLooping: Boolean,
    onTogglePlay: () -> Unit,
    onSeek: (Long) -> Unit,
    onToggleLoop: () -> Unit,
    onNext: () -> Unit,
    onPrevious: () -> Unit,
    upNextSong: SongResponse?,
    isSongEnded: Boolean
) {
    val context = LocalContext.current
    val state by viewModel.uiState.collectAsState()
    val isLiked = state.likedSongIds.contains(song.id)
    val isDownloaded = state.downloadedSongIds.contains(song.id)
    val globalPlayerController = sharedViewModel.player


    // State for dialogs
    var showCommentDialog by remember { mutableStateOf(false) }

    val lyricsList = remember(song.id) { parseLyrics(song.lyrics) }
    val hasValidLyrics = lyricsList.isNotEmpty()
    val currentLyricLine = remember { mutableStateOf("") }

    LaunchedEffect(isPlaying, currentPosition) {
        if (isPlaying) {
            val line = getCurrentLyric(currentPosition.toInt(), lyricsList)
            currentLyricLine.value = line
            delay(100)
        }
    }
    if (isSongEnded) {
        sharedViewModel.setSongList(globalPlayerController.getSongList(), globalPlayerController.getCurrentIndex())
        sharedViewModel.addRecentlyPlayed(song.id)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                mainViewModel.setFullScreenPlayer(false)
                navController.popBackStack()
            }) {
                Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Back", tint = Color.White, modifier = Modifier.size(32.dp))
            }

            Spacer(modifier = Modifier.width(290.dp))

            SongOptionsMenu(
                song = song,
                navController = navController,
                isFavorite = isLiked,
                isDownloaded = isDownloaded,
                onFavoriteToggle = { viewModel.toggleFavorite(song) },
                onDownloadToggle = { viewModel.toggleDownload(song) },
                onCommentClick = { showCommentDialog = true },
                onAddToPlaylists = { songId, playlistIds ->
                    viewModel.addToPlaylists(songId, playlistIds)
                    Toast.makeText(context, "Adding to ${playlistIds.size} playlist(s)...", Toast.LENGTH_SHORT).show()
                },
                onShare = { songId ->
                    viewModel.shareSong(songId)
                    Toast.makeText(context, "Sharing song...", Toast.LENGTH_SHORT).show()
                }
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Box(
            modifier = Modifier
                .size(350.dp)
                .clip(RoundedCornerShape(16.dp))
        ) {
            Image(
                painter = rememberAsyncImagePainter(song.thumbnail),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize()
            )
            if (!song.lyrics.isNullOrBlank()) {
                Text(
                    text = if (hasValidLyrics) currentLyricLine.value else song.lyrics,
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .background(Color.Black.copy(alpha = 0.6f))
                        .padding(8.dp),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = song.title,
                        color = Color.LightGray,
                        fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Row {
                    IconButton(onClick = { viewModel.toggleFavorite(song) }) {
                        Icon(
                            imageVector = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Like",
                            tint = if (isLiked) Color.Red else Color.White
                        )
                    }

                    IconButton(onClick = { viewModel.toggleDownload(song)}) {
                        Icon(
                            imageVector = Icons.Default.Download,
                            contentDescription = "Download",
                            tint = if (isDownloaded) Color.Cyan else Color.White
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                song.artistName,
                color = Color.LightGray,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 20.sp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Slider(
            value = currentPosition.toFloat(),
            onValueChange = { onSeek(it.toLong()) },
            valueRange = 0f..duration.toFloat(),
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(formatDuration(currentPosition), color = Color.Gray)
            Text(formatDuration(duration), color = Color.Gray)
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            IconButton(onClick = { /* TODO: shuffle */ }, modifier = Modifier.size(48.dp)) {
                Icon(Icons.Default.Shuffle, contentDescription = "Shuffle", tint = Color.White)
            }

            Spacer(modifier = Modifier.width(16.dp))

            IconButton(onClick = onPrevious, modifier = Modifier.size(40.dp)) {
                Icon(Icons.Default.SkipPrevious, contentDescription = "Previous Song", tint = Color.White)
            }

            Spacer(modifier = Modifier.width(16.dp))

            IconButton(onClick = onTogglePlay, modifier = Modifier.size(96.dp)) {
                Icon(
                    if (isPlaying) Icons.Default.PauseCircle else Icons.Default.PlayCircle,
                    contentDescription = "Play/Pause",
                    tint = Color.White,
                    modifier = Modifier.size(96.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            IconButton(onClick = onNext, modifier = Modifier.size(40.dp)) {
                Icon(Icons.Default.SkipNext, contentDescription = "Next Song", tint = Color.White)
            }

            Spacer(modifier = Modifier.width(16.dp))

            IconButton(onClick = onToggleLoop, modifier = Modifier.size(48.dp)) {
                Icon(
                    Icons.Default.Repeat,
                    contentDescription = "Repeat",
                    tint = if (isLooping) Color.Cyan else Color.White
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp, end = 2.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Up Next", color = Color.LightGray.copy(alpha = 0.5f), fontSize = 20.sp)
            Spacer(modifier = Modifier.width(170.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable {
                    navController.navigate("queue")
                    println("Navigating to Queue, current queue size: ${sharedViewModel.queue.size}")
                }
            ) {
                Text("Queue", color = Color.White, fontSize = 20.sp)
                Spacer(modifier = Modifier.width(4.dp))
                Icon(Icons.Default.ArrowForwardIos, contentDescription = "Queue", tint = Color.White, modifier = Modifier.size(25.dp))
            }
        }
        upNextSong?.let { song ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .background(Color.Gray.copy(alpha = 0.2f), shape = RoundedCornerShape(8.dp))
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = song.thumbnail,
                    contentDescription = song.title,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = song.title,
                        color = Color.White,
                        fontSize = 16.sp,
                        maxLines = Int.MAX_VALUE,
                        overflow = TextOverflow.Clip
                    )
                    Text(song.artistName, color = Color.LightGray, style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
    
    // Comment Dialog
    if (showCommentDialog) {
        CommentDialog(
            song = song,
            onDismiss = { showCommentDialog = false }
        )
    }
}

fun formatDuration(durationMs: Long): String {
    val totalSec = durationMs / 1000
    val minutes = totalSec / 60
    val seconds = totalSec % 60
    return "%02d:%02d".format(minutes, seconds)
}