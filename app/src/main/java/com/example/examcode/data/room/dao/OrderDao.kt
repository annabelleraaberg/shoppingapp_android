package com.example.examcode.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.examcode.data.room.OrderEntity

@Dao
interface OrderDao {
    @Query("SELECT * FROM Orders")
    suspend fun getAllOrders(): List<OrderEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(orderEntity: OrderEntity)

    @Query("DELETE FROM Orders")
    suspend fun clearOrderHistory()
}