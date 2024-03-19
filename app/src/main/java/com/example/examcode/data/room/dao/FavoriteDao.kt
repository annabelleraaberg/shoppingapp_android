package com.example.examcode.data.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.examcode.data.room.FavoriteEntity

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM ProductFavorite")
    suspend fun getFavorites(): List<FavoriteEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favoriteEntity: FavoriteEntity)

    @Delete
    suspend fun removeFavorite(favoriteEntity: FavoriteEntity)
}