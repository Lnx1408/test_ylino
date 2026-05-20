package com.example.test_ylino.core.result

import com.example.test_ylino.core.error.DomainError

sealed interface ResultState<out T> {
    data class Success<T>(val data: T) : ResultState<T>
    data class Failure(val error: DomainError) : ResultState<Nothing>
}
