package com.example.examcode.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Orders")
data class OrderEntity(
    @PrimaryKey
    val orderDate: String,
    val totalAmount: Double,
    val cartItems: List<CartItemEntity>
)