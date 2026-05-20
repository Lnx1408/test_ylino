package com.example.test_ylino.presentation.wallet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.test_ylino.core.result.ResultState
import com.example.test_ylino.core.ui.UiEvent
import com.example.test_ylino.core.ui.UiState
import com.example.test_ylino.domain.model.CryptoAsset
import com.example.test_ylino.domain.usecase.GetWalletItemsUseCase
import com.example.test_ylino.presentation.navigation.Screen
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WalletViewModel(
    private val getWalletItemsUseCase: GetWalletItemsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<CryptoAsset>>>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    init {
        loadWalletItems()
    }

    fun loadWalletItems() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            when (val result = getWalletItemsUseCase()) {
                is ResultState.Success -> {
                    _uiState.value = UiState.Success(result.data)
                }
                is ResultState.Failure -> {
                    _uiState.value = UiState.Error(result.error)
                }
            }
        }
    }

    fun onCryptoClick(crypto: CryptoAsset) {
        viewModelScope.launch {
            _uiEvent.emit(UiEvent.Navigate(Screen.CryptoDetail.createRoute(crypto.id)))
        }
    }
}
