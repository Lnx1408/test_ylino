package com.example.test_ylino.presentation.wallet

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.test_ylino.core.ui.UiEvent
import com.example.test_ylino.core.ui.UiState
import com.example.test_ylino.domain.model.CryptoAsset
import com.example.test_ylino.presentation.components.ErrorView
import com.example.test_ylino.presentation.components.LoadingView
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WalletScreen(
    viewModel: WalletViewModel,
    onNavigate: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collectLatest { event ->
            when (event) {
                is UiEvent.Navigate -> onNavigate(event.route)
                else -> {}
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Mi Cartera Crypto") })
        }
    ) { innerPadding ->
        when (val state = uiState) {
            is UiState.Loading -> LoadingView(modifier = Modifier.padding(innerPadding))
            is UiState.Error -> ErrorView(
                error = state.error,
                onRetry = { viewModel.loadWalletItems() },
                modifier = Modifier.padding(innerPadding)
            )
            is UiState.Success -> CryptoList(
                items = state.data,
                onItemClick = { viewModel.onCryptoClick(it) },
                modifier = Modifier.padding(innerPadding)
            )
            UiState.Idle -> {}
        }
    }
}

@Composable
fun CryptoList(
    items: List<CryptoAsset>,
    onItemClick: (CryptoAsset) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize().padding(horizontal = 16.dp)
    ) {
        items(items) { item ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clickable { onItemClick(item) },
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Text(
                    text = item.name,
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}
