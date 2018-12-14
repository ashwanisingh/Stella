package com.ns.networking.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BoardingPassResponse(
    val `data`: BoardingPassData,
    val message: String,
    val resultcode: Int
):Parcelable

@Parcelize
data class BoardingPassData(
    val boarding_pass: List<Booking>
):Parcelable


