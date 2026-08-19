package com.example.cryptopulse.presentation.markets

import app.cash.turbine.test
import com.example.cryptopulse.domain.model.Coin
import com.example.cryptopulse.domain.model.UserPreferences
import com.example.cryptopulse.domain.repository.CoinRepository
import com.example.cryptopulse.domain.repository.UserPreferencesRepository
import com.example.cryptopulse.util.NetworkResult
import io.mockk.coEvery
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
class MarketsViewModelTest {

    private lateinit var viewModel: MarketsViewModel
    private val coinRepository: CoinRepository = mockk(relaxed = true)
    private val prefsRepository: UserPreferencesRepository = mockk(relaxed = true)
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        every { prefsRepository.userPreferences } returns flowOf(UserPreferences(currency = "usd"))
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `setSortOrder updates state and fetches new data`() = runTest {
        coEvery { coinRepository.getMarketCoins(any(), any(), any(), any(), any()) } returns flowOf(NetworkResult.Success(emptyList()))
        
        viewModel = MarketsViewModel(coinRepository, prefsRepository)
        
        testDispatcher.scheduler.advanceUntilIdle()
        
        viewModel.setSortOrder("price_desc")
        
        viewModel.uiState.test {
            val state = expectMostRecentItem()
            assertEquals("price_desc", state.sortOrder)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
