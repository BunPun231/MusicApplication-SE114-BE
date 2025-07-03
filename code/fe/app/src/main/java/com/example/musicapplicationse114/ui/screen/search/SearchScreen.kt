package com.example.musicapplicationse114.ui.screen.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.musicapplicationse114.R
import com.example.musicapplicationse114.ui.theme.MusicApplicationSE114Theme
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.ui.draw.clip


@Composable
fun SearchScreenStyled(
    navController: NavController,
    state: SearchUiState,
    onQueryChange: (String) -> Unit = {},
    onClearHistory: () -> Unit = {},
    onRemoveFromHistory: (String) -> Unit = {}
) {
    Box(modifier = Modifier.fillMaxSize()) {
        // Nội dung giao diện chính
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.Black
        ) {
            Column(modifier = Modifier.padding(start = 24.dp)) {
                Spacer(modifier = Modifier.height(50.dp))
                SearchHeader(state.query, onQueryChange)
                Spacer(modifier = Modifier.height(30.dp))
                TabBarSearch()
                Spacer(modifier = Modifier.height(30.dp))
                if (state.query.isEmpty()) {
                    SearchTrendingArtists(state)
                    Spacer(modifier = Modifier.height(24.dp))
                    CategoryGrid(state.categories)
                } else {
                    SearchHistoryList(state, onClearHistory, onRemoveFromHistory)
                }
            }
        }

        // NavBar đúng vị trí trong BoxScope
        SearchBottomNavigationBar(
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}



@Composable
fun SearchHeader(query: String, onQueryChange: (String) -> Unit) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = { Text("Search songs, artist, album...", color = Color.LightGray) },
        modifier = Modifier.fillMaxWidth().padding(end = 24.dp),
        shape = RoundedCornerShape(30.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Gray,
            unfocusedBorderColor = Color.DarkGray,
            cursorColor = Color.White,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedContainerColor = Color.DarkGray,
            unfocusedContainerColor = Color.DarkGray
        )
    )
}

@Composable
fun TabBarSearch() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text("Top", fontSize = 19.sp, color = Color.LightGray)
        Spacer(modifier = Modifier.width(25.dp))
        Box(
            modifier = Modifier
                .background(color = Color.DarkGray, shape = RoundedCornerShape(18.dp))
                .padding(horizontal = 18.dp, vertical = 8.dp)
        ) {
            Text("Artists", fontSize = 19.sp, color = Color.LightGray)
        }
        Spacer(modifier = Modifier.width(25.dp))
        Text("Albums", fontSize = 19.sp, color = Color.LightGray)
        Spacer(modifier = Modifier.width(25.dp))
        Text("Playlists", maxLines = 1, fontSize = 19.sp, color = Color.LightGray)
    }
}

@Composable
fun SearchTrendingArtists(state: SearchUiState) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = Icons.Filled.TrendingUp,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            "Trending artists",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
    Spacer(modifier = Modifier.height(8.dp))
    LazyRow(modifier = Modifier.padding(end = 24.dp)) {
        items(state.trendingArtists) { artist ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(8.dp)
            ) {
                Image(
                    painter = painterResource(id = artist.imageRes),
                    contentDescription = artist.name,
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(30.dp))
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = artist.name,
                    color = Color.White,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun SearchBottomNavigationBar(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(Color.Black.copy(alpha = 0.8f))
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Filled.Home, contentDescription = null, tint = Color.LightGray, modifier = Modifier.size(30.dp))
                Spacer(modifier = Modifier.height(3.dp))
                Text("Home", fontSize = 12.sp, color = Color.LightGray)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Filled.Search, contentDescription = null, tint = Color.LightGray, modifier = Modifier.size(30.dp))
                Spacer(modifier = Modifier.height(3.dp))
                Text("Search", fontSize = 12.sp, color = Color.LightGray)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Filled.Menu, contentDescription = null, tint = Color.LightGray, modifier = Modifier.size(30.dp))
                Spacer(modifier = Modifier.height(3.dp))
                Text("Library", fontSize = 12.sp, color = Color.LightGray)
            }
        }
    }
}

@Composable
fun SearchHistoryList(
    state: SearchUiState,
    onClearHistory: () -> Unit,
    onRemoveFromHistory: (String) -> Unit
) {
    Text("Recent searches", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
    Spacer(modifier = Modifier.height(8.dp))
    LazyColumn(modifier = Modifier.padding(end = 24.dp)) {
        items(state.recentSearches.size) { index ->
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = state.recentSearches[index],
                    color = Color.White
                )
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier
                        .size(20.dp)
                        .clickable { onRemoveFromHistory(state.recentSearches[index]) }
                )
            }
        }
        item {
            Text(
                "Clear history",
                color = Color.Gray,
                modifier = Modifier.clickable { onClearHistory() }.padding(top = 8.dp)
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun SearchScreenStyledPreview() {
    val previewState = SearchUiState(
        query = "",
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
        recentSearches = listOf("Maroon 5", "Drake", "Phonk Madness")
    )

    MusicApplicationSE114Theme(darkTheme = true) {
        SearchScreenStyled(
            navController = rememberNavController(),
            state = previewState
        )
    }
}
@Composable
fun CategoryGrid(categories: List<Category>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 24.dp)
    ) {
        items(categories.chunked(2)) { row ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            ) {
                row.forEach { category ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(100.dp)
                            .clip(RoundedCornerShape(16.dp))
                    ) {
                        Image(
                            painter = painterResource(id = category.imageRes),
                            contentDescription = category.name,
                            modifier = Modifier.fillMaxSize()
                        )
                        Text(
                            text = category.name,
                            color = Color.White,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }

                // Nếu row chỉ có 1 item, chèn spacer để căn giữa
                if (row.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}


