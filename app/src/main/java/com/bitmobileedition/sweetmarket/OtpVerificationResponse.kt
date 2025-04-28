package com.bitmobileedition.sweetmarket

data class OtpVerificationResponse(
    val isSuccess: Boolean,
    val statusCode: ResponseStatus,
    val data: Any
)

data class OtpVerificationData(
    val isVerified: Boolean
)

data class ResponseStatus(
    val value: Int,
    val description: String
)

