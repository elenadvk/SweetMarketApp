package com.bitmobileedition.sweetmarket

data class LoginResponse(
    val user: UserResponse?,
    val accessToken: String
)
//data class LoginResponse(val accessToken: String)