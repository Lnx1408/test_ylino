package com.example.test_ylino.presentation.wallet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.test_ylino.core.result.ResultState
import com.example.test_ylino.core.ui.UiState
import com.example.test_ylino.domain.model.CryptoAsset
import com.example.test_ylino.domain.model.CryptoDetail
import com.example.test_ylino.domain.usecase.GetCryptoDetailUseCase
import com.example.test_ylino.domain.usecase.GetWalletItemsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WalletViewModel(
    private val getWalletItemsUseCase: GetWalletItemsUseCase,
    private val getCryptoDetailUseCase: GetCryptoDetailUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<CryptoAsset>>>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _detailState = MutableStateFlow<UiState<CryptoDetail?>>(UiState.Idle)
    val detailState = _detailState.asStateFlow()

    private val _selectedAsset = MutableStateFlow<CryptoAsset?>(null)
    val selectedAsset = _selectedAsset.asStateFlow()

    init {
        loadWalletItems()
    }

    fun loadWalletItems() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            when (val result = getWalletItemsUseCase()) {
                is ResultState.Success -> {
                    _uiState.value = UiState.Success(result.data)
                    if (result.data.isNotEmpty() && _selectedAsset.value == null) {
                        _selectedAsset.value = result.data[0]
                    }
                }
                is ResultState.Failure -> {
                    _uiState.value = UiState.Error(result.error)
                }
            }
        }
    }

    fun onAssetSelected(asset: CryptoAsset) {
        _selectedAsset.value = asset
    }

    fun onSearchClick() {
        val asset = _selectedAsset.value ?: return
        viewModelScope.launch {
            _detailState.value = UiState.Loading
            when (val result = getCryptoDetailUseCase(asset.id)) {
                is ResultState.Success -> {
                    _detailState.value = UiState.Success(result.data)
                }
                is ResultState.Failure -> {
                    _detailState.value = UiState.Error(result.error)
                }
            }
        }
    }
}
