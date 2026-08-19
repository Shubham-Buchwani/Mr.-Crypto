package com.example.cryptopulse

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.cryptopulse.presentation.navigation.BottomNavBar
import com.example.cryptopulse.presentation.navigation.CryptoPulseNavigation
import com.example.cryptopulse.presentation.navigation.Screen
import com.example.cryptopulse.presentation.navigation.bottomNavItems
import com.example.cryptopulse.presentation.theme.CryptoPulseTheme
import kotlinx.coroutines.flow.map

import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val app = application as CryptoPulseApp

        setContent {
            val themeMode by app.userPreferencesRepository.userPreferences
                .map { it.themeMode }
                .collectAsStateWithLifecycle(
                    initialValue = com.example.cryptopulse.presentation.theme.ThemeMode.SYSTEM
                )

            CryptoPulseTheme(themeMode = themeMode) {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                val showBottomBar = currentRoute in bottomNavItems.map { it.route }

                Scaffold(
                    bottomBar = {
                        if (showBottomBar) {
                            BottomNavBar(navController = navController)
                        }
                    },
                    containerColor = MaterialTheme.colorScheme.background
                ) { paddingValues ->
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = paddingValues.calculateBottomPadding()),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        CryptoPulseNavigation(navController = navController)
                    }
                }
            }
        }
    }
}
