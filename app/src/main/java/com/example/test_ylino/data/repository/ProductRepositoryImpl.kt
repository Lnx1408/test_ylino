package com.example.test_ylino.data.repository

import com.example.test_ylino.core.error.DomainError
import com.example.test_ylino.core.result.ResultState
import com.example.test_ylino.data.mapper.toDomain
import com.example.test_ylino.data.remote.mock.MockProductApiService
import com.example.test_ylino.domain.model.Product
import com.example.test_ylino.domain.repository.ProductRepository
import kotlinx.coroutines.withContext
import com.example.test_ylino.core.dispatcher.DispatcherProvider

class ProductRepositoryImpl(
    private val apiService: MockProductApiService,
    private val dispatcherProvider: DispatcherProvider
) : ProductRepository {

    override suspend fun getProducts(): ResultState<List<Product>> = withContext(dispatcherProvider.io) {
        try {
            val response = apiService.fetchProducts()
            if (response.isEmpty()) {
                ResultState.Failure(DomainError.EmptyData)
            } else {
                ResultState.Success(response.map { it.toDomain() })
            }
        } catch (_: Exception) {
            ResultState.Failure(DomainError.Network)
        }
    }

    override suspend fun getProductDetail(id: String): ResultState<Product> = withContext(dispatcherProvider.io) {
        try {
            val response = apiService.fetchProductDetail(id)
            ResultState.Success(response.toDomain())
        } catch (e: Exception) {
            ResultState.Failure(DomainError.Unknown(e.message ?: "Unknown error"))
        }
    }
}
