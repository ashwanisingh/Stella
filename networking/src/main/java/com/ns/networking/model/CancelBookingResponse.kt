package com.ns.networking.model

data class CancelBookingResponse(
    val `data`: List<Any>,
    val message: String,
    val resultcode: Int
)