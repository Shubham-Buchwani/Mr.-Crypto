package com.example.cryptopulse.data.remote.api

import com.example.cryptopulse.BuildConfig

object ApiConfig {
    const val BASE_URL = "https://api.coingecko.com/api/v3/"
    const val CONNECT_TIMEOUT_SECONDS = 30L
    const val READ_TIMEOUT_SECONDS = 30L
    const val WRITE_TIMEOUT_SECONDS = 30L

    val apiKey: String
        get() = BuildConfig.COINGECKO_API_KEY

    val hasApiKey: Boolean
        get() = apiKey.isNotBlank() && apiKey != "YOUR_KEY_HERE"
}
