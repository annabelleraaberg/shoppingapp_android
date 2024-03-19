package com.example.examcode.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "CartItem")
data class CartItemEntity(
    @PrimaryKey
    val productId: Int,
    val quantity: Int
)