package com.example.cryptopulse.data.remote.api

import okhttp3.Interceptor
import okhttp3.Response

class ApiKeyInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val newRequest = if (ApiConfig.hasApiKey) {
            // Send API key via BOTH header AND query parameter for maximum compatibility
            // CoinGecko Demo API accepts both methods
            val newUrl = originalRequest.url.newBuilder()
                .addQueryParameter("x_cg_demo_api_key", ApiConfig.apiKey)
                .build()
            originalRequest.newBuilder()
                .url(newUrl)
                .header("x-cg-demo-api-key", ApiConfig.apiKey)
                .build()
        } else {
            originalRequest
        }

        return chain.proceed(newRequest)
    }
}
