package com.ns.networking.model


data class BookingHistoryResponse(
    val data: BookingListData,
    val message: String,
    val resultcode: Int
)

data class BookingListData(
    val booking_list: List<Booking>
)














