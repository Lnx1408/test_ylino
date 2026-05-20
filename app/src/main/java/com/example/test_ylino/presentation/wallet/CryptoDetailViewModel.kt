package com.example.test_ylino.presentation.wallet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.test_ylino.core.result.ResultState
import com.example.test_ylino.core.ui.UiState
import com.example.test_ylino.domain.model.CryptoDetail
import com.example.test_ylino.domain.usecase.GetCryptoDetailUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CryptoDetailViewModel(
    private val getCryptoDetailUseCase: GetCryptoDetailUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<CryptoDetail>>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    fun loadCryptoDetail(id: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            when (val result = getCryptoDetailUseCase(id)) {
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
