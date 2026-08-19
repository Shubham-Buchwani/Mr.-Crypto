package com.example.cryptopulse

import android.app.Application
import com.example.cryptopulse.data.local.CryptoPulseDatabase
import com.example.cryptopulse.data.remote.api.ApiConfig
import com.example.cryptopulse.data.remote.api.ApiKeyInterceptor
import com.example.cryptopulse.data.remote.api.CoinGeckoApi
import com.example.cryptopulse.data.repository.CoinRepositoryImpl
import com.example.cryptopulse.data.repository.UserPreferencesRepositoryImpl
import com.example.cryptopulse.domain.repository.CoinRepository
import com.example.cryptopulse.domain.repository.UserPreferencesRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class CryptoPulseApp : Application() {

    lateinit var coinRepository: CoinRepository
        private set
    lateinit var userPreferencesRepository: UserPreferencesRepository
        private set

    override fun onCreate() {
        super.onCreate()
        instance = this

        // OkHttp
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BASIC
                    else HttpLoggingInterceptor.Level.NONE
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(ApiKeyInterceptor())
            .addInterceptor(loggingInterceptor)
            .connectTimeout(ApiConfig.CONNECT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(ApiConfig.READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(ApiConfig.WRITE_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .build()

        // Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl(ApiConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(CoinGeckoApi::class.java)

        // Database
        val database = CryptoPulseDatabase.getInstance(this)

        // Repositories
        coinRepository = CoinRepositoryImpl(
            api = api,
            coinMarketDao = database.coinMarketDao(),
            favoriteCoinDao = database.favoriteCoinDao()
        )

        userPreferencesRepository = UserPreferencesRepositoryImpl(this)
    }

    companion object {
        lateinit var instance: CryptoPulseApp
            private set
    }
}
