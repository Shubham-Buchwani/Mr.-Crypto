package com.example.cryptopulse.presentation.coin

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.cryptopulse.presentation.components.CoinIcon
import com.example.cryptopulse.presentation.components.ErrorState
import com.example.cryptopulse.presentation.components.FavoriteButton
import com.example.cryptopulse.presentation.components.MarketStat
import com.example.cryptopulse.presentation.components.PriceChange
import com.example.cryptopulse.presentation.components.PriceChart
import com.example.cryptopulse.presentation.components.SectionHeader
import com.example.cryptopulse.util.DateUtils
import com.example.cryptopulse.util.NumberFormatUtils

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun CoinDetailScreen(
    viewModel: CoinDetailViewModel,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = {
                state.coinDetail?.let { detail ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        CoinIcon(
                            imageUrl = detail.imageUrl,
                            coinName = detail.name,
                            size = 28.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = detail.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = detail.symbol.uppercase(),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } ?: Text("Coin Detail")
            },
            navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                }
            },
            actions = {
                FavoriteButton(
                    isFavorite = state.isFavorite,
                    onToggle = { viewModel.toggleFavorite() },
                    coinName = state.coinDetail?.name ?: "coin"
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        )

        when {
            state.isLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            state.errorMessage != null && state.coinDetail == null -> {
                ErrorState(
                    message = state.errorMessage!!,
                    onRetry = { viewModel.retry() },
                    modifier = Modifier.fillMaxSize()
                )
            }
            state.coinDetail != null -> {
                val detail = state.coinDetail!!
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    // Price section
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = NumberFormatUtils.formatPrice(detail.currentPrice, state.currency),
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            PriceChange(percentage = detail.priceChangePercentage24h)
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "24h: ${NumberFormatUtils.formatPrice(detail.low24h, state.currency)} – ${NumberFormatUtils.formatPrice(detail.high24h, state.currency)}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    // Chart
                    Spacer(modifier = Modifier.height(8.dp))
                    if (state.isChartLoading) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(250.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                        }
                    } else {
                        PriceChart(
                            prices = state.chartData?.prices ?: emptyList(),
                            selectedRange = state.selectedTimeRange,
                            onRangeSelected = { viewModel.selectTimeRange(it) },
                            currency = state.currency,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Market Statistics
                    SectionHeader(title = "Market Statistics")
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        FlowRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            maxItemsInEachRow = 2
                        ) {
                            MarketStat(
                                label = "Market Cap",
                                value = NumberFormatUtils.formatLargeNumber(detail.marketCap, state.currency),
                                modifier = Modifier.weight(1f)
                            )
                            MarketStat(
                                label = "Rank",
                                value = "#${detail.marketCapRank}",
                                modifier = Modifier.weight(1f)
                            )
                            MarketStat(
                                label = "24h Volume",
                                value = NumberFormatUtils.formatLargeNumber(detail.totalVolume, state.currency),
                                modifier = Modifier.weight(1f)
                            )
                            MarketStat(
                                label = "Circulating Supply",
                                value = NumberFormatUtils.formatSupply(detail.circulatingSupply, detail.symbol.uppercase()),
                                modifier = Modifier.weight(1f)
                            )
                            MarketStat(
                                label = "Total Supply",
                                value = detail.totalSupply?.let { NumberFormatUtils.formatSupply(it) } ?: "∞",
                                modifier = Modifier.weight(1f)
                            )
                            MarketStat(
                                label = "Max Supply",
                                value = detail.maxSupply?.let { NumberFormatUtils.formatSupply(it) } ?: "∞",
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // ATH/ATL
                    SectionHeader(title = "All-Time Statistics")
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        FlowRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            maxItemsInEachRow = 2
                        ) {
                            MarketStat(
                                label = "All-Time High",
                                value = NumberFormatUtils.formatPrice(detail.ath, state.currency),
                                modifier = Modifier.weight(1f)
                            )
                            MarketStat(
                                label = "ATH Date",
                                value = DateUtils.formatDate(detail.athDate),
                                modifier = Modifier.weight(1f)
                            )
                            MarketStat(
                                label = "From ATH",
                                value = NumberFormatUtils.formatPercentage(detail.athChangePercentage),
                                modifier = Modifier.weight(1f)
                            )
                            MarketStat(
                                label = "All-Time Low",
                                value = NumberFormatUtils.formatPrice(detail.atl, state.currency),
                                modifier = Modifier.weight(1f)
                            )
                            MarketStat(
                                label = "ATL Date",
                                value = DateUtils.formatDate(detail.atlDate),
                                modifier = Modifier.weight(1f)
                            )
                            MarketStat(
                                label = "From ATL",
                                value = NumberFormatUtils.formatPercentage(detail.atlChangePercentage),
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    // Description
                    if (detail.description.isNotBlank()) {
                        Spacer(modifier = Modifier.height(16.dp))
                        SectionHeader(title = "About ${detail.name}")

                        var expanded by remember { mutableStateOf(false) }
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .animateContentSize()
                        ) {
                            Text(
                                text = detail.description,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = if (expanded) Int.MAX_VALUE else 5,
                                overflow = TextOverflow.Ellipsis
                            )
                            if (detail.description.length > 300) {
                                TextButton(onClick = { expanded = !expanded }) {
                                    Text(if (expanded) "Show less" else "Read more")
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}
