package com.example.test_ylino.domain.usecase

import com.example.test_ylino.core.result.ResultState
import com.example.test_ylino.domain.model.CryptoAsset
import com.example.test_ylino.domain.repository.WalletRepository

class GetWalletItemsUseCase(private val repository: WalletRepository) {
    suspend operator fun invoke(): ResultState<List<CryptoAsset>> {
        return repository.getWalletItems()
    }
}
