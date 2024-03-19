package com.example.examcode.screens.order_history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.examcode.data.ProductRepository
import com.example.examcode.data.room.OrderEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OrderHistoryViewModel: ViewModel() {
    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _orders = MutableStateFlow<List<OrderEntity>>(emptyList())
    val orders = _orders.asStateFlow()

    init {
        loadOrderHistory()
    }

    fun loadOrderHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            _loading.value = true
            _orders.value = ProductRepository.getOrders()
            _loading.value = false
        }
    }

    fun clearOrderHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            ProductRepository.clearOrderHistory()
            _orders.value = emptyList()
        }
    }
}