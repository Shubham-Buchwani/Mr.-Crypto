package com.example.cryptopulse.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.cryptopulse.domain.model.Coin
import com.example.cryptopulse.domain.model.UserPreferences
import com.example.cryptopulse.domain.repository.CoinRepository
import com.example.cryptopulse.domain.repository.UserPreferencesRepository
import com.example.cryptopulse.util.NetworkResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class HomeUiState(
    val isLoading: Boolean = true,
    val popularCoins: List<Coin> = emptyList(),
    val topGainers: List<Coin> = emptyList(),
    val topLosers: List<Coin> = emptyList(),
    val favoriteCoins: List<Coin> = emptyList(),
    val totalMarketCap: Double? = null,
    val errorMessage: String? = null,
    val lastUpdated: Long? = null,
    val isOffline: Boolean = false,
    val currency: String = "usd"
)

class HomeViewModel(
    private val coinRepository: CoinRepository,
    private val prefsRepository: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            prefsRepository.userPreferences.collect { prefs ->
                _uiState.value = _uiState.value.copy(currency = prefs.currency)
                loadData(prefs.currency)
            }
        }
        observeFavorites()
    }

    private fun observeFavorites() {
        viewModelScope.launch {
            val currency = prefsRepository.userPreferences.first().currency
            coinRepository.getFavoriteCoins(currency).collect { favorites ->
                _uiState.value = _uiState.value.copy(favoriteCoins = favorites)
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            val currency = _uiState.value.currency
            loadData(currency, forceRefresh = true)
        }
    }

    private suspend fun loadData(currency: String, forceRefresh: Boolean = false) {
        coinRepository.getMarketCoins(
            currency = currency,
            perPage = 50,
            forceRefresh = forceRefresh
        ).collect { result ->
            when (result) {
                is NetworkResult.Loading -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = result.data.isNullOrEmpty(),
                        errorMessage = null
                    )
                    result.data?.let { processCoins(it, currency) }
                }
                is NetworkResult.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = null,
                        isOffline = false,
                        lastUpdated = System.currentTimeMillis()
                    )
                    processCoins(result.data, currency)
                }
                is NetworkResult.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = if (result.data.isNullOrEmpty()) result.message else null,
                        isOffline = result.data != null
                    )
                    result.data?.let { processCoins(it, currency) }
                    if (result.data != null) {
                        _uiState.value = _uiState.value.copy(
                            lastUpdated = coinRepository.getLastUpdatedTime(currency)
                        )
                    }
                }
            }
        }
    }

    private fun processCoins(coins: List<Coin>, currency: String) {
        val popular = coins.take(5)
        val sorted = coins.sortedByDescending { it.priceChangePercentage24h }
        val gainers = sorted.take(5)
        val losers = sorted.takeLast(5).reversed()
        val totalMcap = coins.sumOf { it.marketCap }

        _uiState.value = _uiState.value.copy(
            popularCoins = popular,
            topGainers = gainers,
            topLosers = losers,
            totalMarketCap = totalMcap
        )
    }

    class Factory(
        private val coinRepository: CoinRepository,
        private val prefsRepository: UserPreferencesRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return HomeViewModel(coinRepository, prefsRepository) as T
        }
    }
}
