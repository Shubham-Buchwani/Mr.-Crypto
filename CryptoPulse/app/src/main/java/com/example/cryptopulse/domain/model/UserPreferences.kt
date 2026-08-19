package com.example.cryptopulse.domain.model

import com.example.cryptopulse.presentation.theme.ThemeMode

data class UserPreferences(
    val currency: String = "usd",
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val refreshIntervalMinutes: Int = 5
) {
    companion object {
        val SUPPORTED_CURRENCIES = listOf(
            "usd" to "USD ($)",
            "eur" to "EUR (€)",
            "gbp" to "GBP (£)",
            "inr" to "INR (₹)"
        )

        val REFRESH_INTERVALS = listOf(
            1 to "1 minute",
            5 to "5 minutes",
            15 to "15 minutes",
            30 to "30 minutes",
            60 to "1 hour"
        )
    }
}
