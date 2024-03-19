package com.example.examcode.data

import com.example.examcode.data.room.ProductDetailsEntity
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductService {
    @GET("products")
    suspend fun getAllProducts(): Response<ProductResponse>

    @GET("products/{id}")
    suspend fun getProductDetails(
        @Path("id") id: Int
    ): Response<ProductDetailsEntity>
}