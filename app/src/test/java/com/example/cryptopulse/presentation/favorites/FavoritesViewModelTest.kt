package com.example.cryptopulse.presentation.favorites

import app.cash.turbine.test
import com.example.cryptopulse.domain.model.Coin
import com.example.cryptopulse.domain.model.UserPreferences
import com.example.cryptopulse.domain.repository.CoinRepository
import com.example.cryptopulse.domain.repository.UserPreferencesRepository
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FavoritesViewModelTest {

    private lateinit var viewModel: FavoritesViewModel
    private val coinRepository: CoinRepository = mockk(relaxed = true)
    private val prefsRepository: UserPreferencesRepository = mockk(relaxed = true)
    private val testDispatcher = StandardTestDispatcher()

    private val sampleCoin = Coin(
        id = "bitcoin",
        symbol = "btc",
        name = "Bitcoin",
        image = "",
        currentPrice = 50000.0,
        marketCap = 1000000000.0,
        marketCapRank = 1,
        totalVolume = 500000.0,
        high24h = 51000.0,
        low24h = 49000.0,
        priceChange24h = 1000.0,
        priceChangePercentage24h = 2.0,
        circulatingSupply = 19000000.0,
        totalSupply = 21000000.0,
        maxSupply = 21000000.0,
        ath = 69000.0,
        athChangePercentage = -20.0,
        athDate = "",
        atl = 100.0,
        atlChangePercentage = 50000.0,
        atlDate = "",
        isFavorite = true
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        every { prefsRepository.userPreferences } returns flowOf(UserPreferences(currency = "usd"))
        every { coinRepository.getFavoriteCoins(any()) } returns flowOf(listOf(sampleCoin))
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initialization loads favorites from repository`() = runTest {
        viewModel = FavoritesViewModel(coinRepository, prefsRepository)
        
        viewModel.uiState.test {
            val initialState = awaitItem()
            
            testDispatcher.scheduler.advanceUntilIdle()
            
            val finalState = expectMostRecentItem()
            assertEquals(false, finalState.isLoading)
            assertEquals(1, finalState.favorites.size)
            assertEquals("bitcoin", finalState.favorites.first().id)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `removeFavorite calls repository toggle`() = runTest {
        viewModel = FavoritesViewModel(coinRepository, prefsRepository)
        
        viewModel.removeFavorite("bitcoin")
        
        testDispatcher.scheduler.advanceUntilIdle()
        
        coVerify(exactly = 1) { coinRepository.toggleFavorite("bitcoin") }
    }
}
