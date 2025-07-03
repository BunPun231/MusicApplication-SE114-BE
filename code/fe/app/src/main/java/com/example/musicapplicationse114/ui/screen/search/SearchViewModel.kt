package com.example.musicapplicationse114.ui.screen.search

import androidx.lifecycle.ViewModel
import com.example.musicapplicationse114.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class SearchUiState(
    val query: String = "",
    val trendingArtists: List<Artist> = emptyList(),
    val categories: List<Category> = emptyList(),
    val recentSearches: List<String> = emptyList()

)
data class Category(val name: String, val imageRes: Int)
data class Artist(
    val name: String,
    val imageRes: Int
)
class SearchViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(
        SearchUiState(
            trendingArtists = listOf(
                Artist("Childish Gambino", R.drawable.childish),
                Artist("Marvin Gaye", R.drawable.marvin),
                Artist("Kanye West", R.drawable.kanye),
                Artist("Justin Bieber", R.drawable.bieber)
            ),
            categories = listOf(
                Category("TAMIL", R.drawable.tamil),
                Category("INTERNATIONAL", R.drawable.international),
                Category("POP", R.drawable.pop),
                Category("HIP-HOP", R.drawable.hiphop),
                Category("DANCE", R.drawable.dance),
                Category("COUNTRY", R.drawable.country),
                Category("INDIE", R.drawable.indie),
                Category("JAZZ", R.drawable.jazz)
            ),
            recentSearches = listOf("You (feat. Travis Scott)", "Maroon 5", "Phonk Madness")
        )
    )

    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    fun onQueryChange(newQuery: String) {
        _uiState.value = _uiState.value.copy(query = newQuery)
    }

    fun clearHistory() {
        _uiState.value = _uiState.value.copy(recentSearches = emptyList())
    }

    fun removeFromHistory(item: String) {
        _uiState.value = _uiState.value.copy(
            recentSearches = _uiState.value.recentSearches.filterNot { it == item }
        )
    }
}
