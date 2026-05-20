package com.example.test_ylino.domain.model

data class CryptoAsset(
    val id: String,
    val name: String
)

data class CryptoDetail(
    val id: String,
    val symbol: String,
    val name: String,
    val priceUsd: String,
    val percentChange24h: String,
    val marketCapUsd: String
)
