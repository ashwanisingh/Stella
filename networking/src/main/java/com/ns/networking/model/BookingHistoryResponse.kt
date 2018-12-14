package com.ns.networking.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BookingHistoryResponse(
    val data: BookingListData,
    val message: String,
    val resultcode: Int
):Parcelable

@Parcelize
data class BookingListData(
    val booking_list: List<Booking>
):Parcelable

@Parcelize
data class Booking(
    val arrival_time: String,
    val booked_by: String,
    val booking_created_at: String,
    val booking_id: Int,
    val customer: String,
    val customer_seat: CustomerSeat,
    val drop_address: String,
    val drop_address_main: String,
    val flight: String,
    val flight_id: Int,
    val flight_no: String,
    val from_city: Int,
    val from_city_info: FromCityInfo,
    val guest_seats: List<GuestSeat>,
    val guests: List<Guest>,
    val journey_date: String,
    val journey_datetime: Long,
    val journey_time: String,
    val pick_address: String,
    val pick_address_main: String,
    val prefs: Prefs,
    val service: String,
    val status: String,
    val to_city: Int,
    val to_city_info: ToCityInfo,
    val transaction_id: Int,
    val travelling_self: Int,
    val trip_id: Int,
    val user: Int
):Parcelable

@Parcelize
data class ToCityInfo(
    val name: String,
    val short_name: String,
    val airport : String
):Parcelable

@Parcelize
data class FromCityInfo(
    val name: String,
    val short_name: String,
    val airport : String
):Parcelable

@Parcelize
data class Guest(
    val email: String,
    val guest_id: Int,
    val name: String,
    val phone: String
):Parcelable

@Parcelize
data class CustomerSeat(
    val seat_code: String,
    val seat_id: Int
): Parcelable

@Parcelize
data class GuestSeat(
    val email: String,
    val guest_id: Int,
    val name: String,
    val phone: String,
    val seat_code: String,
    val seat_id: Int
):Parcelable

@Parcelize
data class Prefs(
    val co_passengers: List<CoPassenger>,
    val main_passenger: MainPassenger
):Parcelable

@Parcelize
data class MainPassenger(
    val food_items: FoodItems,
    val name: String
):Parcelable

@Parcelize
data class FoodItems(
    val food_type: String,
    val food_type_text: String,
    val id: Int,
    val name: String
):Parcelable

@Parcelize
data class CoPassenger(
    val food_items: FoodItems,
    val name: String
):Parcelable