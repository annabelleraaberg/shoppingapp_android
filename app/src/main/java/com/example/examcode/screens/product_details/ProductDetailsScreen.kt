package com.example.examcode.screens.product_details

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.examcode.screens.shopping_cart.ShoppingCartViewModel
import com.example.examcode.ui.theme.Orange

@Composable
fun ProductDetailsScreen(
    viewModel: ProductDetailsViewModel,
    shoppingCartViewModel: ShoppingCartViewModel,
    onBackButtonClick: () -> Unit = {},
    onShoppingCartClick: () -> Unit = {}
) {
    val loading = viewModel.loading.collectAsState()
    val productDetailsState = viewModel.selectedProductDetails.collectAsState()
    val inCart = viewModel.inCart.collectAsState()
    val isFavorite = viewModel.isFavorite.collectAsState()

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

    val productDetails = productDetailsState.value
    if (productDetails == null) {
        Text(text = "Failed to get product details. Selected product is null")
        return
    }
    Log.d("ProductDetailsScreen", "Product details $productDetails")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(state = rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Product Details
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White),
            horizontalArrangement = Arrangement.SpaceBetween,
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
                text = "Product Details",
                style = MaterialTheme.typography.titleLarge
            )
            IconButton(onClick = { viewModel.updateFavorite(productDetails.id) }) {
                Icon(
                    imageVector = if (isFavorite.value) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Update Favorites",
                    tint = Color.Red
                )
            }
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { onShoppingCartClick() }) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = "Navigate to shoppingCartScreen"
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        AsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .height(270.dp)
                .background(color = Color.Gray),
            model = productDetails.thumbnail,
            contentScale = ContentScale.Crop,
            alignment = Alignment.Center,
            contentDescription = "Image of ${productDetails.title}"
        )

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = productDetails.title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Price: $${productDetails.price}",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Row(
                modifier = Modifier.padding(2.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = "Rating: ${productDetails.rating}",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Icon(
                    imageVector = Icons.Rounded.Star,
                    contentDescription = "Star, rating",
                    tint = Orange
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Brand: ${productDetails.brand}",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Medium,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = productDetails.description,
            modifier = Modifier.padding(horizontal = 20.dp),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Medium,
            color = Color.Black,
        )
        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { viewModel.updateCart(productDetails.id) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
        ) {
            Text(text = "Add to Cart")
        }
    }
}