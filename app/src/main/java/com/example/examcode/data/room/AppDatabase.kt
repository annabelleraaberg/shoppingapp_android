package com.example.examcode.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.examcode.data.room.dao.CartDao
import com.example.examcode.data.room.dao.FavoriteDao
import com.example.examcode.data.room.dao.OrderDao
import com.example.examcode.data.room.dao.ProductDao
import com.example.examcode.data.room.dao.ProductDetailsDao

@Database(
    entities = [CartItemEntity::class, FavoriteEntity::class, OrderEntity::class, ProductEntity::class, ProductDetailsEntity::class],
    version = 1, exportSchema = false
)


@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun cartDao(): CartDao
    abstract fun favoriteDao(): FavoriteDao
    abstract fun orderDao(): OrderDao
    abstract fun productDao(): ProductDao
    abstract fun productDetailsDao(): ProductDetailsDao
}