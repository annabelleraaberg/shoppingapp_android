package com.example.examcode

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.examcode.data.ProductRepository
import com.example.examcode.screens.favorite_list.FavoriteListScreen
import com.example.examcode.screens.favorite_list.FavoriteListViewModel
import com.example.examcode.screens.order_history.OrderHistoryScreen
import com.example.examcode.screens.order_history.OrderHistoryViewModel
import com.example.examcode.screens.product_details.ProductDetailsScreen
import com.example.examcode.screens.product_details.ProductDetailsViewModel
import com.example.examcode.screens.product_overview.ProductListScreen
import com.example.examcode.screens.product_overview.ProductListViewModel
import com.example.examcode.screens.shopping_cart.ShoppingCartScreen
import com.example.examcode.screens.shopping_cart.ShoppingCartViewModel
import com.example.examcode.ui.theme.ExamcodeTheme

class MainActivity : ComponentActivity() {
    private val _productListViewModel: ProductListViewModel by viewModels()
    private val _productDetailsViewModel: ProductDetailsViewModel by viewModels()
    private val _shoppingCartViewModel: ShoppingCartViewModel by viewModels()
    private val _orderHistoryViewModel: OrderHistoryViewModel by viewModels()
    private val _favoriteListViewModel: FavoriteListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ProductRepository.initializeDatabase(applicationContext)

        setContent {
            ExamcodeTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = "productListScreen"
                ) {
                    composable(route = "productListScreen")
                    {
                        ProductListScreen(
                            viewModel = _productListViewModel,
                            onProductClick = { productId ->
                                navController.navigate("productDetailsScreen/$productId")
                            },
                            navController = navController,
                            navigateToFavoriteList = {
                                navController.navigate("favoriteListScreen")
                            }
                        )
                    }
                    composable(
                        route = "productDetailsScreen/{productId}",
                        arguments = listOf(
                            navArgument(name = "productId") {
                                type = NavType.IntType
                            }
                        )
                    ) {backStackEntry ->
                        val productId = backStackEntry.arguments?.getInt("productId") ?: -1
                        val viewModel: ProductDetailsViewModel = viewModel()
                        LaunchedEffect(productId) {
                            viewModel.setSelectedProduct(productId)
                        }
                        ProductDetailsScreen(
                            viewModel = viewModel,
                            shoppingCartViewModel = _shoppingCartViewModel,
                            onBackButtonClick = {navController.popBackStack()},
                            onShoppingCartClick = {navController.navigate("shoppingCartScreen")}
                        )
                    }
                    composable(route = "favoriteListScreen") {
                        LaunchedEffect(Unit) {
                            _favoriteListViewModel.loadFavorites()
                        }
                        FavoriteListScreen(
                            viewModel = _favoriteListViewModel,
                            onBackButtonClick = {navController.popBackStack()},
                            onProductClick = { productId ->
                                navController.navigate("productDetailsScreen/$productId")
                            }
                        )
                    }
                    composable(
                        route = "shoppingCartScreen"
                    ) {
                        ShoppingCartScreen(
                            viewModel = _shoppingCartViewModel,
                            navController = navController,
                            onBackButtonClick = { navController.popBackStack() },
                            onHomeButtonClick = { navController.navigate("productListScreen") }
                        )
                    }
                    composable(
                        route = "orderHistoryScreen"
                    ) {
                        OrderHistoryScreen(
                            viewModel = _orderHistoryViewModel,
                            onBackButtonClick = {navController.popBackStack()},
                            onHomeButtonClick = {navController.navigate("productListScreen")}
                        )
                    }
                }
            }
        }
    }
}

