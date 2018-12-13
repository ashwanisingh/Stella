package com.ns.networking.model

data class GuestConfirmResponse(
    val `data`: GuestConfirmData,
    val message: String,
    val resultcode: Int
)

data class GuestConfirmData(
    val new_contacts: List<NewContact>
)

data class NewContact(
    val guest_id: Int
)