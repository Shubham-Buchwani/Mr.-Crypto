package com.example.cryptopulse.presentation.favorites

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.cryptopulse.presentation.components.CoinRow
import com.example.cryptopulse.presentation.components.EmptyState
import com.example.cryptopulse.presentation.components.FavoriteButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    viewModel: FavoritesViewModel,
    onNavigateToCoinDetail: (String) -> Unit,
    onNavigateToMarkets: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = {
                Text(
                    text = "Watchlist",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        )

        if (state.favorites.isEmpty() && !state.isLoading) {
            EmptyState(
                message = "Your watchlist is empty.\nTap the star icon on any coin to add it here.",
                actionText = "Browse Markets",
                onAction = onNavigateToMarkets,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            // Header count
            Text(
                text = "${state.favorites.size} coin${if (state.favorites.size != 1) "s" else ""} tracked",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                itemsIndexed(
                    items = state.favorites,
                    key = { _, coin -> coin.id }
                ) { index, coin ->
                    AnimatedVisibility(
                        visible = true,
                        enter = fadeIn(tween(300, delayMillis = index * 50)) +
                                slideInVertically(tween(300, delayMillis = index * 50)) { it / 2 },
                        exit = fadeOut()
                    ) {
                        Column {
                            CoinRow(
                                coin = coin,
                                currency = state.currency,
                                onClick = { onNavigateToCoinDetail(coin.id) },
                                trailingContent = {
                                    FavoriteButton(
                                        isFavorite = true,
                                        onToggle = { viewModel.removeFavorite(coin.id) },
                                        coinName = coin.name
                                    )
                                }
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
}
