package com.bitmobileedition.sweetmarket

import retrofit2.http.GET
//import retrofit2.http.Path



interface ApiService {
    @GET("/items")
    suspend fun getItems(): List<Item>
}

//package com.bitmobileedition.sweetmarket
//
//import retrofit2.http.Body
//import retrofit2.http.GET
//import retrofit2.http.POST
//import retrofit2.http.Path
//
//// ApiService.kt - расширяем интерфейс
//interface ApiService {
//    @GET("/items")
//    suspend fun getItems(): List<Item>

//    @POST("/auth/login")
//    suspend fun login(@Body request: LoginRequest): LoginResponse
//
//    @POST("/auth/register")
//    suspend fun register(@Body request: RegisterRequest): RegisterResponse
//
//    @POST("/products")
//    suspend fun addProduct(@Body product: ProductRequest): ProductResponse
//
//    @GET("/orders/seller/{sellerId}")
//    suspend fun getSellerOrders(@Path("sellerId") sellerId: Int): List<Order>
//
//    @POST("/orders")
//    suspend fun placeOrder(@Body order: OrderRequest): OrderResponse
//
//    @POST("api/register")
//    suspend fun register(@Body user: User): Response<String>
//
//    @POST("api/login")
//    suspend fun login(@Body loginRequest: LoginRequest): Response<String>
//}

