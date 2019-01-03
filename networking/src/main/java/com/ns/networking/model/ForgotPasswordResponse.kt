package com.ns.networking.model

data class ForgotPasswordResponse(
    val data: List<Any>,
    val message: String,
    val resultcode: Int
)