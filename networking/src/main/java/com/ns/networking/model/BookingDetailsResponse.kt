package com.ns.networking.model

data class BookingDetailsResponse(
    val `data`: BookingDetailsData,
    val message: String,
    val resultcode: Int
)

data class BookingDetailsData(
    val details: List<Booking>
)

/*
data class Detail(
    val arrival_time: String,
    val assigned_drivers: List<Any>,
    val booked_by: String,
    val booked_by_user_type: String,
    val booking_created_at: String,
    val booking_id: Int,
    val customer: String,
    val customer_seat: CustomerSeat,
    val departure_datetime: String,
    val drop_address: String,
    val drop_address_main: String,
    val flight: String,
    val flight_id: Int,
    val flight_no: String,
    val from_city: Int,
    val from_city_info: FromCityInfo,
    val guest_seats: List<Any>,
    val guests: List<Any>,
    val journey_date: String,
    val journey_datetime: Long,
    val journey_time: String,
    val pick_address: String,
    val pick_address_main: String,
    val prefs: Prefs,
    val schedule_id: Int,
    val seats: List<Seat>,
    val service: String,
    val status: String,
    val to_city: Int,
    val to_city_info: ToCityInfo,
    val transaction_id: Int,
    val travelling_self: Int,
    val trip_id: Int,
    val user: Int
)

data class ToCityInfo(
    val airport: String,
    val name: String,
    val short_name: String
)

data class FromCityInfo(
    val airport: String,
    val name: String,
    val short_name: String
)

data class CustomerSeat(
    val seat_code: String,
    val seat_id: Int
)

data class Prefs(
    val co_passengers: List<Any>,
    val main_passenger: MainPassenger
)

data class MainPassenger(
    val food_items: List<FoodItem>,
    val name: String,
    val phone: String,
    val seats_info: SeatsInfo
)

data class SeatsInfo(
    val seat_code: String,
    val seat_id: Int
)

data class FoodItem(
    val food_type: String,
    val food_type_text: String,
    val id: Int,
    val name: String
)

data class Seat(
    val seat_code: String,
    val seat_id: Int
)*/
