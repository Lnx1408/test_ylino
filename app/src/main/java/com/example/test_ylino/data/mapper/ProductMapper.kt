package com.example.test_ylino.data.mapper

import com.example.test_ylino.data.remote.dto.ProductDto
import com.example.test_ylino.domain.model.Product

fun ProductDto.toDomain(): Product {
    return Product(
        id = productId,
        name = productName,
        description = productDescription,
        balance = productBalance,
        currency = productCurrency
    )
}
