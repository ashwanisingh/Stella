package com.ns.networking.model.secondaryusers

data class AddSecondaryUserResponse(
    val `data`: SeondaryUserData,
    val message: String,
    val resultcode: Int
)

data class SeondaryUserData(
    val secondary_user_id: Int
)