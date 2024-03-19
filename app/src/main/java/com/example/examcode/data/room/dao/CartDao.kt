package com.example.examcode.data.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.examcode.data.room.CartItemEntity

@Dao
interface CartDao {
    @Query("SELECT * FROM CartItem")
    suspend fun getAllCartItems(): List<CartItemEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartItem(cartItemEntity: CartItemEntity)

    @Delete
    suspend fun removeCartItem(cartItemEntity: CartItemEntity)

    @Query("DELETE FROM CartItem")
    suspend fun clearCart()
}