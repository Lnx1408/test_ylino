package com.example.test_ylino.presentation.wallet

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.test_ylino.core.ui.UiState
import com.example.test_ylino.domain.model.CryptoDetail
import com.example.test_ylino.presentation.components.ErrorView
import com.example.test_ylino.presentation.components.LoadingView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CryptoDetailScreen(
    cryptoId: String,
    viewModel: CryptoDetailViewModel,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(cryptoId) {
        viewModel.loadCryptoDetail(cryptoId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle Crypto") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) { innerPadding ->
        when (val state = uiState) {
            is UiState.Loading -> LoadingView(modifier = Modifier.padding(innerPadding))
            is UiState.Error -> ErrorView(
                error = state.error,
                onRetry = { viewModel.loadCryptoDetail(cryptoId) },
                modifier = Modifier.padding(innerPadding)
            )
            is UiState.Success -> CryptoDetailContent(
                crypto = state.data,
                modifier = Modifier.padding(innerPadding)
            )
            UiState.Idle -> {}
        }
    }
}

@Composable
fun CryptoDetailContent(
    crypto: CryptoDetail,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = crypto.name, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text(text = "Símbolo: ${crypto.symbol}", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(16.dp))
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Precio USD", style = MaterialTheme.typography.labelLarge)
                Text(text = "$${crypto.priceUsd}", style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Cambio 24h: ${crypto.percentChange24h}%", color = if(crypto.percentChange24h.startsWith("-")) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary)
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Capitalización de Mercado", style = MaterialTheme.typography.titleSmall)
        Text(text = "$${crypto.marketCapUsd}", style = MaterialTheme.typography.bodyLarge)
    }
}
