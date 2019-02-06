package com.ns.networking.model

import android.os.Parcel
import android.os.Parcelable

data class Booking(
    val arrival_time: String?,
    val booked_by: String?,
    val booking_created_at: String?,
    val booking_id: Int,
    val customer: String?,
    val customer_seat: CustomerSeat?,
    var drop_address: String?,
    val drop_address_main: String?,
    val flight: String?,
    val flight_id: Int,
    val flight_no: String?,
    val from_city: Int,
    val from_city_info: FromCityInfo?,
    val guest_seats: List<GuestSeat>?,
    val guests: List<Guest>?,
    val journey_date: String?,
    val journey_datetime: Long,
    val journey_time: String?,
    var pick_address: String?,
    val pick_address_main: String?,
    val prefs: Prefs?,
    val service: String?,
    val schedule_id: String?,
    val status: String?,
    val to_city: Int,
    val to_city_info: ToCityInfo?,
    val transaction_id: Int,
    val travelling_self: Int,
    val trip_id: Int,
    val user: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readParcelable(CustomerSeat::class.java.classLoader),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readParcelable(FromCityInfo::class.java.classLoader),
        parcel.createTypedArrayList(GuestSeat.CREATOR),
        parcel.createTypedArrayList(Guest.CREATOR),
        parcel.readString(),
        parcel.readLong(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readParcelable(Prefs::class.java.classLoader),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readParcelable(ToCityInfo::class.java.classLoader),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(arrival_time)
        parcel.writeString(booked_by)
        parcel.writeString(booking_created_at)
        parcel.writeInt(booking_id)
        parcel.writeString(customer)
        parcel.writeParcelable(customer_seat, flags)
        parcel.writeString(drop_address)
        parcel.writeString(drop_address_main)
        parcel.writeString(flight)
        parcel.writeInt(flight_id)
        parcel.writeString(flight_no)
        parcel.writeInt(from_city)
        parcel.writeParcelable(from_city_info, flags)
        parcel.writeTypedList(guest_seats)
        parcel.writeTypedList(guests)
        parcel.writeString(journey_date)
        parcel.writeLong(journey_datetime)
        parcel.writeString(journey_time)
        parcel.writeString(pick_address)
        parcel.writeString(pick_address_main)
        parcel.writeParcelable(prefs, flags)
        parcel.writeString(service)
        parcel.writeString(schedule_id)
        parcel.writeString(status)
        parcel.writeInt(to_city)
        parcel.writeParcelable(to_city_info, flags)
        parcel.writeInt(transaction_id)
        parcel.writeInt(travelling_self)
        parcel.writeInt(trip_id)
        parcel.writeInt(user)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Booking> {
        override fun createFromParcel(parcel: Parcel): Booking {
            return Booking(parcel)
        }

        override fun newArray(size: Int): Array<Booking?> {
            return arrayOfNulls(size)
        }
    }
}