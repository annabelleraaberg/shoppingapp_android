package com.example.examcode.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ProductFavorite")
data class FavoriteEntity (
    @PrimaryKey
    val productId: Int
)