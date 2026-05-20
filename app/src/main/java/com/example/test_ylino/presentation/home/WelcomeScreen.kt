package com.example.test_ylino.presentation.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WelcomeScreen() {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Inicio") })
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "¡Bienvenido a tu App Bancaria!",
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }
}
