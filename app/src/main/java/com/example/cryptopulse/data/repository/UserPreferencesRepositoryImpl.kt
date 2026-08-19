package com.example.cryptopulse.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.cryptopulse.domain.model.UserPreferences
import com.example.cryptopulse.domain.repository.UserPreferencesRepository
import com.example.cryptopulse.presentation.theme.ThemeMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class UserPreferencesRepositoryImpl(
    private val context: Context
) : UserPreferencesRepository {

    private object Keys {
        val CURRENCY = stringPreferencesKey("currency")
        val THEME_MODE = stringPreferencesKey("theme_mode")
        val REFRESH_INTERVAL = intPreferencesKey("refresh_interval_minutes")
    }

    override val userPreferences: Flow<UserPreferences> = context.dataStore.data.map { prefs ->
        UserPreferences(
            currency = prefs[Keys.CURRENCY] ?: "usd",
            themeMode = prefs[Keys.THEME_MODE]?.let { ThemeMode.valueOf(it) } ?: ThemeMode.SYSTEM,
            refreshIntervalMinutes = prefs[Keys.REFRESH_INTERVAL] ?: 5
        )
    }

    override suspend fun setCurrency(currency: String) {
        context.dataStore.edit { prefs ->
            prefs[Keys.CURRENCY] = currency
        }
    }

    override suspend fun setThemeMode(themeMode: ThemeMode) {
        context.dataStore.edit { prefs ->
            prefs[Keys.THEME_MODE] = themeMode.name
        }
    }

    override suspend fun setRefreshInterval(minutes: Int) {
        context.dataStore.edit { prefs ->
            prefs[Keys.REFRESH_INTERVAL] = minutes
        }
    }
}
