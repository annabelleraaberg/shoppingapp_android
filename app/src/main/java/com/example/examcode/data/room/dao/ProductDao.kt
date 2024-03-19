package com.example.examcode.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.examcode.data.room.ProductEntity

@Dao
interface ProductDao {
    @Query("SELECT * FROM Products")
    fun getAllProducts(): List<ProductEntity>

    @Query("SELECT * FROM Products WHERE id = :id")
    suspend fun getProductById(id: Int): ProductEntity?

    @Query("SELECT * FROM Products WHERE id IN (:idList)")
    suspend fun getProductsByIds(idList: List<Int>): List<ProductEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(productEntities: List<ProductEntity>)
}