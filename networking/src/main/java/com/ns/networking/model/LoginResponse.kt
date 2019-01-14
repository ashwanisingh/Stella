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
    val locked_seats: List<LockedSeats>?,
    val name: String,
    val phone: String,
    val subscriptions: List<Subscription>,
    val user_id: Int
):Parcelable

@Parcelize
data class Subscription(
    val created_at: String?,
    val expiry_date: String?,
    val no_of_seats: Int?
):Parcelable

@Parcelize
data class CustomerPrefs(
    val foods: List<Food>?,
    val payment_prefs: String?,
    val seat_prefs: List<SeatPreferences>?,
    val seats_available: Int?,
    val subscription_expiry: Long?,
    val subscription_expiry_datetime: String?
):Parcelable

@Parcelize
data class SeatPreferences(
    val flight: Flight?,
    val flight_id: Int?,
    val id: Int?,
    val seat_code: String?,
    val sort_order: Int?
):Parcelable


@Parcelize
data class Food(
    val description: String?,
    val food_image: String?,
    val food_type: String?,
    val food_type_text: String?,
    val id: Int?,
    val image_path: String?,
    val img_url: String?,
    val name: String?,
    var pref: Boolean?
):Parcelable

@Parcelize
data class CustomerCareInfo(
    val email: String?,
    val phone: String?
):Parcelable

@Parcelize
data class Addresse(
    val address: String?,
    val address_tag: String?,
    val city: Int?,
    val city_info: CityInfo?,
    val created_at: String?,
    val id: Int?,
    val user: Int?
):Parcelable

@Parcelize
data class CityInfo(
    val id: Int?,
    val name: String?
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
    val email: String?,
    val id: Int?,
    val name: String?,
    val nick_name: String?,
    val phone: String?,
    val relationship: String?,
    val user: Int?,
    val user_info: UserInfo?
):Parcelable

@Parcelize
data class UserInfo(
    val id: Int?,
    val name: String?
) : Parcelable

@Parcelize
data class LockedSeats(
    val flight: Flight?,
    val flight_id: Int?,
    val flight_seat: FlightSeat?,
    val flight_seat_id: Int?,
    val from_city: Int?,
    val id: Int?,
    val journey_date: String?,
    val journey_time: String?,
    val seat_reserved_at: String?,
    val status: Int?,
    val to_city: Int?,
    val user: Int?,
    val datetime_ms : Long? ,
    val expire_within_s : Int?,
    val direction : String?,
    val sun_rise_set : String?,
    val arrival_time : String?
):Parcelable

@Parcelize
data class Flight(
    val flight_no: String?,
    val id: Int?,
    val model: String?,
    val no_of_seats: Int?
):Parcelable

@Parcelize
data class FlightSeat(
    val id: Int?,
    val seat_code: String?
):Parcelable