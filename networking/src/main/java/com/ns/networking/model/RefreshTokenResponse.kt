package com.ns.networking.model

data class RefreshTokenResponse(
    val data: RefreshTokenData,
    val message: String,
    val resultcode: Int
)

data class RefreshTokenData(
    val refresh_token: String,
    val token: String
)