package com.example.test_ylino.data.repository

import com.example.test_ylino.core.dispatcher.DispatcherProvider
import com.example.test_ylino.core.result.ResultState
import com.example.test_ylino.data.remote.mock.MockProductApiService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProductRepositoryImplTest {

    private lateinit var repository: ProductRepositoryImpl
    private val testDispatcher = StandardTestDispatcher()
    
    private val dispatcherProvider = object : DispatcherProvider {
        override val main = testDispatcher
        override val io = testDispatcher
        override val default = testDispatcher
    }

    @Test
    fun `getProducts returns Success when API succeeds`() = runTest(testDispatcher) {
        // Arrange
        val apiService = MockProductApiService(shouldFail = false)
        repository = ProductRepositoryImpl(apiService, dispatcherProvider)

        // Act
        val result = repository.getProducts()

        // Assert
        assertTrue(result is ResultState.Success)
    }

    @Test
    fun `getProducts returns Failure when API fails`() = runTest(testDispatcher) {
        // Arrange
        val apiService = MockProductApiService(shouldFail = true)
        repository = ProductRepositoryImpl(apiService, dispatcherProvider)

        // Act
        val result = repository.getProducts()

        // Assert
        assertTrue(result is ResultState.Failure)
    }
}
