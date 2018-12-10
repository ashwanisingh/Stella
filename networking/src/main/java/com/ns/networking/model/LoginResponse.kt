package com.ns.networking.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LoginResponse(
    val `data`: Data,
    val message: String,
    val resultcode: Int
): Parcelable

@Parcelize
data class Data(
    val refresh_token: String,
    val token: String,
    val user_data: UserData
): Parcelable

@Parcelize
data class UserData(
    val addresses: List<Addresse>,
    val cities: List<City>,
    val contacts: List<Contact>,
    val created_at: String,
    val customer_care_info: CustomerCareInfo,
    val customer_prefs: CustomerPrefs,
    val email: String,
    val locked_seats: List<String>,
    val name: String,
    val phone: String,
    val subscriptions: List<Subscription>,
    val user_id: Int
):Parcelable

@Parcelize
data class Subscription(
    val created_at: String,
    val expiry_date: String,
    val no_of_seats: Int
):Parcelable

@Parcelize
data class CustomerPrefs(
    val foods: List<Food>,
    val payment_prefs: String,
    val seat_prefs: List<String>,
    val seats_available: Int,
    val subscription_expiry: Int,
    val subscription_expiry_datetime: Int
):Parcelable

@Parcelize
data class Food(
    val description: String,
    val food_image: String,
    val food_type: String,
    val id: Int,
    val image_path: String,
    val img_url: String,
    val name: String,
    val pref: Boolean
):Parcelable

@Parcelize
data class CustomerCareInfo(
    val email: String,
    val phone: String
):Parcelable

@Parcelize
data class Addresse(
    val address: String,
    val address_tag: String,
    val city: Int,
    val city_info: CityInfo,
    val created_at: String,
    val id: Int,
    val user: Int
):Parcelable

@Parcelize
data class CityInfo(
    val id: Int,
    val name: String
):Parcelable

@Parcelize
data class City(
    val airport: String,
    val id: Int,
    val name: String,
    val short_name: String,
    val status: Int
):Parcelable


@Parcelize
data class Contact(
    val email: String,
    val id: Int,
    val name: String,
    val nick_name: String,
    val phone: String,
    val relationship: String,
    val user: Int,
    val user_info: UserInfo
):Parcelable

@Parcelize
data class UserInfo(
    val id: Int,
    val name: String
) : Parcelable