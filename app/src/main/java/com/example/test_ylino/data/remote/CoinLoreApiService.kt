package com.example.test_ylino.data.remote

import com.example.test_ylino.data.remote.dto.CryptoDetailDto
import retrofit2.http.GET
import retrofit2.http.Query

interface CoinLoreApiService {
    @GET("api/ticker/")
    suspend fun getCryptoDetail(@Query("id") id: String): List<CryptoDetailDto>
}
