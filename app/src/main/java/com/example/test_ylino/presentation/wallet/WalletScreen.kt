package com.example.test_ylino.presentation.wallet

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.test_ylino.core.ui.UiState
import com.example.test_ylino.domain.model.CryptoAsset
import com.example.test_ylino.domain.model.CryptoDetail
import com.example.test_ylino.presentation.components.ErrorView
import com.example.test_ylino.presentation.components.LoadingView
import androidx.compose.ui.text.font.FontWeight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WalletScreen(
    viewModel: WalletViewModel
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val detailState by viewModel.detailState.collectAsStateWithLifecycle()
    val selectedAsset by viewModel.selectedAsset.collectAsStateWithLifecycle()
    var expanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Mi Cartera Crypto") })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            when (val state = uiState) {
                is UiState.Loading -> LoadingView()
                is UiState.Error -> ErrorView(
                    error = state.error,
                    onRetry = { viewModel.loadWalletItems() }
                )
                is UiState.Success -> {
                    SearchSection(
                        assets = state.data,
                        selectedAsset = selectedAsset,
                        expanded = expanded,
                        onExpandedChange = { expanded = it },
                        onAssetSelected = { viewModel.onAssetSelected(it) },
                        onSearchClick = { viewModel.onSearchClick() }
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    DetailSection(detailState = detailState, onRetry = { viewModel.onSearchClick() })
                }
                UiState.Idle -> {}
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchSection(
    assets: List<CryptoAsset>,
    selectedAsset: CryptoAsset?,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onAssetSelected: (CryptoAsset) -> Unit,
    onSearchClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { onExpandedChange(it) },
            modifier = Modifier.weight(1f)
        ) {
            TextField(
                value = selectedAsset?.name ?: "Seleccione",
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
                colors = ExposedDropdownMenuDefaults.textFieldColors()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { onExpandedChange(false) }
            ) {
                assets.forEach { asset ->
                    DropdownMenuItem(
                        text = { Text(asset.name) },
                        onClick = {
                            onAssetSelected(asset)
                            onExpandedChange(false)
                        }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.width(8.dp))
        IconButton(
            onClick = onSearchClick,
            modifier = Modifier.size(48.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Buscar",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun DetailSection(
    detailState: UiState<CryptoDetail?>,
    onRetry: () -> Unit
) {
    when (detailState) {
        is UiState.Loading -> LoadingView()
        is UiState.Error -> ErrorView(error = detailState.error, onRetry = onRetry)
        is UiState.Success -> {
            detailState.data?.let { crypto ->
                CryptoDetailCard(crypto = crypto)
            }
        }
        UiState.Idle -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = "Selecciona una criptomoneda y presiona la lupa para buscar",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun CryptoDetailCard(
    crypto: CryptoDetail,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = crypto.name,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Símbolo: ${crypto.symbol}",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.secondary
            )
            Spacer(modifier = Modifier.height(16.dp))
            
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(text = "Precio Actual", style = MaterialTheme.typography.labelMedium)
                    Text(
                        text = "$${crypto.priceUsd}",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(text = "Cambio (24h)", style = MaterialTheme.typography.labelMedium)
                    val isNegative = crypto.percentChange24h.startsWith("-")
                    Text(
                        text = "${crypto.percentChange24h}%",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (isNegative) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Market Cap", style = MaterialTheme.typography.labelMedium)
            Text(
                text = "$${crypto.marketCapUsd}",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
