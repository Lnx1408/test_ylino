package com.example.test_ylino.domain.repository

import com.example.test_ylino.core.result.ResultState
import com.example.test_ylino.domain.model.Product

interface ProductRepository {
    suspend fun getProducts(): ResultState<List<Product>>
    suspend fun getProductDetail(id: String): ResultState<Product>
}
