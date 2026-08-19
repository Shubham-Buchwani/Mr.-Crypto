package com.example.cryptopulse.data.repository

import com.example.cryptopulse.data.local.dao.CoinMarketDao
import com.example.cryptopulse.data.local.dao.FavoriteCoinDao
import com.example.cryptopulse.data.local.entity.CoinMarketEntity
import com.example.cryptopulse.data.local.entity.FavoriteCoinEntity
import com.example.cryptopulse.data.remote.api.CoinGeckoApi
import com.example.cryptopulse.data.remote.dto.CoinMarketDto
import com.example.cryptopulse.domain.model.Coin
import com.example.cryptopulse.domain.model.CoinDetail
import com.example.cryptopulse.domain.model.CoinMarketChart
import com.example.cryptopulse.domain.model.PricePoint
import com.example.cryptopulse.domain.model.SearchResult
import com.example.cryptopulse.domain.repository.CoinRepository
import com.example.cryptopulse.util.HtmlUtils
import com.example.cryptopulse.util.NetworkResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.io.IOException

class CoinRepositoryImpl(
    private val api: CoinGeckoApi,
    private val coinMarketDao: CoinMarketDao,
    private val favoriteCoinDao: FavoriteCoinDao
) : CoinRepository {

    private val requestTimestamps = mutableMapOf<String, Long>()
    private val minRequestIntervalMs = 10_000L // 10 second throttle

    override fun getMarketCoins(
        currency: String,
        page: Int,
        perPage: Int,
        order: String,
        forceRefresh: Boolean
    ): Flow<NetworkResult<List<Coin>>> = flow {
        // Emit cached data first
        val cachedCoins = coinMarketDao.getAllCoinsList(currency)
        val favoriteIds = favoriteCoinDao.getAllFavoriteIdsSync()

        if (cachedCoins.isNotEmpty()) {
            emit(NetworkResult.Loading(cachedCoins.map { it.toCoin(it.id in favoriteIds) }))
        } else {
            emit(NetworkResult.Loading())
        }

        // Check throttle
        val requestKey = "markets_${currency}_${page}"
        val lastRequestTime = requestTimestamps[requestKey] ?: 0L
        val now = System.currentTimeMillis()

        if (!forceRefresh && now - lastRequestTime < minRequestIntervalMs && cachedCoins.isNotEmpty()) {
            emit(NetworkResult.Success(cachedCoins.map { it.toCoin(it.id in favoriteIds) }))
            return@flow
        }

        try {
            val response = api.getMarkets(
                vsCurrency = currency,
                order = order,
                perPage = perPage,
                page = page
            )
            requestTimestamps[requestKey] = now

            val entities = response.mapNotNull { it.toEntity(currency) }
            if (entities.isNotEmpty()) {
                if (page == 1) {
                    coinMarketDao.deleteAll(currency)
                }
                coinMarketDao.insertAll(entities)
            }

            val updatedFavoriteIds = favoriteCoinDao.getAllFavoriteIdsSync()
            val coins = entities.map { it.toCoin(it.id in updatedFavoriteIds) }
            emit(NetworkResult.Success(coins))
        } catch (e: IOException) {
            if (cachedCoins.isNotEmpty()) {
                emit(NetworkResult.Error("No internet connection. Showing cached data.", cachedCoins.map { it.toCoin(it.id in favoriteIds) }))
            } else {
                emit(NetworkResult.Error("No internet connection. Please check your network and try again."))
            }
        } catch (e: retrofit2.HttpException) {
            val errorMsg = when (e.code()) {
                429 -> "Too many requests. Please wait a moment and try again."
                401, 403 -> "API key error. Please check your CoinGecko API key configuration."
                in 500..599 -> "CoinGecko server error. Please try again later."
                else -> "Failed to load market data (Error ${e.code()})."
            }
            if (cachedCoins.isNotEmpty()) {
                emit(NetworkResult.Error(errorMsg, cachedCoins.map { it.toCoin(it.id in favoriteIds) }))
            } else {
                emit(NetworkResult.Error(errorMsg))
            }
        } catch (e: Exception) {
            if (cachedCoins.isNotEmpty()) {
                emit(NetworkResult.Error("Something went wrong. Showing cached data.", cachedCoins.map { it.toCoin(it.id in favoriteIds) }))
            } else {
                emit(NetworkResult.Error("Something went wrong. Please try again."))
            }
        }
    }

    override suspend fun getCoinDetail(coinId: String, currency: String): NetworkResult<CoinDetail> {
        // Retry up to 3 times for rate-limited requests
        var lastException: Exception? = null
        repeat(3) { attempt ->
            try {
                if (attempt > 0) kotlinx.coroutines.delay(2000L * attempt)
                val response = api.getCoinDetail(id = coinId)
                val isFav = favoriteCoinDao.isFavoriteSync(coinId)
                val detail = response.toDomain(currency, isFav)
                return NetworkResult.Success(detail)
            } catch (e: IOException) {
                return NetworkResult.Error("No internet connection. Please check your network and try again.")
            } catch (e: retrofit2.HttpException) {
                if (e.code() == 429 && attempt < 2) {
                    lastException = e
                    // Will retry
                } else {
                    val errorMsg = when (e.code()) {
                        429 -> "Too many requests. Please wait a moment and try again."
                        404 -> "Coin not found."
                        401 -> "Unauthorized. Please check your API key."
                        else -> "Failed to load coin details (Error ${e.code()})."
                    }
                    return NetworkResult.Error(errorMsg)
                }
            } catch (e: Exception) {
                return NetworkResult.Error("Failed to load coin details: ${e.message}")
            }
        }
        return NetworkResult.Error("Too many requests. Please wait a moment and try again.")
    }

    override suspend fun getMarketChart(coinId: String, currency: String, days: String): NetworkResult<CoinMarketChart> {
        var lastException: Exception? = null
        repeat(3) { attempt ->
            try {
                if (attempt > 0) kotlinx.coroutines.delay(2000L * attempt)
                val response = api.getMarketChart(id = coinId, vsCurrency = currency, days = days)
                val prices = response.prices?.mapNotNull { pair ->
                    try {
                        if (pair.size >= 2) PricePoint(timestamp = pair[0].toLong(), price = pair[1])
                        else null
                    } catch (e: Exception) { null }
                } ?: emptyList()
                return NetworkResult.Success(CoinMarketChart(prices = prices))
            } catch (e: IOException) {
                return NetworkResult.Error("No internet connection.")
            } catch (e: retrofit2.HttpException) {
                if (e.code() == 429 && attempt < 2) {
                    lastException = e
                } else {
                    val errorMsg = when (e.code()) {
                        429 -> "Too many requests. Please wait a moment and try again."
                        401 -> "Unauthorized. Please check your API key."
                        else -> "Failed to load chart data (Error ${e.code()})"
                    }
                    return NetworkResult.Error(errorMsg)
                }
            } catch (e: Exception) {
                return NetworkResult.Error("Failed to load chart data: ${e.message}")
            }
        }
        return NetworkResult.Error("Too many requests. Please wait a moment and try again.")
    }

    override suspend fun searchCoins(query: String): NetworkResult<List<SearchResult>> {
        return try {
            val response = api.search(query = query)
            val results = response.coins?.mapNotNull { coin ->
                val id = coin.id ?: return@mapNotNull null
                SearchResult(
                    id = id,
                    name = coin.name ?: "",
                    symbol = coin.symbol ?: "",
                    marketCapRank = coin.marketCapRank,
                    thumbUrl = coin.thumb ?: "",
                    largeUrl = coin.large ?: ""
                )
            } ?: emptyList()
            NetworkResult.Success(results)
        } catch (e: IOException) {
            NetworkResult.Error("No internet connection.")
        } catch (e: Exception) {
            NetworkResult.Success(emptyList())
        }
    }

    override suspend fun getTrendingCoins(): NetworkResult<List<SearchResult>> {
        return try {
            val response = api.getTrending()
            val results = response.coins?.mapNotNull { wrapper ->
                val coin = wrapper.item ?: return@mapNotNull null
                val id = coin.id ?: return@mapNotNull null
                SearchResult(
                    id = id,
                    name = coin.name ?: "",
                    symbol = coin.symbol ?: "",
                    marketCapRank = coin.marketCapRank,
                    thumbUrl = coin.thumb ?: "",
                    largeUrl = coin.large ?: ""
                )
            } ?: emptyList()
            NetworkResult.Success(results)
        } catch (e: IOException) {
            NetworkResult.Error("No internet connection.")
        } catch (e: Exception) {
            NetworkResult.Error("Failed to load trending coins: ${e.message}")
        }
    }

    override suspend fun toggleFavorite(coinId: String) {
        if (favoriteCoinDao.isFavoriteSync(coinId)) {
            favoriteCoinDao.removeFavorite(coinId)
        } else {
            favoriteCoinDao.addFavorite(FavoriteCoinEntity(coinId = coinId))
        }
    }

    override fun getFavoriteIds(): Flow<List<String>> = favoriteCoinDao.getAllFavoriteIds()

    override fun isFavorite(coinId: String): Flow<Boolean> = favoriteCoinDao.isFavorite(coinId)

    override fun getFavoriteCoins(currency: String): Flow<List<Coin>> {
        return combine(
            favoriteCoinDao.getAllFavoriteIds(),
            coinMarketDao.getAllCoins(currency)
        ) { favoriteIds, allCoins ->
            allCoins.filter { it.id in favoriteIds }
                .map { it.toCoin(isFavorite = true) }
        }
    }

    override suspend fun getLastUpdatedTime(currency: String): Long? {
        return coinMarketDao.getLastUpdatedTime(currency)
    }

    // Extension functions for mapping

    private fun CoinMarketDto.toEntity(currency: String): CoinMarketEntity? {
        val coinId = id ?: return null
        return CoinMarketEntity(
            id = coinId,
            symbol = symbol ?: "",
            name = name ?: "",
            image = image ?: "",
            currentPrice = currentPrice ?: 0.0,
            marketCap = marketCap ?: 0.0,
            marketCapRank = marketCapRank ?: 0,
            totalVolume = totalVolume ?: 0.0,
            high24h = high24h ?: 0.0,
            low24h = low24h ?: 0.0,
            priceChange24h = priceChange24h ?: 0.0,
            priceChangePercentage24h = priceChangePercentage24h ?: 0.0,
            circulatingSupply = circulatingSupply ?: 0.0,
            totalSupply = totalSupply,
            maxSupply = maxSupply,
            ath = ath ?: 0.0,
            athChangePercentage = athChangePercentage ?: 0.0,
            athDate = athDate ?: "",
            atl = atl ?: 0.0,
            atlChangePercentage = atlChangePercentage ?: 0.0,
            atlDate = atlDate ?: "",
            lastUpdated = System.currentTimeMillis(),
            currency = currency
        )
    }

    private fun CoinMarketEntity.toCoin(isFavorite: Boolean = false): Coin {
        return Coin(
            id = id,
            symbol = symbol,
            name = name,
            image = image,
            currentPrice = currentPrice,
            marketCap = marketCap,
            marketCapRank = marketCapRank,
            totalVolume = totalVolume,
            high24h = high24h,
            low24h = low24h,
            priceChange24h = priceChange24h,
            priceChangePercentage24h = priceChangePercentage24h,
            circulatingSupply = circulatingSupply,
            totalSupply = totalSupply,
            maxSupply = maxSupply,
            ath = ath,
            athChangePercentage = athChangePercentage,
            athDate = athDate,
            atl = atl,
            atlChangePercentage = atlChangePercentage,
            atlDate = atlDate,
            isFavorite = isFavorite
        )
    }

    private fun com.example.cryptopulse.data.remote.dto.CoinDetailDto.toDomain(
        currency: String,
        isFav: Boolean
    ): CoinDetail {
        val cur = currency.lowercase()
        return CoinDetail(
            id = id ?: "",
            symbol = symbol ?: "",
            name = name ?: "",
            description = HtmlUtils.stripHtml(description?.get("en")),
            imageUrl = image?.large ?: image?.small ?: "",
            marketCapRank = marketCapRank ?: marketData?.marketCapRank ?: 0,
            currentPrice = marketData?.currentPrice?.get(cur) ?: 0.0,
            marketCap = marketData?.marketCap?.get(cur) ?: 0.0,
            totalVolume = marketData?.totalVolume?.get(cur) ?: 0.0,
            high24h = marketData?.high24h?.get(cur) ?: 0.0,
            low24h = marketData?.low24h?.get(cur) ?: 0.0,
            priceChange24h = marketData?.priceChange24h ?: 0.0,
            priceChangePercentage24h = marketData?.priceChangePercentage24h ?: 0.0,
            circulatingSupply = marketData?.circulatingSupply ?: 0.0,
            totalSupply = marketData?.totalSupply,
            maxSupply = marketData?.maxSupply,
            ath = marketData?.ath?.get(cur) ?: 0.0,
            athChangePercentage = marketData?.athChangePercentage?.get(cur) ?: 0.0,
            athDate = marketData?.athDate?.get(cur) ?: "",
            atl = marketData?.atl?.get(cur) ?: 0.0,
            atlChangePercentage = marketData?.atlChangePercentage?.get(cur) ?: 0.0,
            atlDate = marketData?.atlDate?.get(cur) ?: "",
            isFavorite = isFav
        )
    }

    // Helper extension for getting favorite IDs synchronously
    private suspend fun FavoriteCoinDao.getAllFavoriteIdsSync(): Set<String> {
        return try {
            getAllFavoriteIds().first().toSet()
        } catch (e: Exception) {
            emptySet()
        }
    }
}
