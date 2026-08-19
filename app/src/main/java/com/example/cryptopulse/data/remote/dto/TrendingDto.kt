package com.example.cryptopulse.data.remote.dto

import com.google.gson.annotations.SerializedName

data class TrendingResponseDto(
    @SerializedName("coins") val coins: List<TrendingCoinItemDto>?
)

data class TrendingCoinItemDto(
    @SerializedName("item") val item: SearchCoinDto?
)
