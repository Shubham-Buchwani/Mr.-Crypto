package com.example.cryptopulse.presentation.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.cryptopulse.CryptoPulseApp
import com.example.cryptopulse.presentation.coin.CoinDetailScreen
import com.example.cryptopulse.presentation.coin.CoinDetailViewModel
import com.example.cryptopulse.presentation.favorites.FavoritesScreen
import com.example.cryptopulse.presentation.favorites.FavoritesViewModel
import com.example.cryptopulse.presentation.home.HomeScreen
import com.example.cryptopulse.presentation.home.HomeViewModel
import com.example.cryptopulse.presentation.markets.MarketsScreen
import com.example.cryptopulse.presentation.markets.MarketsViewModel
import com.example.cryptopulse.presentation.search.SearchScreen
import com.example.cryptopulse.presentation.search.SearchViewModel
import com.example.cryptopulse.presentation.settings.SettingsScreen
import com.example.cryptopulse.presentation.settings.SettingsViewModel

private const val ANIM_DURATION = 300

@Composable
fun CryptoPulseNavigation(navController: NavHostController) {
    val app = CryptoPulseApp.instance

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        enterTransition = { fadeIn(tween(ANIM_DURATION)) },
        exitTransition = { fadeOut(tween(ANIM_DURATION)) },
        popEnterTransition = { fadeIn(tween(ANIM_DURATION)) },
        popExitTransition = { fadeOut(tween(ANIM_DURATION)) }
    ) {
        composable(Screen.Home.route) {
            val viewModel: HomeViewModel = viewModel(
                factory = HomeViewModel.Factory(app.coinRepository, app.userPreferencesRepository)
            )
            HomeScreen(
                viewModel = viewModel,
                onNavigateToSearch = { navController.navigate(Screen.Search.route) },
                onNavigateToMarkets = {
                    navController.navigate(Screen.Markets.route) {
                        popUpTo(Screen.Home.route) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onNavigateToCoinDetail = { coinId ->
                    navController.navigate(Screen.CoinDetail.createRoute(coinId))
                }
            )
        }

        composable(Screen.Markets.route) {
            val viewModel: MarketsViewModel = viewModel(
                factory = MarketsViewModel.Factory(app.coinRepository, app.userPreferencesRepository)
            )
            MarketsScreen(
                viewModel = viewModel,
                onNavigateToSearch = { navController.navigate(Screen.Search.route) },
                onNavigateToCoinDetail = { coinId ->
                    navController.navigate(Screen.CoinDetail.createRoute(coinId))
                }
            )
        }

        composable(Screen.Favorites.route) {
            val viewModel: FavoritesViewModel = viewModel(
                factory = FavoritesViewModel.Factory(app.coinRepository, app.userPreferencesRepository)
            )
            FavoritesScreen(
                viewModel = viewModel,
                onNavigateToCoinDetail = { coinId ->
                    navController.navigate(Screen.CoinDetail.createRoute(coinId))
                },
                onNavigateToMarkets = {
                    navController.navigate(Screen.Markets.route) {
                        popUpTo(Screen.Home.route) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }

        composable(Screen.Settings.route) {
            val viewModel: SettingsViewModel = viewModel(
                factory = SettingsViewModel.Factory(app.userPreferencesRepository)
            )
            SettingsScreen(viewModel = viewModel)
        }

        composable(
            route = Screen.Search.route,
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Up, tween(ANIM_DURATION))
            },
            exitTransition = { fadeOut(tween(ANIM_DURATION)) },
            popExitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Down, tween(ANIM_DURATION))
            }
        ) {
            val viewModel: SearchViewModel = viewModel(
                factory = SearchViewModel.Factory(app.coinRepository)
            )
            SearchScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToCoinDetail = { coinId ->
                    navController.navigate(Screen.CoinDetail.createRoute(coinId))
                }
            )
        }

        composable(
            route = Screen.CoinDetail.route,
            arguments = listOf(navArgument("coinId") { type = NavType.StringType }),
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(ANIM_DURATION))
            },
            popExitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(ANIM_DURATION))
            }
        ) { backStackEntry ->
            val coinId = backStackEntry.arguments?.getString("coinId") ?: return@composable
            val viewModel: CoinDetailViewModel = viewModel(
                factory = CoinDetailViewModel.Factory(coinId, app.coinRepository, app.userPreferencesRepository)
            )
            CoinDetailScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
