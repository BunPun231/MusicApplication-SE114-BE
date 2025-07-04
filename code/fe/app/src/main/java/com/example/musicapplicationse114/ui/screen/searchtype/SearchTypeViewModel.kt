package com.example.musicapplicationse114.ui.screen.searchtype

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.example.musicapplicationse114.R
import com.example.musicapplicationse114.Screen
import com.example.musicapplicationse114.auth.TokenManager
import com.example.musicapplicationse114.common.enum.LoadStatus
import com.example.musicapplicationse114.model.AlbumResponse
import com.example.musicapplicationse114.model.ArtistResponse
import com.example.musicapplicationse114.model.PlaylistResponse
import com.example.musicapplicationse114.model.SongResponse
import com.example.musicapplicationse114.repositories.Api
import com.example.musicapplicationse114.repositories.MainLog
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SearchTypeUiState(
    val recentSearches: List<RecentSearch> = emptyList(),
    val query: String = "",
    val songs: List<SongResponse> = emptyList(),
    val songs1 : List<SongResponse> = emptyList(),
    val albums : List<AlbumResponse> = emptyList(),
    val artists: List<ArtistResponse> = emptyList(),
    val playlists: List<PlaylistResponse> = emptyList(),
    val totalResults : Long = 0,
    val status : LoadStatus = LoadStatus.Init(),

    )

@HiltViewModel
class SearchTypeViewModel @Inject constructor(
    private val mainLog: MainLog?,
    private val api: Api?,
    private val tokenManager: TokenManager?
) : ViewModel() {
    val _uiState = MutableStateFlow(SearchTypeUiState())
    val uiState = _uiState.asStateFlow()
    private var searchJob: Job? = null

    fun searchAllDebounced(query: String, limit: Int = 10) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(100)
            searchAll(query, limit)
        }
    }


    fun updateQuery(query: String) {
        _uiState.value = _uiState.value.copy(query = query)
    }

    fun searchAll(query: String, limit: Int = 10)
    {
        if(query.isBlank()) return
        viewModelScope.launch {
            try {
//                _uiState.value = _uiState.value.copy(status = LoadStatus.Loading())
                val token = tokenManager?.getToken()
                if(api != null && !token.isNullOrBlank())
                {
                   val result = api.globalSearch(token, query, limit)
                    _uiState.value = _uiState.value.copy(
                        songs = result.songs,
                        albums = result.albums,
                        artists = result.artists,
                        playlists = result.playlists,
                        totalResults = result.totalResults,
                        status = LoadStatus.Success())
                }
                else
                {
                    _uiState.value = _uiState.value.copy(status = LoadStatus.Error("Không có API"))
                }
            }
            catch(e: Exception)
            {
                _uiState.value = _uiState.value.copy(status = LoadStatus.Error(e.message.toString()))
            }
        }

    }

    fun loadSong()
    {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(status = LoadStatus.Loading())
                val token = tokenManager?.getToken()
                if(api != null && !token.isNullOrBlank()){
                    Log.d("SongRequest", "Token: $token")
                    val songs = api.getSongs(token)
                    Log.d("SearchSongAddIntoPlaylistViewModel", "Songs loaded: ${songs.content.size} items")
                    _uiState.value = _uiState.value.copy(
                        songs1 = songs.content,
                        status = LoadStatus.Success()
                    )
                }
                else
                {
                    Log.e("SearchSongAddIntoPlaylistViewModel", "API hoặc token null")
                    _uiState.value = _uiState.value.copy(status = LoadStatus.Error("Không có token hoặc API"))
                }
            }catch(ex : Exception)
            {
                _uiState.value = _uiState.value.copy(status = LoadStatus.Error(ex.message.toString()))
                Log.d("SearchSongAddIntoPlaylistViewModel", "Failed to load songs: ${ex.message}")
            }
        }
    }
}
