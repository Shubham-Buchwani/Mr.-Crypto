package com.example.cryptopulse.domain.model

data class CoinMarketChart(
    val prices: List<PricePoint>
)

data class PricePoint(
    val timestamp: Long,
    val price: Double
)
