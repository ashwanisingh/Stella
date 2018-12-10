package com.ns.networking.model

data class ValidateCustomerResponse(
    val `data`: ValidateCustomerData,
    val message: String,
    val resultcode: Int
)

data class ValidateCustomerData(
    val username: String,
    val usertype: String
)