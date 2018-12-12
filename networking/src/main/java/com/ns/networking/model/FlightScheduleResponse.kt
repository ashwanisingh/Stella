package com.ns.networking.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FlightScheduleResponse(
    val data: List<FlightScheduleData>,
    val message: String,
    val resultcode: Int
):Parcelable

@Parcelize
data class FlightScheduleData(
    val arrival_time: String,
    val direction: String,
    val flight_id: Int,
    val flight_seat_availability: FlightSeatAvailability,
    val journey_date: String,
    val journey_datetime_ms: Long,
    val journey_time: String,
    val sun_rise_set: String
):Parcelable

@Parcelize
data class FlightSeatAvailability(
    val available_seats: Int,
    val booked: List<Int>,
    val locked: List<Int>,
    val total_seats: Int
):Parcelable