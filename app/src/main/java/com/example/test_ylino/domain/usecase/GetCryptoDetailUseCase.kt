package com.example.test_ylino.domain.usecase

import com.example.test_ylino.core.result.ResultState
import com.example.test_ylino.domain.model.CryptoDetail
import com.example.test_ylino.domain.repository.WalletRepository

class GetCryptoDetailUseCase(private val repository: WalletRepository) {
    suspend operator fun invoke(id: String): ResultState<CryptoDetail> {
        return repository.getCryptoDetail(id)
    }
}
