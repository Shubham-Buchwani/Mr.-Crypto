package com.example.cryptopulse.data.remote.api

import com.example.cryptopulse.data.remote.dto.CoinDetailDto
import com.example.cryptopulse.data.remote.dto.CoinMarketChartDto
import com.example.cryptopulse.data.remote.dto.CoinMarketDto
import com.example.cryptopulse.data.remote.dto.SearchResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CoinGeckoApi {

    @GET("coins/markets")
    suspend fun getMarkets(
        @Query("vs_currency") vsCurrency: String = "usd",
        @Query("order") order: String = "market_cap_desc",
        @Query("per_page") perPage: Int = 50,
        @Query("page") page: Int = 1,
        @Query("sparkline") sparkline: Boolean = false,
        @Query("price_change_percentage") priceChangePercentage: String = "24h"
    ): List<CoinMarketDto>

    @GET("coins/{id}")
    suspend fun getCoinDetail(
        @Path("id") id: String,
        @Query("localization") localization: Boolean = false,
        @Query("tickers") tickers: Boolean = false,
        @Query("market_data") marketData: Boolean = true,
        @Query("community_data") communityData: Boolean = false,
        @Query("developer_data") developerData: Boolean = false
    ): CoinDetailDto

    @GET("coins/{id}/market_chart")
    suspend fun getMarketChart(
        @Path("id") id: String,
        @Query("vs_currency") vsCurrency: String = "usd",
        @Query("days") days: String = "7"
    ): CoinMarketChartDto

    @GET("search")
    suspend fun search(
        @Query("query") query: String
    ): SearchResponseDto

    @GET("search/trending")
    suspend fun getTrending(): com.example.cryptopulse.data.remote.dto.TrendingResponseDto
}
