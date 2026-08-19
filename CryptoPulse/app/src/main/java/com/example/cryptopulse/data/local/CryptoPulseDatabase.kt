package com.example.cryptopulse.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.cryptopulse.data.local.dao.CoinMarketDao
import com.example.cryptopulse.data.local.dao.FavoriteCoinDao
import com.example.cryptopulse.data.local.entity.CoinMarketEntity
import com.example.cryptopulse.data.local.entity.FavoriteCoinEntity

@Database(
    entities = [CoinMarketEntity::class, FavoriteCoinEntity::class],
    version = 1,
    exportSchema = false
)
abstract class CryptoPulseDatabase : RoomDatabase() {
    abstract fun coinMarketDao(): CoinMarketDao
    abstract fun favoriteCoinDao(): FavoriteCoinDao

    companion object {
        @Volatile
        private var INSTANCE: CryptoPulseDatabase? = null

        fun getInstance(context: Context): CryptoPulseDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CryptoPulseDatabase::class.java,
                    "cryptopulse_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
