package com.example.cryptopulse.domain.repository

import com.example.cryptopulse.domain.model.Coin
import com.example.cryptopulse.domain.model.CoinDetail
import com.example.cryptopulse.domain.model.CoinMarketChart
import com.example.cryptopulse.domain.model.SearchResult
import com.example.cryptopulse.util.NetworkResult
import kotlinx.coroutines.flow.Flow

interface CoinRepository {
    fun getMarketCoins(
        currency: String = "usd",
        page: Int = 1,
        perPage: Int = 50,
        order: String = "market_cap_desc",
        forceRefresh: Boolean = false
    ): Flow<NetworkResult<List<Coin>>>

    suspend fun getCoinDetail(id: String, currency: String): NetworkResult<CoinDetail>
    suspend fun getMarketChart(id: String, currency: String, days: String): NetworkResult<CoinMarketChart>
    suspend fun searchCoins(query: String): NetworkResult<List<SearchResult>>
    suspend fun getTrendingCoins(): NetworkResult<List<SearchResult>>

    suspend fun toggleFavorite(coinId: String)

    fun getFavoriteIds(): Flow<List<String>>

    fun isFavorite(coinId: String): Flow<Boolean>

    fun getFavoriteCoins(currency: String = "usd"): Flow<List<Coin>>

    suspend fun getLastUpdatedTime(currency: String = "usd"): Long?
}
