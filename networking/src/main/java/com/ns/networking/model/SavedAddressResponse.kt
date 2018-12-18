package com.ns.networking.model

data class SavedAddressResponse(
    val `data`: SavedAddressData,
    val message: String,
    val resultcode: Int
)

data class SavedAddressData(
    val addresses: List<SavedAddresse>
)

data class SavedAddresse(
    val address: String,
    val address_tag: String,
    val city: Int,
    val city_info: SavedCityInfo,
    val created_at: String,
    val id: Int,
    val user: Int
)

data class SavedCityInfo(
    val id: Int,
    val name: String
)