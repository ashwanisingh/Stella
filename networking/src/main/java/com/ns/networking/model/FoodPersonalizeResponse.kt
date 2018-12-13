package com.ns.networking.model

data class FoodPersonalizeResponse(
    val `data`: FoodPersonlaizeData,
    val message: String,
    val resultcode: Int
)

data class FoodPersonlaizeData(
    val booking_id: Int
)