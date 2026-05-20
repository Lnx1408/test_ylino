package com.example.test_ylino.domain.usecase

import com.example.test_ylino.core.result.ResultState
import com.example.test_ylino.domain.model.Product
import com.example.test_ylino.domain.repository.ProductRepository

class GetProductsUseCase(private val repository: ProductRepository) {
    suspend operator fun invoke(): ResultState<List<Product>> {
        return repository.getProducts()
    }
}
