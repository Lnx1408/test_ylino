package com.example.test_ylino.core.error

sealed class DomainError {
    data object Network : DomainError()
    data object Timeout : DomainError()
    data object Unauthorized : DomainError()
    data object EmptyData : DomainError()
    data class Unknown(val message: String) : DomainError()
}
