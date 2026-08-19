package com.example.cryptopulse.presentation.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.cryptopulse.presentation.components.CoinIcon
import com.example.cryptopulse.presentation.components.CoinRow
import com.example.cryptopulse.presentation.components.EmptyState
import com.example.cryptopulse.presentation.components.ErrorState
import com.example.cryptopulse.presentation.components.LoadingCoinRow
import com.example.cryptopulse.presentation.components.PriceChange
import com.example.cryptopulse.presentation.components.SectionHeader
import com.example.cryptopulse.util.DateUtils
import com.example.cryptopulse.util.NumberFormatUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onNavigateToSearch: () -> Unit,
    onNavigateToMarkets: () -> Unit,
    onNavigateToCoinDetail: (String) -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = androidx.compose.ui.res.painterResource(id = com.example.cryptopulse.R.drawable.ic_splash_logo),
                        contentDescription = "Mr. Crypto Logo",
                        modifier = Modifier.size(36.dp).padding(end = 8.dp),
                        tint = androidx.compose.ui.graphics.Color.Unspecified
                    )
                    Text(
                        text = "Mr. Crypto",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
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

        // Offline banner
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

        if (state.isLoading && state.popularCoins.isEmpty()) {
            Column(modifier = Modifier.fillMaxSize()) {
                repeat(8) { LoadingCoinRow() }
            }
        } else if (state.errorMessage != null && state.popularCoins.isEmpty()) {
            ErrorState(
                message = state.errorMessage!!,
                onRetry = { viewModel.refresh() },
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                // Hero section
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                    ),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = "Crypto Market",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        state.totalMarketCap?.let { cap ->
                            Text(
                                text = "Total Market Cap",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = NumberFormatUtils.formatLargeNumber(cap, state.currency),
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }

                // Popular coins
                SectionHeader(
                    title = "Popular Coins",
                    actionText = "See all",
                    onActionClick = onNavigateToMarkets
                )

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(state.popularCoins, key = { it.id }) { coin ->
                        PopularCoinCard(
                            coin = coin,
                            currency = state.currency,
                            onClick = { onNavigateToCoinDetail(coin.id) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Top Gainers
                if (state.topGainers.isNotEmpty()) {
                    SectionHeader(
                        title = "Top Gainers",
                        icon = Icons.Default.TrendingUp
                    )
                    state.topGainers.forEach { coin ->
                        CoinRow(
                            coin = coin,
                            currency = state.currency,
                            onClick = { onNavigateToCoinDetail(coin.id) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Top Losers
                if (state.topLosers.isNotEmpty()) {
                    SectionHeader(
                        title = "Top Losers",
                        icon = Icons.Default.TrendingDown
                    )
                    state.topLosers.forEach { coin ->
                        CoinRow(
                            coin = coin,
                            currency = state.currency,
                            onClick = { onNavigateToCoinDetail(coin.id) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Watchlist
                SectionHeader(
                    title = "Your Watchlist",
                    icon = Icons.Default.Star
                )
                if (state.favoriteCoins.isEmpty()) {
                    EmptyState(
                        message = "Add coins to your watchlist to see them here.",
                        actionText = "Browse Markets",
                        onAction = onNavigateToMarkets
                    )
                } else {
                    state.favoriteCoins.forEach { coin ->
                        CoinRow(
                            coin = coin,
                            currency = state.currency,
                            onClick = { onNavigateToCoinDetail(coin.id) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun PopularCoinCard(
    coin: com.example.cryptopulse.domain.model.Coin,
    currency: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(150.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                CoinIcon(imageUrl = coin.image, coinName = coin.name, size = 32.dp)
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = coin.symbol.uppercase(),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = coin.name,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = NumberFormatUtils.formatPrice(coin.currentPrice, currency),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            PriceChange(percentage = coin.priceChangePercentage24h)
        }
    }
}
