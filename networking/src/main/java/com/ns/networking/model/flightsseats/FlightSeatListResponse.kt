package com.ns.networking.model.flightsseats

import com.ns.networking.model.FlightSeat

data class FlightSeatListResponse(
    val data: FlightSeatData,
    val message: String,
    val resultcode: Int
)

data class FlightSeatData(
    val flight_details: FlightDetails,
    val flight_id: String,
    val flight_seat_availability: FlightSeatAvailability,
    val flight_seats: List<FlightSeats>,
    val seats_locked_by_user: List<SeatsLockedByUser>
)

data class FlightSeats(
    val flight_id: Int,
    val id: Int,
    val layout_path: String,
    val seat_code: String,
    val seat_layout: String,
    val sort_order: Int
)

data class SeatsLockedByUser(
    val flight_seat_id: Int,
    val seat_reserved_at: String,
    val seat_reserved_at_ms: Long
)

data class FlightSeatAvailability(
    val available_seats: Int,
    val booked: List<Int>,
    val locked: List<Int>,
    val total_seats: Int
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