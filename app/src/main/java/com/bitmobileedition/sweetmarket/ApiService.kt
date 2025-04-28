package com.bitmobileedition.sweetmarket


import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @POST("/auth/register")
    suspend fun registerUser(@Body request: RegisterRequest): Response<RegisterResponse>

    @POST("/auth/login")
    suspend fun loginUser(@Body request: LoginRequest): Response<LoginResponse>

    @GET("/auth/otp-verification")
    suspend fun verifyOtp(
        @Query("userId") userId: String,
        @Query("otp") otp: String
    ): Response<OtpVerificationResponse>

//    @GET("/auth/otp-verification")
//    suspend fun verifyOtp(@Body otpVerificationRequest: OtpVerificationRequest): Response<Boolean>
}
//@GET("/items")
//suspend fun getItems(): List<Item>

//    @GET("auth/otp-verification")
//    suspend fun verifyOtp(
//        @Query("userId") userId: String,
//        @Query("otp") otp: String
//    ): Response<Boolean>

//    @POST("auth/register")
//    fun register(@Body registerRequest: RegisterRequest): Call<RegisterResponse>
//
//    @POST("auth/login")
//    fun login(@Body authRequest: AuthRequest): Call<AuthResponse>
//}