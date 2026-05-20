package com.example.test_ylino.domain.repository

import com.example.test_ylino.core.result.ResultState
import com.example.test_ylino.domain.model.CryptoAsset
import com.example.test_ylino.domain.model.CryptoDetail

interface WalletRepository {
    suspend fun getWalletItems(): ResultState<List<CryptoAsset>>
    suspend fun getCryptoDetail(id: String): ResultState<CryptoDetail>
}
