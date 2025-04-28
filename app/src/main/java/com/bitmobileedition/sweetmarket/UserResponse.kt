package com.bitmobileedition.sweetmarket

data class UserResponse(
    val id: String,
    val email: String,
    val isVerified: Boolean?,
    val userType: String
)