package com.example.examcode.screens.product_overview

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(
    viewModel: ProductListViewModel,
    navController: NavController,
    onProductClick: (productId: Int) -> Unit = {},
    navigateToFavoriteList: () -> Unit = {}
) {
    val loading = viewModel.loading.collectAsState()
    val searchFilter = viewModel.searchFilter.collectAsState()
    val products = viewModel.filteredProducts.collectAsState()
    val category = viewModel.filteredCategory.collectAsState()

    if (loading.value) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.padding(8.dp),
                text = "Products",
                style = MaterialTheme.typography.titleLarge
            )
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { viewModel.loadProducts() }
                ) {
                    Icon(imageVector = Icons.Default.Refresh,
                        contentDescription = "Refresh products"
                    )
                }
                IconButton(onClick = { navigateToFavoriteList() }) {
                    Icon(imageVector = Icons.Default.Favorite,
                        contentDescription = "Favorites",
                        tint = Color.Red
                    )
                }
                IconButton(onClick = {
                    navController.navigate("shoppingCartScreen")
                    Log.d("ProductListScreen", "Shopping cart icon click")
                }
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = "Shopping cart"
                    )
                }
                IconButton(onClick = {
                    navController.navigate("orderHistoryScreen")
                    Log.d("ProductListScreen", "Order history icon clicked")
                }
                ) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Order history",
                        tint = Color.Blue
                    )
                }
            }
        }
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            value = searchFilter.value,
            onValueChange = { text -> viewModel.onFilterTextChanged(text) },
            textStyle = MaterialTheme.typography.titleMedium,
            label = { Text(text = "Product title") }
        )

        CategoryFilter(
            categories = listOf("smartphones", "laptops", "fragrances", "skincare", "groceries", "home-decoration"),
            selectedCategory = category.value
        ) { viewModel.onCategorySelected(it) }

        Divider()

        Log.d("ProductListScreen", "Recomposing UI")

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            items(products.value) {product ->
                ProductItem(
                    product = product,
                    onClick = {
                        Log.d("ProductListScreen", "Product clicked: ${product.id}")
                        onProductClick(product.id)
                    }
                )
            }
        }

        // Filter products by category.
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()

        ) {
            items(category.value) {product ->
                ProductItem(
                    product = product,
                    onClick = {
                        Log.d("ProductListScreen", "Product clicked: ${product.id}")
                        onProductClick(product.id)
                    }
                )
            }
        }
    }
}