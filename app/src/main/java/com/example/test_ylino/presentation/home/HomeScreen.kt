package com.example.test_ylino.presentation.home

import androidx.compose.foundation.layout.padding
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
import com.example.test_ylino.domain.model.Product
import com.example.test_ylino.presentation.components.ErrorView
import com.example.test_ylino.presentation.components.LoadingView
import com.example.test_ylino.presentation.components.ProductCard
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onNavigate: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collectLatest { event ->
            when (event) {
                is UiEvent.Navigate -> onNavigate(event.route)
                is UiEvent.ShowSnackbar -> { /* Handle snackbar */ }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Mis Productos") })
        }
    ) { innerPadding ->
        when (val state = uiState) {
            is UiState.Loading -> LoadingView(modifier = Modifier.padding(innerPadding))
            is UiState.Error -> ErrorView(
                error = state.error,
                onRetry = { viewModel.loadProducts() },
                modifier = Modifier.padding(innerPadding)
            )
            is UiState.Success -> ProductList(
                products = state.data,
                onProductClick = { viewModel.onProductClick(it) },
                modifier = Modifier.padding(innerPadding)
            )
            UiState.Idle -> {}
        }
    }
}

@Composable
fun ProductList(
    products: List<Product>,
    onProductClick: (Product) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.padding(horizontal = 16.dp)
    ) {
        items(products) { product ->
            ProductCard(
                product = product,
                onClick = { onProductClick(product) }
            )
        }
    }
}
