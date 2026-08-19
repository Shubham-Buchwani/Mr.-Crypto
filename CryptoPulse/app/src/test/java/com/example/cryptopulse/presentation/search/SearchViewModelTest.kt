package com.example.cryptopulse.presentation.search

import app.cash.turbine.test
import com.example.cryptopulse.domain.model.SearchResult
import com.example.cryptopulse.domain.repository.CoinRepository
import com.example.cryptopulse.util.NetworkResult
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {

    private lateinit var viewModel: SearchViewModel
    private val coinRepository: CoinRepository = mockk(relaxed = true)
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = SearchViewModel(coinRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onQueryChange with blank query clears results`() = runTest {
        viewModel.onQueryChange("")
        
        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals("", state.query)
            assertEquals(true, state.results.isEmpty())
            assertEquals(false, state.hasSearched)
        }
    }

    @Test
    fun `onQueryChange debounces and performs search`() = runTest {
        val searchResult = SearchResult("bitcoin", "Bitcoin", "BTC", 1, "", "")
        coEvery { coinRepository.searchCoins(any()) } returns NetworkResult.Success(listOf(searchResult))
        
        viewModel.onQueryChange("bit")
        
        viewModel.uiState.test {
            // Initial state (query updated)
            val initialState = awaitItem()
            assertEquals("bit", initialState.query)
            
            // Fast forward time to pass the debounce delay
            testDispatcher.scheduler.advanceTimeBy(600)
            
            val finalState = expectMostRecentItem()
            assertEquals(1, finalState.results.size)
            assertEquals("bitcoin", finalState.results.first().id)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
