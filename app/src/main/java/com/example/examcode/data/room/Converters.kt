package com.example.examcode.data.room

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun fromString(value: String?): List<CartItemEntity>? {
        if (value == null) {
            return null
        }

        val listType = object : TypeToken<List<CartItemEntity>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun toString(list: List<CartItemEntity>?): String? {
        return if (list == null) {
            null
        } else {
            Gson().toJson(list)
        }
    }
}