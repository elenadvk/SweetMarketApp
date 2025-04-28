package com.bitmobileedition.sweetmarket

data class OtpVerificationRequest(
    val userId: String,
    val otp: String
)
