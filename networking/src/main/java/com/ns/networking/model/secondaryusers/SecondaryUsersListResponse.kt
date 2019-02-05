package com.ns.networking.model.secondaryusers

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class SecondaryUsersListResponse(
    val `data`: SecondaryUsersData,
    val message: String,
    val resultcode: Int
)

data class SecondaryUsersData(
    val primary_user: String,
    val secondary_users: List<SecondaryUserInfoList>
)

@Parcelize
data class SecondaryUserInfoList(
    val su_name: String,
    val su_email: String,
    val su_id: Int,
    val su_phone: String
):Parcelable

data class SecondaryUser(
    val su_name: String,
    val su_email: String,
    val su_id: Int,
    val su_phone: String
)