package com.example.cryptopulse.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.cryptopulse.data.local.entity.FavoriteCoinEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteCoinDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(favorite: FavoriteCoinEntity)

    @Query("DELETE FROM favorite_coins WHERE coinId = :coinId")
    suspend fun removeFavorite(coinId: String)

    @Query("SELECT * FROM favorite_coins ORDER BY addedAt DESC")
    fun getAllFavorites(): Flow<List<FavoriteCoinEntity>>

    @Query("SELECT coinId FROM favorite_coins")
    fun getAllFavoriteIds(): Flow<List<String>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_coins WHERE coinId = :coinId)")
    fun isFavorite(coinId: String): Flow<Boolean>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_coins WHERE coinId = :coinId)")
    suspend fun isFavoriteSync(coinId: String): Boolean

    @Query("SELECT COUNT(*) FROM favorite_coins")
    fun getFavoriteCount(): Flow<Int>
}
