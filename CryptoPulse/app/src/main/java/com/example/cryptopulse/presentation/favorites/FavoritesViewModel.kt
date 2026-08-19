package com.example.cryptopulse.presentation.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.cryptopulse.domain.model.Coin
import com.example.cryptopulse.domain.repository.CoinRepository
import com.example.cryptopulse.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class FavoritesUiState(
    val isLoading: Boolean = true,
    val favorites: List<Coin> = emptyList(),
    val currency: String = "usd"
)

class FavoritesViewModel(
    private val coinRepository: CoinRepository,
    private val prefsRepository: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FavoritesUiState())
    val uiState: StateFlow<FavoritesUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val prefs = prefsRepository.userPreferences.first()
            _uiState.value = _uiState.value.copy(currency = prefs.currency)
            coinRepository.getFavoriteCoins(prefs.currency).collect { coins ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    favorites = coins
                )
            }
        }
        viewModelScope.launch {
            prefsRepository.userPreferences.collect { prefs ->
                _uiState.value = _uiState.value.copy(currency = prefs.currency)
            }
        }
    }

    fun removeFavorite(coinId: String) {
        viewModelScope.launch {
            coinRepository.toggleFavorite(coinId)
        }
    }

    class Factory(
        private val coinRepository: CoinRepository,
        private val prefsRepository: UserPreferencesRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return FavoritesViewModel(coinRepository, prefsRepository) as T
        }
    }
}
