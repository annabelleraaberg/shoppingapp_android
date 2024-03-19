package com.example.examcode.screens.favorite_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.examcode.data.ProductRepository
import com.example.examcode.data.room.ProductEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FavoriteListViewModel : ViewModel() {
    private val _favoriteProducts = MutableStateFlow<List<ProductEntity>>(emptyList())
    val favoriteProducts = _favoriteProducts.asStateFlow()

    fun loadFavorites() {
        viewModelScope.launch(Dispatchers.IO) {
            val listOfFavoriteIds = ProductRepository.getFavorites().map { it.productId }
            _favoriteProducts.value = ProductRepository.getProductsByIds(listOfFavoriteIds)
        }
    }
}