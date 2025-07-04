package com.example.musicapplicationse114.ui.playerController

import android.content.Context
import android.util.Log
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.example.musicapplicationse114.model.SongResponse
import com.example.musicapplicationse114.repositories.Api
import com.example.musicapplicationse114.ui.screen.player.PlayerViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GlobalPlayerController @Inject constructor(
    context: Context,
) {
    private val _state = MutableStateFlow(PlayerState())
    val state: MutableStateFlow<PlayerState> = _state
    private val player = ExoPlayer.Builder(context).build()
    private var songList: List<SongResponse> = emptyList()
    private var currentSongIndex: Int = -1
    private var updateJob: Job? = null
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    init {
        player.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {
                if (state == Player.STATE_ENDED) {
                    if (_state.value.isLooping) {
                        player.seekTo(0)
                        player.play()
                    } else {
                        nextSong(context)
                    }
                }
                updatePlayerState()
            }

            override fun onPositionDiscontinuity(
                oldPosition: Player.PositionInfo,
                newPosition: Player.PositionInfo,
                reason: Int
            ) {
                updatePlayerState()
            }
        })

        // Bắt đầu cập nhật trạng thái
        startUpdatingPlayerState()
    }

    fun stop() {
        player.stop()
        player.clearMediaItems()
        _state.value = _state.value.copy(
            currentSong = null,
            isPlaying = false,
            position = 0L,
            duration = 0L
        )
        songList = emptyList()
        currentSongIndex = -1
    }


    private fun startUpdatingPlayerState() {
        updateJob?.cancel()
        updateJob = coroutineScope.launch {
            while (true) {
                if (player.isPlaying) {
                    updatePlayerState()
                }
                delay(100)
            }
        }
    }

    fun getSongList(): List<SongResponse> {
        return songList
    }

    private fun updatePlayerState() {
        _state.value = _state.value.copy(
            position = player.currentPosition.coerceAtLeast(0L),
            duration = player.duration.coerceAtLeast(0L),
            isPlaying = player.isPlaying
        )
    }

    fun setSongList(songs: List<SongResponse>, startIndex: Int = 0) {
        songList = songs
        currentSongIndex = if (songs.isEmpty()) -1 else startIndex.coerceIn(0, songs.size - 1)
    }

    fun play(song: SongResponse) {
        val index = songList.indexOfFirst { it.id == song.id }
        if (index != -1) {
            currentSongIndex = index
        } else {
            songList = listOf(song)
            currentSongIndex = 0
        }
        player.setMediaItem(MediaItem.fromUri(song.audioUrl))
        player.prepare()
        player.play()
        _state.value = _state.value.copy(currentSong = song, isPlaying = true)
    }

    fun toggle() {
        if (player.isPlaying) {
            player.pause()
        } else {
            player.play()
        }
        _state.value = _state.value.copy(isPlaying = player.isPlaying)
    }

    fun setLooping(looping: Boolean) {
        player.repeatMode = if (looping) Player.REPEAT_MODE_ONE else Player.REPEAT_MODE_OFF
        _state.value = _state.value.copy(isLooping = looping)
    }

    fun seekTo(positionMs: Long) {
        player.seekTo(positionMs)
        updatePlayerState()
    }

    fun nextSong(context: Context) {
        if (_state.value.isLooping) return
        if (songList.isEmpty() || currentSongIndex >= songList.size - 1) {
            // Dừng lại, không chuyển bài
            Log.d("GlobalPlayerController", "Reached end of playlist")
            return
        }
        currentSongIndex += 1
        play(songList[currentSongIndex])
    }

    fun previousSong(context: Context) {
        if (_state.value.isLooping) return
        if (songList.isEmpty() || currentSongIndex <= 0) {
            // Dừng lại, không quay ngược
            Log.d("GlobalPlayerController", "At start of playlist")
            return
        }
        currentSongIndex -= 1
        play(songList[currentSongIndex])
    }

    fun release() {
        updateJob?.cancel()
        player.release()
        coroutineScope.cancel()
    }

    fun getCurrentIndex(): Int {
        return currentSongIndex
    }
}