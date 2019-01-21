package com.ns.networking.model.secondaryusers

data class SecondaryUsersListResponse(
    val `data`: SecondaryUsersData,
    val message: String,
    val resultcode: Int
)

data class SecondaryUsersData(
    val primary_user: String,
    val secondary_users: List<SecondaryUserInfoList>
)

data class SecondaryUserInfoList(
    val su_name: String,
    val su_email: String,
    val su_id: Int,
    val su_phone: String
)

data class SecondaryUser(
    val su_name: String,
    val su_email: String,
    val su_id: Int,
    val su_phone: String
)