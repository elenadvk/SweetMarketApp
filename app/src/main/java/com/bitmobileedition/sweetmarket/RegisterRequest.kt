package com.bitmobileedition.sweetmarket


data class RegisterRequest(
    val email: String,
    val password: String,
    val userType: String
)