package com.ns.networking.model

data class CabPersonalizeResponse(
    val `data`: CabPersonalizeData,
    val message: String,
    val resultcode: Int
)

data class CabPersonalizeData(
    val booking_id: Int
)