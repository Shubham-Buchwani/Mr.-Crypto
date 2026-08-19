package com.example.cryptopulse.presentation.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Markets : Screen("markets")
    data object Favorites : Screen("favorites")
    data object Settings : Screen("settings")
    data object Search : Screen("search")
    data object CoinDetail : Screen("coin_detail/{coinId}") {
        fun createRoute(coinId: String): String = "coin_detail/$coinId"
    }
}
