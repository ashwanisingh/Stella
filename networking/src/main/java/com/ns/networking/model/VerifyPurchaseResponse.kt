package com.ns.networking.model

data class VerifyPurchaseResponse(
    val `data`: VerifyPuchaseData,
    val message: String,
    val resultcode: Int
)

data class VerifyPuchaseData(
    val updated_seats: Int
)