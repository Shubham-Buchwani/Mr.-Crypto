package com.example.cryptopulse.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_coins")
data class FavoriteCoinEntity(
    @PrimaryKey val coinId: String,
    val addedAt: Long = System.currentTimeMillis()
)
