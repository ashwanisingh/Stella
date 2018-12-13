package com.ns.networking.model

data class BookingConfirmResponse(
    val `data`: BookingConfirmData,
    val message: String,
    val resultcode: Int
)

data class BookingConfirmData(
    val booking_id: Int
)