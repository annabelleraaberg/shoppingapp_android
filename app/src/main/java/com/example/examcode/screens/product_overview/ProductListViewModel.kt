package com.example.examcode.screens.product_overview

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.examcode.data.ProductRepository
import com.example.examcode.data.room.ProductEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProductListViewModel: ViewModel() {
    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _products = MutableStateFlow<List<ProductEntity>>(emptyList())
    val products = _products.asStateFlow()

    private val _searchFilter = MutableStateFlow("")
    val searchFilter = _searchFilter.asStateFlow()

    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory = _selectedCategory.asStateFlow()

    val filteredProducts = combine(products, searchFilter) { product, filterText ->
        product.filter { it.title.startsWith(filterText) }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = emptyList()
    )

    val filteredCategory = combine(products, selectedCategory) { product, filterCategory ->
        try {
            Log.d("ProductListViewModel", "List by category")
            if (filterCategory.isNullOrEmpty()) {
                emptyList()
            } else {
                val filteredProducts = product.filter { it.category == filterCategory }
                Log.d("ProductListViewModel", "Filtered products: $filteredProducts")
                filteredProducts
            }
        } catch (e: Exception) {
            Log.e("ProductListViewModel", "Error fetching products by category", e)
            emptyList()
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = emptyList()
    )

    init {
        loadProducts()
    }

    fun loadProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            _loading.value = true
            _products.value = ProductRepository.getProducts()
            _loading.value = false
        }
    }

    fun onFilterTextChanged(text: String) {
        try {
            Log.d("ProductListViewModel", "Filter text changed: $text")
            _searchFilter.value = text
        } catch (e: Exception) {
            Log.e("ProductListViewModel", "Error filtering text search", e)
        }
    }

    fun onCategorySelected(category: String) {
        try {
            _selectedCategory.value = category
        } catch (e: Exception) {
            Log.e("ProductListViewModel", "Error filtering by category", e)
        }
    }
}