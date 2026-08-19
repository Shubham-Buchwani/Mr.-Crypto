package com.example.cryptopulse.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.cryptopulse.data.local.entity.CoinMarketEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CoinMarketDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(coins: List<CoinMarketEntity>)

    @Query("SELECT * FROM coin_markets WHERE currency = :currency ORDER BY marketCapRank ASC")
    fun getAllCoins(currency: String = "usd"): Flow<List<CoinMarketEntity>>

    @Query("SELECT * FROM coin_markets WHERE currency = :currency ORDER BY marketCapRank ASC")
    suspend fun getAllCoinsList(currency: String = "usd"): List<CoinMarketEntity>

    @Query("SELECT * FROM coin_markets WHERE id IN (:ids) AND currency = :currency")
    fun getCoinsByIds(ids: List<String>, currency: String = "usd"): Flow<List<CoinMarketEntity>>

    @Query("SELECT * FROM coin_markets WHERE id IN (:ids) AND currency = :currency")
    suspend fun getCoinsByIdsList(ids: List<String>, currency: String = "usd"): List<CoinMarketEntity>

    @Query("SELECT MAX(lastUpdated) FROM coin_markets WHERE currency = :currency")
    suspend fun getLastUpdatedTime(currency: String = "usd"): Long?

    @Query("DELETE FROM coin_markets WHERE currency = :currency")
    suspend fun deleteAll(currency: String = "usd")

    @Query("SELECT COUNT(*) FROM coin_markets WHERE currency = :currency")
    suspend fun getCount(currency: String = "usd"): Int
}
