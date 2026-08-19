package com.example.cryptopulse.data.remote.dto

import com.google.gson.annotations.SerializedName

data class CoinDetailDto(
    @SerializedName("id") val id: String?,
    @SerializedName("symbol") val symbol: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("description") val description: Map<String, String>?,
    @SerializedName("image") val image: CoinImageDto?,
    @SerializedName("market_cap_rank") val marketCapRank: Int?,
    @SerializedName("market_data") val marketData: MarketDataDto?,
    @SerializedName("last_updated") val lastUpdated: String?
)

data class CoinImageDto(
    @SerializedName("thumb") val thumb: String?,
    @SerializedName("small") val small: String?,
    @SerializedName("large") val large: String?
)

data class MarketDataDto(
    @SerializedName("current_price") val currentPrice: Map<String, Double>?,
    @SerializedName("market_cap") val marketCap: Map<String, Double>?,
    @SerializedName("total_volume") val totalVolume: Map<String, Double>?,
    @SerializedName("high_24h") val high24h: Map<String, Double>?,
    @SerializedName("low_24h") val low24h: Map<String, Double>?,
    @SerializedName("price_change_24h") val priceChange24h: Double?,
    @SerializedName("price_change_percentage_24h") val priceChangePercentage24h: Double?,
    @SerializedName("market_cap_rank") val marketCapRank: Int?,
    @SerializedName("circulating_supply") val circulatingSupply: Double?,
    @SerializedName("total_supply") val totalSupply: Double?,
    @SerializedName("max_supply") val maxSupply: Double?,
    @SerializedName("ath") val ath: Map<String, Double>?,
    @SerializedName("ath_change_percentage") val athChangePercentage: Map<String, Double>?,
    @SerializedName("ath_date") val athDate: Map<String, String>?,
    @SerializedName("atl") val atl: Map<String, Double>?,
    @SerializedName("atl_change_percentage") val atlChangePercentage: Map<String, Double>?,
    @SerializedName("atl_date") val atlDate: Map<String, String>?
)
