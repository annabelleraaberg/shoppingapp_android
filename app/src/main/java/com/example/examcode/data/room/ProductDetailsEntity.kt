package com.example.examcode.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ProductDetails")
data class ProductDetailsEntity (
    @PrimaryKey
    val title: String,
    val id: Int,
    val description: String,
    val price: Int,
    val discountPercentage: Double,
    val rating: Double,
    val stock: Int,
    val brand: String,
    val category: String,
    val thumbnail: String,
)