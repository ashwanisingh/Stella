package com.ns.networking.model

data class FlightSeatResponse(
    val data: FlightSeatData,
    val message: String,
    val resultcode: Int
)

data class FlightSeatData(
    val flight_details: FlightDetails,
    val flight_id: String,
    val flight_seat_availability: FlightSeatAvailability,
    val flight_seats: List<FlightSeatAlignment>
)

data class FlightSeatAlignment(
    val flight_id: Int,
    val id: Int,
    val layout_path: String,
    val seat_code: String,
    val seat_layout: String,
    val sort_order: Int
)

data class FlightDetails(
    val flight_no: String,
    val id: Int,
    val layout: String,
    val layout_path: String,
    val model: String,
    val name: String,
    val no_of_seats: Int
)