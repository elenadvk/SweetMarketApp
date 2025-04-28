package com.bitmobileedition.sweetmarket

import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    val isSuccess: Boolean,
    val statusCode: StatusCode,
    val data: Data
)

data class StatusCode(val value: Int, val description: String)

data class Data(
    @SerializedName("id") val id: String,  // Явное указание имени поля в JSON
    val email: String,
    val message: String
)

