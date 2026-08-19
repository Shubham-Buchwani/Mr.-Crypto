package com.example.cryptopulse.domain.model

data class SearchResult(
    val id: String,
    val name: String,
    val symbol: String,
    val marketCapRank: Int?,
    val thumbUrl: String,
    val largeUrl: String
)
