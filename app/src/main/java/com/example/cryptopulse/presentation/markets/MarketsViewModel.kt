package com.example.cryptopulse.presentation.markets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.cryptopulse.domain.model.Coin
import com.example.cryptopulse.domain.repository.CoinRepository
import com.example.cryptopulse.domain.repository.UserPreferencesRepository
import com.example.cryptopulse.util.NetworkResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class MarketsUiState(
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val coins: List<Coin> = emptyList(),
    val errorMessage: String? = null,
    val currency: String = "usd",
    val sortOrder: String = "market_cap_desc",
    val isOffline: Boolean = false,
    val lastUpdated: Long? = null
)

class MarketsViewModel(
    private val coinRepository: CoinRepository,
    private val prefsRepository: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MarketsUiState())
    val uiState: StateFlow<MarketsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val prefs = prefsRepository.userPreferences.first()
            _uiState.value = _uiState.value.copy(currency = prefs.currency)
            loadMarkets()
        }
        viewModelScope.launch {
            prefsRepository.userPreferences.collect { prefs ->
                if (prefs.currency != _uiState.value.currency) {
                    _uiState.value = _uiState.value.copy(currency = prefs.currency)
                    loadMarkets(forceRefresh = true)
                }
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isRefreshing = true)
            loadMarkets(forceRefresh = true)
            _uiState.value = _uiState.value.copy(isRefreshing = false)
        }
    }

    fun setSortOrder(order: String) {
        _uiState.value = _uiState.value.copy(sortOrder = order)
        viewModelScope.launch { loadMarkets(forceRefresh = true) }
    }

    private suspend fun loadMarkets(forceRefresh: Boolean = false) {
        coinRepository.getMarketCoins(
            currency = _uiState.value.currency,
            order = _uiState.value.sortOrder,
            perPage = 250,
            forceRefresh = forceRefresh
        ).collect { result ->
            when (result) {
                is NetworkResult.Loading -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = result.data.isNullOrEmpty(),
                        errorMessage = null
                    )
                    result.data?.let { _uiState.value = _uiState.value.copy(coins = it) }
                }
                is NetworkResult.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        coins = result.data,
                        errorMessage = null,
                        isOffline = false,
                        lastUpdated = System.currentTimeMillis()
                    )
                }
                is NetworkResult.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = if (result.data.isNullOrEmpty()) result.message else null,
                        isOffline = result.data != null
                    )
                    result.data?.let { _uiState.value = _uiState.value.copy(coins = it) }
                }
            }
        }
    }

    class Factory(
        private val coinRepository: CoinRepository,
        private val prefsRepository: UserPreferencesRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MarketsViewModel(coinRepository, prefsRepository) as T
        }
    }
}
