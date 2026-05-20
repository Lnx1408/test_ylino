package com.example.test_ylino.presentation.navigation

import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Home

sealed class Screen(val route: String) {
    data object Products : Screen("products")
    data object Home : Screen("home")
    data object Wallet : Screen("wallet")
    data object ProductDetail : Screen("product_detail/{productId}") {
        fun createRoute(productId: String) = "product_detail/$productId"
    }
    data object CryptoDetail : Screen("crypto_detail/{cryptoId}")
}

sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    data object Products : BottomBarScreen(
        route = Screen.Products.route,
        title = "Productos",
        icon = androidx.compose.material.icons.Icons.Default.AttachMoney
    )

    data object Home : BottomBarScreen(
        route = Screen.Home.route,
        title = "Inicio",
        icon = androidx.compose.material.icons.Icons.Default.Home
    )

    data object Wallet : BottomBarScreen(
        route = Screen.Wallet.route,
        title = "Cartera",
        icon = androidx.compose.material.icons.Icons.Default.AccountBalanceWallet
    )
}
