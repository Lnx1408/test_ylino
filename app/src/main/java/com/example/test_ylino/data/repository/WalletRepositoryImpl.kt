package com.example.test_ylino.data.repository

import com.example.test_ylino.core.dispatcher.DispatcherProvider
import com.example.test_ylino.core.error.DomainError
import com.example.test_ylino.core.result.ResultState
import com.example.test_ylino.data.remote.CoinLoreApiService
import com.example.test_ylino.domain.model.CryptoAsset
import com.example.test_ylino.domain.model.CryptoDetail
import com.example.test_ylino.domain.repository.WalletRepository
import kotlinx.coroutines.withContext

class WalletRepositoryImpl(
    private val apiService: CoinLoreApiService,
    private val dispatcherProvider: DispatcherProvider
) : WalletRepository {

    override suspend fun getWalletItems(): ResultState<List<CryptoAsset>> = withContext(dispatcherProvider.io) {
        try {
            // Simulated JSON response
            val items = listOf(
                CryptoAsset("89", "Stellar"),
                CryptoAsset("90", "Bitcoin"),
                CryptoAsset("91", "ClubCoin")
            )
            ResultState.Success(items)
        } catch (e: Exception) {
            ResultState.Failure(DomainError.Unknown(e.message ?: "Error loading wallet"))
        }
    }

    override suspend fun getCryptoDetail(id: String): ResultState<CryptoDetail> = withContext(dispatcherProvider.io) {
        try {
            val response = apiService.getCryptoDetail(id)
            if (response.isNotEmpty()) {
                val dto = response[0]
                ResultState.Success(
                    CryptoDetail(
                        id = dto.id,
                        symbol = dto.symbol,
                        name = dto.name,
                        priceUsd = dto.priceUsd,
                        percentChange24h = dto.percentChange24h,
                        marketCapUsd = dto.marketCapUsd
                    )
                )
            } else {
                ResultState.Failure(DomainError.EmptyData)
            }
        } catch (e: Exception) {
            ResultState.Failure(DomainError.Network)
        }
    }
}
