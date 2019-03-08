package com.ns.networking.model.foods

data class GlobalFoodPrefResponse(
    val `data`: List<Any>,
    val message: String,
    val resultcode: Int
)