package com.example.examcode.screens.shopping_cart

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ShoppingCartScreen(
    viewModel: ShoppingCartViewModel,
    navController: NavController,
    onBackButtonClick: () -> Unit = {},
    onCartItemClick: (productId: Int) -> Unit = {},
    onHomeButtonClick: () -> Unit = {}
) {
    val cartItems = viewModel.cartItems.collectAsState()
    val totalSum = viewModel.totalSum()
    val cartCount = viewModel.cartCount()

    LaunchedEffect(Unit) {
        viewModel.loadCartItems()
    }

    Log.d("ShoppingCartScreen", "products ${cartItems.value}")

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { onBackButtonClick() }
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back"
                )
            }
            Text(
                modifier = Modifier.padding(8.dp),
                text = "Shopping Cart",
                style = MaterialTheme.typography.titleLarge
            )
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = "ShoppingCart"
                )
                Text(
                    text = "$cartCount",
                )
                IconButton(onClick = { onHomeButtonClick() }) {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = "Navigate to ProductListScreen, Home button"
                    )
                }
            }
        }
        Divider()

        if (cartItems.value.isEmpty()) {
            Text(
                modifier = Modifier.padding(24.dp),
                text = "No items in the cart.",
                style = MaterialTheme.typography.titleMedium,
                color = Color.Gray
            )
        } else {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(cartItems.value) { cartItem ->
                    val product = viewModel.getProductById(cartItem.productId)
                    CartItem(
                        product = product,
                        cartItem = cartItem,
                        onRemoveClick = {
                            viewModel.removeCartItem(cartItem)
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    val orderDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(
                        Date()
                    )
                    viewModel.placeOrder(orderDate, cartItems.value)
                    viewModel.clearCart()
                    navController.navigate("orderHistoryScreen")
                    Log.d("ShoppingCartScreen", "Place Order button click")
                }
            ) {
                Text(
                    text = "Place order ($${totalSum})",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}