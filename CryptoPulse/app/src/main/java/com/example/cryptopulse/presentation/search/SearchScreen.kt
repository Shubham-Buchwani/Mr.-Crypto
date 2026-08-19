package com.example.cryptopulse.presentation.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.cryptopulse.presentation.components.CoinIcon
import com.example.cryptopulse.presentation.components.EmptyState
import com.example.cryptopulse.presentation.components.ErrorState
import com.example.cryptopulse.presentation.components.SectionHeader
import androidx.compose.material.icons.filled.TrendingUp
import com.example.cryptopulse.domain.model.SearchResult

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToCoinDetail: (String) -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize()) {
        SearchBar(
            inputField = {
                SearchBarDefaults.InputField(
                    query = state.query,
                    onQueryChange = { viewModel.onQueryChange(it) },
                    onSearch = { },
                    expanded = false,
                    onExpandedChange = { },
                    placeholder = { Text("Search cryptocurrencies...") },
                    leadingIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                        }
                    },
                    trailingIcon = {
                        if (state.query.isNotEmpty()) {
                            IconButton(onClick = { viewModel.clearSearch() }) {
                                Icon(Icons.Default.Clear, "Clear")
                            }
                        } else {
                            Icon(Icons.Default.Search, "Search")
                        }
                    }
                )
            },
            expanded = false,
            onExpandedChange = { },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) { }

        when {
            state.isLoading -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            }
            state.errorMessage != null -> {
                ErrorState(
                    message = state.errorMessage!!,
                    modifier = Modifier.fillMaxSize()
                )
            }
            state.hasSearched && state.results.isEmpty() -> {
                Column(modifier = Modifier.fillMaxSize()) {
                    EmptyState(
                        message = "No results found for \"${state.query}\"",
                        modifier = Modifier.fillMaxWidth().padding(vertical = 32.dp)
                    )
                    if (state.trendingCoins.isNotEmpty()) {
                        SectionHeader(title = "Trending Searches", icon = Icons.Default.TrendingUp)
                        LazyColumn(modifier = Modifier.fillMaxWidth()) {
                            items(state.trendingCoins, key = { it.id }) { result ->
                                SearchResultRow(result = result, onClick = { onNavigateToCoinDetail(result.id) })
                            }
                        }
                    }
                }
            }
            state.results.isNotEmpty() -> {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(state.results, key = { it.id }) { result ->
                        SearchResultRow(result = result, onClick = { onNavigateToCoinDetail(result.id) })
                    }
                }
            }
            !state.hasSearched -> {
                Column(modifier = Modifier.fillMaxSize()) {
                    if (state.trendingCoins.isNotEmpty()) {
                        SectionHeader(title = "Trending Searches", icon = Icons.Default.TrendingUp)
                        LazyColumn(modifier = Modifier.fillMaxWidth()) {
                            items(state.trendingCoins, key = { it.id }) { result ->
                                SearchResultRow(result = result, onClick = { onNavigateToCoinDetail(result.id) })
                            }
                        }
                    } else {
                        EmptyState(
                            message = "Search for any cryptocurrency by name or symbol",
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchResultRow(result: SearchResult, onClick: () -> Unit) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CoinIcon(
                imageUrl = result.largeUrl.ifBlank { result.thumbUrl },
                coinName = result.name,
                size = 40.dp
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = result.name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = result.symbol.uppercase(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            result.marketCapRank?.let { rank ->
                Text(
                    text = "#$rank",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 16.dp),
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
        )
    }
}
