package com.example.examcode.data

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.example.examcode.data.room.AppDatabase
import com.example.examcode.data.room.CartItemEntity
import com.example.examcode.data.room.FavoriteEntity
import com.example.examcode.data.room.OrderEntity
import com.example.examcode.data.room.ProductDetailsEntity
import com.example.examcode.data.room.ProductEntity
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ProductRepository {
    private val _httpClient =
        OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.BODY)
            )
            .build()

    private val _retrofit =
        Retrofit.Builder()
            .client(_httpClient)
            .baseUrl("https://dummyjson.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    private val _productService = _retrofit.create(ProductService::class.java)

    private lateinit var _appDatabase: AppDatabase
    private val _productDao by lazy { _appDatabase.productDao() }
    private val _favoriteDao by lazy { _appDatabase.favoriteDao() }
    private val _productDetailsDao by lazy { _appDatabase.productDetailsDao() }
    private val _cartDao by lazy { _appDatabase.cartDao() }
    private val _orderDao by lazy { _appDatabase.orderDao() }

    fun initializeDatabase(context: Context) {
        _appDatabase = Room.databaseBuilder(
            context = context,
            klass = AppDatabase::class.java,
            name = "app-database"
        ).build()
    }

    suspend fun getProducts(): List<ProductEntity> {
        try {
            val response = _productService.getAllProducts()

            if (response.isSuccessful) {
                val productResponse = response.body()
                val products = productResponse?.products ?: emptyList()
                _productDao.insertProduct(products)
                return _productDao.getAllProducts()
            } else {
                throw Exception("Response was not successful")
            }
        } catch (e: Exception) {
            Log.e("ProductRepository", "Error fetching list of products", e)
            return _productDao.getAllProducts()
        }
    }

    suspend fun getProductDetails(id: Int): ProductDetailsEntity? {
        return try {
            val response = _productService.getProductDetails(id)
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("ProductRepository", "Error fetching product by ID", e)
            null
        }
    }

    suspend fun getProductById(id: Int): ProductEntity? {
        return _productDao.getProductById(id)
    }

    suspend fun getProductsByIds(idList: List<Int>): List<ProductEntity> {
        return _productDao.getProductsByIds(idList)
    }

    /*  Favorites   */
    suspend fun getFavorites(): List<FavoriteEntity> {
        return _favoriteDao.getFavorites()
    }

    suspend fun addFavorite(favoriteEntity: FavoriteEntity) {
        _favoriteDao.insertFavorite(favoriteEntity)
    }

    suspend fun removeFavorite(favoriteEntity: FavoriteEntity) {
        _favoriteDao.removeFavorite(favoriteEntity)
    }

    /*  Cart items  */
    suspend fun getCartItems(): List<CartItemEntity> {
        return _cartDao.getAllCartItems()
    }

    suspend fun addCartItem(cartItemEntity: CartItemEntity) {
        _cartDao.insertCartItem(cartItemEntity)
    }

    suspend fun removeCartItem(cartItemEntity: CartItemEntity) {
        _cartDao.removeCartItem(cartItemEntity)
    }

    suspend fun clearCart() {
        _cartDao.clearCart()
    }

    /*  Orders  */
    suspend fun getOrders(): List<OrderEntity> {
        return _orderDao.getAllOrders()
    }

    suspend fun calculateTotalSum(cartItems: List<CartItemEntity>): Int {
        return cartItems.sumOf { it.quantity * getProductPrice(it.productId) }
    }

    private suspend fun getProductPrice(productId: Int): Int {
        val product = _productDao.getProductById(productId)
        return product?.price ?: 0
    }

    suspend fun placeOrder(orderEntity: OrderEntity) {
        _orderDao.insertOrder(orderEntity)
        _cartDao.clearCart()
    }

    suspend fun clearOrderHistory() {
        _orderDao.clearOrderHistory()
    }
}