package com.example.test_ylino.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.test_ylino.core.dispatcher.DefaultDispatcherProvider
import com.example.test_ylino.data.remote.mock.MockProductApiService
import com.example.test_ylino.data.repository.ProductRepositoryImpl
import com.example.test_ylino.domain.usecase.GetProductDetailUseCase
import com.example.test_ylino.domain.usecase.GetProductsUseCase
import com.example.test_ylino.presentation.detail.DetailScreen
import com.example.test_ylino.presentation.detail.DetailViewModel
import com.example.test_ylino.presentation.home.HomeScreen
import com.example.test_ylino.presentation.home.HomeViewModel

@Composable
fun AppNavigation(navController: NavHostController) {
    // Basic manual DI setup for the template
    val dispatcherProvider = remember { DefaultDispatcherProvider() }
    val apiService = remember { MockProductApiService() }
    val repository = remember { ProductRepositoryImpl(apiService, dispatcherProvider) }
    
    val getProductsUseCase = remember { GetProductsUseCase(repository) }
    val getProductDetailUseCase = remember { GetProductDetailUseCase(repository) }

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            val homeViewModel: HomeViewModel = viewModel(
                factory = GenericViewModelFactory { HomeViewModel(getProductsUseCase) }
            )
            HomeScreen(
                viewModel = homeViewModel,
                onNavigate = { route -> navController.navigate(route) }
            )
        }
        composable(
            route = Screen.Detail.route,
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
