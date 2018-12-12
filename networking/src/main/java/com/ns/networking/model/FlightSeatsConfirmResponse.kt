package com.ns.networking.model

data class FlightSeatsConfirmResponse(
    val `data`: FlightSeatsConfirmData,
    val message: String,
    val resultcode: Int
)

data class FlightSeatsConfirmData(
    val flight_seat_availability: FlightSeatAvailability
)
