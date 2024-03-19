package com.example.examcode.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.examcode.data.room.ProductDetailsEntity

@Dao
interface ProductDetailsDao {
    @Query("SELECT * FROM ProductDetails WHERE :productTitle = title")
    suspend fun getProductByTitle(productTitle: String): ProductDetailsEntity?

    @Query("SELECT * FROM ProductDetails WHERE :productId = id")
    suspend fun getProductDetailsById(productId: String): ProductDetailsEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProductDetails(vararg productDetails: ProductDetailsEntity)
}