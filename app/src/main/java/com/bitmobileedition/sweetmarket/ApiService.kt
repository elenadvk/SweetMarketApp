package com.bitmobileedition.sweetmarket

import retrofit2.http.GET

interface ApiService {
    @GET("/items")
    suspend fun getItems(): List<Item>
}

//interface ApiService {
//
//    @GET("/items")
//    suspend fun getItems(): List<Item>
//
//    @POST("/registerUser")
//    suspend fun registerUser(@Body user: User): ApiResponse
//
//    @POST("/registerSeller")
//    suspend fun registerSeller(@Body seller: Seller): ApiResponse
//
//    @POST("/loginUser")
//    suspend fun loginUser(@Body loginRequest: LoginRequest): ApiResponse
//
//    @POST("/loginSeller")
//    suspend fun loginSeller(@Body loginRequest: LoginRequest): ApiResponse
//}
