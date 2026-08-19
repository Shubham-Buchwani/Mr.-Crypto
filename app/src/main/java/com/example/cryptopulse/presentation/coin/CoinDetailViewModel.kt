package com.example.cryptopulse.presentation.coin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.cryptopulse.domain.model.CoinDetail
import com.example.cryptopulse.domain.model.CoinMarketChart
import com.example.cryptopulse.domain.repository.CoinRepository
import com.example.cryptopulse.domain.repository.UserPreferencesRepository
import com.example.cryptopulse.presentation.components.ChartTimeRange
import com.example.cryptopulse.presentation.components.chartTimeRanges
import com.example.cryptopulse.util.NetworkResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class CoinDetailUiState(
    val isLoading: Boolean = true,
    val coinDetail: CoinDetail? = null,
    val chartData: CoinMarketChart? = null,
    val isChartLoading: Boolean = true,
    val selectedTimeRange: ChartTimeRange = chartTimeRanges[1], // 7D default
    val isFavorite: Boolean = false,
    val errorMessage: String? = null,
    val chartError: String? = null,
    val currency: String = "usd"
)

class CoinDetailViewModel(
    private val coinId: String,
    private val coinRepository: CoinRepository,
    private val prefsRepository: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CoinDetailUiState())
    val uiState: StateFlow<CoinDetailUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val prefs = prefsRepository.userPreferences.first()
            _uiState.value = _uiState.value.copy(currency = prefs.currency)
            // Load detail first, then chart SEQUENTIALLY to avoid rate limits
            loadCoinDetail(prefs.currency)
            // Small delay to avoid hitting rate limit
            kotlinx.coroutines.delay(500)
            loadChartData(prefs.currency)
        }
        observeFavorite()
    }

    private fun observeFavorite() {
        viewModelScope.launch {
            coinRepository.isFavorite(coinId).collect { isFav ->
                _uiState.value = _uiState.value.copy(isFavorite = isFav)
            }
        }
    }

    fun toggleFavorite() {
        viewModelScope.launch {
            coinRepository.toggleFavorite(coinId)
        }
    }

    fun selectTimeRange(range: ChartTimeRange) {
        _uiState.value = _uiState.value.copy(selectedTimeRange = range)
        viewModelScope.launch {
            loadChartData(_uiState.value.currency)
        }
    }

    private suspend fun loadCoinDetail(currency: String) {
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
        when (val result = coinRepository.getCoinDetail(coinId, currency)) {
            is NetworkResult.Success -> {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    coinDetail = result.data,
                    errorMessage = null
                )
            }
            is NetworkResult.Error -> {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = result.message
                )
            }
            is NetworkResult.Loading -> {
                _uiState.value = _uiState.value.copy(isLoading = true)
            }
        }
    }

    private suspend fun loadChartData(currency: String) {
        _uiState.value = _uiState.value.copy(isChartLoading = true, chartError = null)
        when (val result = coinRepository.getMarketChart(coinId, currency, _uiState.value.selectedTimeRange.days)) {
            is NetworkResult.Success -> {
                _uiState.value = _uiState.value.copy(
                    isChartLoading = false,
                    chartData = result.data,
                    chartError = null
                )
            }
            is NetworkResult.Error -> {
                _uiState.value = _uiState.value.copy(
                    isChartLoading = false,
                    chartError = result.message
                )
            }
            is NetworkResult.Loading -> {
                _uiState.value = _uiState.value.copy(isChartLoading = true)
            }
        }
    }

    fun retry() {
        viewModelScope.launch {
            val currency = _uiState.value.currency
            loadCoinDetail(currency)
            kotlinx.coroutines.delay(500)
            loadChartData(currency)
        }
    }

    class Factory(
        private val coinId: String,
        private val coinRepository: CoinRepository,
        private val prefsRepository: UserPreferencesRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return CoinDetailViewModel(coinId, coinRepository, prefsRepository) as T
        }
    }
}
