package com.example.cryptopulse.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.cryptopulse.domain.model.SearchResult
import com.example.cryptopulse.domain.repository.CoinRepository
import com.example.cryptopulse.util.NetworkResult
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class SearchUiState(
    val query: String = "",
    val isLoading: Boolean = false,
    val results: List<SearchResult> = emptyList(),
    val trendingCoins: List<SearchResult> = emptyList(),
    val errorMessage: String? = null,
    val hasSearched: Boolean = false
)

class SearchViewModel(
    private val coinRepository: CoinRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null

    init {
        fetchTrending()
    }

    private fun fetchTrending() {
        viewModelScope.launch {
            when (val result = coinRepository.getTrendingCoins()) {
                is NetworkResult.Success -> {
                    _uiState.value = _uiState.value.copy(trendingCoins = result.data)
                }
                else -> { /* Ignore errors for trending */ }
            }
        }
    }

    fun onQueryChange(query: String) {
        _uiState.value = _uiState.value.copy(query = query)
        searchJob?.cancel()

        if (query.isBlank()) {
            _uiState.value = _uiState.value.copy(
                results = emptyList(),
                errorMessage = null,
                hasSearched = false,
                isLoading = false
            )
            return
        }

        searchJob = viewModelScope.launch {
            delay(500) // debounce
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            when (val result = coinRepository.searchCoins(query)) {
                is NetworkResult.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        results = result.data,
                        hasSearched = true,
                        errorMessage = null
                    )
                }
                is NetworkResult.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = result.message,
                        hasSearched = true
                    )
                }
                is NetworkResult.Loading -> {
                    _uiState.value = _uiState.value.copy(isLoading = true)
                }
            }
        }
    }

    fun clearSearch() {
        _uiState.value = _uiState.value.copy(
            query = "",
            results = emptyList(),
            errorMessage = null,
            hasSearched = false,
            isLoading = false
        )
        searchJob?.cancel()
    }

    class Factory(
        private val coinRepository: CoinRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return SearchViewModel(coinRepository) as T
        }
    }
}
