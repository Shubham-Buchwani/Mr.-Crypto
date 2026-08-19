package com.example.cryptopulse.domain.repository

import com.example.cryptopulse.domain.model.UserPreferences
import com.example.cryptopulse.presentation.theme.ThemeMode
import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    val userPreferences: Flow<UserPreferences>
    suspend fun setCurrency(currency: String)
    suspend fun setThemeMode(themeMode: ThemeMode)
    suspend fun setRefreshInterval(minutes: Int)
}
