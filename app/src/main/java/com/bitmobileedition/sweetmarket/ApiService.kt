package com.bitmobileedition.sweetmarket

import retrofit2.http.GET

interface ApiService {
    @GET("/items")
    suspend fun getItems(): List<Item>
}