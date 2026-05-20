package com.example.test_ylino.presentation.home

import com.example.test_ylino.core.result.ResultState
import com.example.test_ylino.core.ui.UiState
import com.example.test_ylino.domain.model.Product
import com.example.test_ylino.domain.repository.ProductRepository
import com.example.test_ylino.domain.usecase.GetProductsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is Loading and then Success`() = runTest {
        // Arrange
        val products = listOf(Product("1", "Test", "Desc", 100.0, "EUR"))
        val fakeRepository = object : ProductRepository {
            override suspend fun getProducts() = ResultState.Success(products)
            override suspend fun getProductDetail(id: String) = ResultState.Success(products[0])
        }
        val getProductsUseCase = GetProductsUseCase(fakeRepository)
        
        // Act
        val viewModel = HomeViewModel(getProductsUseCase)
        
        // Assert
        assertEquals(UiState.Loading, viewModel.uiState.value)
        advanceUntilIdle()
        assertTrue(viewModel.uiState.value is UiState.Success)
        assertEquals(products, (viewModel.uiState.value as UiState.Success).data)
    }

    private fun assertTrue(condition: Boolean) {
        assert(condition)
    }
}
