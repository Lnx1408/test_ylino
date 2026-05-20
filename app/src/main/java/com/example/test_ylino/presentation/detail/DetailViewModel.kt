package com.example.test_ylino.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.test_ylino.core.result.ResultState
import com.example.test_ylino.core.ui.UiState
import com.example.test_ylino.domain.model.Product
import com.example.test_ylino.domain.usecase.GetProductDetailUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailViewModel(
    private val getProductDetailUseCase: GetProductDetailUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<Product>>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    fun loadProductDetail(id: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            when (val result = getProductDetailUseCase(id)) {
                is ResultState.Success -> {
                    _uiState.value = UiState.Success(result.data)
                }
                is ResultState.Failure -> {
                    _uiState.value = UiState.Error(result.error)
                }
            }
        }
    }
}
