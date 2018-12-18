package com.ns.networking.model

data class AddAddressResponse(
    val `data`: AddAddressData,
    val message: String,
    val resultcode: Int
)

data class AddAddressData(
    val address_id: Int
)