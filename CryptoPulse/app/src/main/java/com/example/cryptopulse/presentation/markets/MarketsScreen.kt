package com.example.cryptopulse.presentation.markets

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.cryptopulse.presentation.components.CoinRow
import com.example.cryptopulse.presentation.components.ErrorState
import com.example.cryptopulse.presentation.components.LoadingCoinRow
import com.example.cryptopulse.util.DateUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarketsScreen(
    viewModel: MarketsViewModel,
    onNavigateToSearch: () -> Unit,
    onNavigateToCoinDetail: (String) -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = {
                Text(
                    text = "Markets",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            },
            actions = {
                IconButton(onClick = onNavigateToSearch) {
                    Icon(Icons.Default.Search, contentDescription = "Search")
                }
                IconButton(onClick = { viewModel.refresh() }) {
                    Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        )

        if (state.isOffline && state.lastUpdated != null) {
            Snackbar(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            ) {
                Text(
                    text = "Offline · Updated ${DateUtils.timeAgo(state.lastUpdated!!)}",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        if (state.isLoading && state.coins.isEmpty()) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(12) { LoadingCoinRow() }
            }
        } else if (state.errorMessage != null && state.coins.isEmpty()) {
            ErrorState(
                message = state.errorMessage!!,
                onRetry = { viewModel.refresh() },
                modifier = Modifier.fillMaxSize()
            )
        } else {
            PullToRefreshBox(
                isRefreshing = state.isRefreshing,
                onRefresh = { viewModel.refresh() },
                modifier = Modifier.fillMaxSize()
            ) {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(
                        items = state.coins,
                        key = { it.id }
                    ) { coin ->
                        CoinRow(
                            coin = coin,
                            currency = state.currency,
                            onClick = { onNavigateToCoinDetail(coin.id) }
                        )
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
                        )
                    }
                }
            }
        }
    }
}
