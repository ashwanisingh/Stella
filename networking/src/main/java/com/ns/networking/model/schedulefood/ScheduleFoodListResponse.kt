package com.ns.networking.model.schedulefood

import com.ns.networking.model.Food

data class ScheduleFoodListResponse(
    val `data`: List<Food>,
    val message: String,
    val resultcode: Int
)

data class ScheduleFoodListData(
    val created_at: String,
    val description: String,
    val food_image: String,
    val food_type: String,
    val food_type_text: String,
    val id: Int,
    val image_path: String,
    val img_url: String,
    val name: String,
    val updated_at: Any
)