package com.example.test_ylino.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.example.test_ylino.core.dispatcher.DefaultDispatcherProvider
import com.example.test_ylino.data.remote.CoinLoreApiService
import com.example.test_ylino.data.remote.mock.MockProductApiService
import com.example.test_ylino.data.repository.ProductRepositoryImpl
import com.example.test_ylino.data.repository.WalletRepositoryImpl
import com.example.test_ylino.domain.usecase.*
import com.example.test_ylino.presentation.detail.DetailScreen
import com.example.test_ylino.presentation.detail.DetailViewModel
import com.example.test_ylino.presentation.home.HomeScreen
import com.example.test_ylino.presentation.home.HomeViewModel
import com.example.test_ylino.presentation.home.WelcomeScreen
import com.example.test_ylino.presentation.wallet.CryptoDetailScreen
import com.example.test_ylino.presentation.wallet.CryptoDetailViewModel
import com.example.test_ylino.presentation.wallet.WalletScreen
import com.example.test_ylino.presentation.wallet.WalletViewModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Composable
fun AppNavigation(navController: NavHostController) {
    // Basic manual DI setup for the template
    val dispatcherProvider = remember { DefaultDispatcherProvider() }
    
    // Retrofit for real API calls
    val retrofit = remember {
        Retrofit.Builder()
            .baseUrl("https://api.coinlore.net/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    val coinLoreApiService = remember { retrofit.create(CoinLoreApiService::class.java) }
    
    // Services and Repositories
    val apiService = remember { MockProductApiService() }
    val productRepository = remember { ProductRepositoryImpl(apiService, dispatcherProvider) }
    val walletRepository = remember { WalletRepositoryImpl(coinLoreApiService, dispatcherProvider) }
    
    // Use Cases
    val getProductsUseCase = remember { GetProductsUseCase(productRepository) }
    val getProductDetailUseCase = remember { GetProductDetailUseCase(productRepository) }
    val getWalletItemsUseCase = remember { GetWalletItemsUseCase(walletRepository) }
    val getCryptoDetailUseCase = remember { GetCryptoDetailUseCase(walletRepository) }

    val bottomBarScreens = listOf(
        BottomBarScreen.Products,
        BottomBarScreen.Home,
        BottomBarScreen.Wallet
    )

    Scaffold(
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination
            
            // Only show bottom bar on top-level destinations
            val showBottomBar = bottomBarScreens.any { it.route == currentDestination?.route }
            
            if (showBottomBar) {
                NavigationBar {
                    bottomBarScreens.forEach { screen ->
                        NavigationBarItem(
                            icon = { Icon(screen.icon, contentDescription = null) },
                            label = { Text(screen.title) },
                            selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = androidx.compose.ui.Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                WelcomeScreen()
            }
            composable(Screen.Products.route) {
                val homeViewModel: HomeViewModel = viewModel(
                    factory = GenericViewModelFactory { HomeViewModel(getProductsUseCase) }
                )
                HomeScreen(
                    viewModel = homeViewModel,
                    onNavigate = { route -> navController.navigate(route) }
                )
            }
            composable(Screen.Wallet.route) {
                val walletViewModel: WalletViewModel = viewModel(
                    factory = GenericViewModelFactory { 
                        WalletViewModel(getWalletItemsUseCase, getCryptoDetailUseCase) 
                    }
                )
                WalletScreen(
                    viewModel = walletViewModel
                )
            }
            composable(
                route = Screen.ProductDetail.route,
                arguments = listOf(navArgument("productId") { type = NavType.StringType })
            ) { backStackEntry ->
                val productId = backStackEntry.arguments?.getString("productId") ?: ""
                val detailViewModel: DetailViewModel = viewModel(
                    factory = GenericViewModelFactory { DetailViewModel(getProductDetailUseCase) }
                )
                DetailScreen(
                    productId = productId,
                    viewModel = detailViewModel,
                    onBack = { navController.popBackStack() }
                )
            }
            composable(
                route = Screen.CryptoDetail.route,
                arguments = listOf(navArgument("cryptoId") { type = NavType.StringType })
            ) { backStackEntry ->
                val cryptoId = backStackEntry.arguments?.getString("cryptoId") ?: ""
                val cryptoDetailViewModel: CryptoDetailViewModel = viewModel(
                    factory = GenericViewModelFactory { CryptoDetailViewModel(getCryptoDetailUseCase) }
                )
                CryptoDetailScreen(
                    cryptoId = cryptoId,
                    viewModel = cryptoDetailViewModel,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}

// Simple factory for manual DI in this template
class GenericViewModelFactory<T : androidx.lifecycle.ViewModel>(
    private val creator: () -> T
) : androidx.lifecycle.ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        return creator() as T
    }
}
