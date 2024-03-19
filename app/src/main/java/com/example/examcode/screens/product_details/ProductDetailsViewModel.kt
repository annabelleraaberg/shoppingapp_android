package com.example.examcode.screens.product_details

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.examcode.data.ProductRepository
import com.example.examcode.data.room.CartItemEntity
import com.example.examcode.data.room.FavoriteEntity
import com.example.examcode.data.room.ProductDetailsEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProductDetailsViewModel(application: Application) : AndroidViewModel(application) {
    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _selectedProductDetails = MutableStateFlow<ProductDetailsEntity?>(null)
    val selectedProductDetails = _selectedProductDetails.asStateFlow()

    private val _inCart = MutableStateFlow(false)
    val inCart = _inCart.asStateFlow()

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite = _isFavorite.asStateFlow()

    fun setSelectedProduct(productId: Int) {
        viewModelScope.launch {
            _loading.value = true
            _selectedProductDetails.value = ProductRepository.getProductDetails(id = productId)
            _inCart.value = isProductInCart()
            _isFavorite.value = isCurrentProductAFavorite()
            _loading.value = false
        }
    }

    fun updateCart(productId: Int) {
        viewModelScope.launch {
            try {
                if (inCart.value) {
                    increaseCartItemQuantity(productId)
                } else {
                    ProductRepository.addCartItem(CartItemEntity(productId, quantity = 1))
                }
                _inCart.value = isProductInCart()
            } catch (e: Exception) {
                Log.e("ProductDetailsViewModel", "Error updating cart", e)
            }
        }
    }

    private suspend fun isProductInCart(): Boolean {
        return ProductRepository.getCartItems().any {it.productId == selectedProductDetails.value?.id}
    }

    private suspend fun increaseCartItemQuantity(productId: Int) {
        try {
            val existingCartItem = ProductRepository.getCartItems().find { it.productId == productId }

            if (existingCartItem != null) {
                val updatedCartItem = existingCartItem.copy(quantity = existingCartItem.quantity + 1)
                ProductRepository.addCartItem(updatedCartItem)
            }
        } catch (e: Exception) {
            Log.e("ProductDetailsViewModel", "Error increasing quantity", e)
        }
    }

    fun updateFavorite(productId: Int) {
        viewModelScope.launch {
            try {
                if (isFavorite.value) {
                    ProductRepository.removeFavorite(FavoriteEntity(productId))
                } else {
                    ProductRepository.addFavorite(FavoriteEntity(productId))
                }
                _isFavorite.value = isCurrentProductAFavorite()
            } catch (e: Exception) {
                Log.e("ProductDetailsViewModel", "Error updating favorite", e)
            }
        }
    }

    private suspend fun isCurrentProductAFavorite(): Boolean {
        return ProductRepository.getFavorites().any {it.productId == selectedProductDetails.value?.id}
    }
}