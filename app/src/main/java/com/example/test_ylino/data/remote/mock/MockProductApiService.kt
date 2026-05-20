package com.example.test_ylino.data.remote.mock

import com.example.test_ylino.data.remote.dto.ProductDto
import kotlinx.coroutines.delay

class MockProductApiService(
    private val shouldFail: Boolean = false
) {
    suspend fun fetchProducts(): List<ProductDto> {
        delay(1500)
        if (shouldFail) throw Exception("Network error")
        return listOf(
            ProductDto("1", "Cuenta Corriente", "Cuenta para gastos diarios", 1500.50, "EUR"),
            ProductDto("2", "Tarjeta de Crédito Platinum", "Tarjeta con beneficios exclusivos", -450.20, "EUR"),
            ProductDto("3", "Préstamo Personal", "Préstamo para reformas", 25000.00, "EUR"),
            ProductDto("4", "Cuenta de Ahorros", "Ahorros para el futuro", 5000.00, "EUR")
        )
    }

    suspend fun fetchProductDetail(id: String): ProductDto {
        delay(1000)
        if (shouldFail) throw Exception("Network error")
        return fetchProducts().find { it.productId == id } 
            ?: throw Exception("Product not found")
    }
}
