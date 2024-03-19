package com.example.examcode.screens.shopping_cart

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.examcode.data.ProductRepository
import com.example.examcode.data.room.CartItemEntity
import com.example.examcode.data.room.OrderEntity
import com.example.examcode.data.room.ProductEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.NoSuchElementException

class ShoppingCartViewModel : ViewModel() {
    private val _products = MutableStateFlow<List<ProductEntity>>(emptyList())
    private val _cartItems = MutableStateFlow<List<CartItemEntity>>(emptyList())

    val products = _products.asStateFlow()
    val cartItems = _cartItems.asStateFlow()

    fun loadCartItems() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val listOfCartItems = ProductRepository.getCartItems()

                if (listOfCartItems.isNotEmpty()) {
                    _cartItems.value = listOfCartItems
                    _products.value = ProductRepository.getProductsByIds(listOfCartItems.map { it.productId })
                } else {
                    Log.d("ShoppingCartViewModel", "Cart is empty")
                }
            } catch (e: Exception) {
                Log.e("ShoppingCartViewModel", "Error loading cart items", e)
            }
        }
    }

    fun removeCartItem(cartItem: CartItemEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                ProductRepository.removeCartItem(cartItem)
                _cartItems.value = _cartItems.value.filter { it.productId != cartItem.productId }

            } catch (e: Exception) {
                Log.e("ShoppingCartViewModel", "Error deleting cart item", e)
            }
        }
    }

    fun totalSum(): Int {
        return _products.value.sumOf { it.price }
    }

    fun getProductById(productId: Int): ProductEntity {
        return _products.value.firstOrNull { it.id == productId } ?: throw NoSuchElementException("Product not found")
    }

    fun placeOrder(orderDate: String, cartItems: List<CartItemEntity>) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val totalAmount = ProductRepository.calculateTotalSum(cartItems)
                val orderEntity = OrderEntity(orderDate, totalAmount.toDouble(), cartItems)
                ProductRepository.placeOrder(orderEntity)
            } catch (e: Exception) {
                Log.e("ShoppingCartViewModel", "Error placing order", e)
            }
        }
    }

    fun cartCount(): Int {
        return cartItems.value.sumOf {it.quantity}
    }

    fun clearCart() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                ProductRepository.clearCart()
                _cartItems.value = emptyList()
                _products.value = emptyList()
                Log.d("ShoppingCartViewModel", "Cart cleared!")
            } catch (e: Exception) {
                Log.e("ShoppingCartViewModel", "Error clearing cart", e)
            }
        }
    }
}