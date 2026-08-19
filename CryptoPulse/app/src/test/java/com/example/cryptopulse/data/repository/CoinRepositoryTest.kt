package com.example.cryptopulse.data.repository

import com.example.cryptopulse.data.local.dao.CoinMarketDao
import com.example.cryptopulse.data.local.dao.FavoriteCoinDao
import com.example.cryptopulse.data.local.entity.CoinMarketEntity
import com.example.cryptopulse.data.remote.api.CoinGeckoApi
import com.example.cryptopulse.data.remote.dto.CoinMarketDto
import com.example.cryptopulse.util.NetworkResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.IOException

class CoinRepositoryTest {

    private val api: CoinGeckoApi = mockk()
    private val coinMarketDao: CoinMarketDao = mockk(relaxed = true)
    private val favoriteCoinDao: FavoriteCoinDao = mockk(relaxed = true)
    
    private lateinit var repository: CoinRepositoryImpl

    @Before
    fun setup() {
        repository = CoinRepositoryImpl(api, coinMarketDao, favoriteCoinDao)
        every { favoriteCoinDao.getAllFavoriteIds() } returns flowOf(emptyList())
    }

    @Test
    fun `getMarketCoins emits cached data on network failure`() = runTest {
        // Setup cache
        val cachedEntity = CoinMarketEntity(
            id = "bitcoin", symbol = "btc", name = "Bitcoin", image = "",
            currentPrice = 0.0, marketCap = 0.0, marketCapRank = 1,
            totalVolume = 0.0, high24h = 0.0, low24h = 0.0,
            priceChange24h = 0.0, priceChangePercentage24h = 0.0,
            circulatingSupply = 0.0, totalSupply = 0.0, maxSupply = 0.0,
            ath = 0.0, athChangePercentage = 0.0, athDate = "",
            atl = 0.0, atlChangePercentage = 0.0, atlDate = ""
        )
        coEvery { coinMarketDao.getAllCoinsList("usd") } returns listOf(cachedEntity)
        
        // Simulate network failure
        coEvery { api.getMarkets(any(), any(), any(), any(), any(), any()) } throws IOException("No internet")
        
        // Call repository
        val results = mutableListOf<NetworkResult<*>>()
        repository.getMarketCoins(forceRefresh = true).collect { results.add(it) }
        
        // Assertions
        assertTrue(results.size >= 2) // Loading (cached), Error (with cached data)
        val errorResult = results.last() as NetworkResult.Error<List<com.example.cryptopulse.domain.model.Coin>>
        assertTrue(errorResult.data?.isNotEmpty() == true)
        assertEquals("bitcoin", errorResult.data?.first()?.id)
    }
}
