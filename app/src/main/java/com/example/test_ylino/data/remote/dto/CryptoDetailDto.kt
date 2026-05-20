package com.example.test_ylino.data.remote.dto

import com.google.gson.annotations.SerializedName

data class CryptoDetailDto(
    @SerializedName("id") val id: String,
    @SerializedName("symbol") val symbol: String,
    @SerializedName("name") val name: String,
    @SerializedName("price_usd") val priceUsd: String,
    @SerializedName("percent_change_24h") val percentChange24h: String,
    @SerializedName("market_cap_usd") val marketCapUsd: String
)
