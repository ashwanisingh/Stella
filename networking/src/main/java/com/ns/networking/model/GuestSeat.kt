package com.ns.networking.model

import android.os.Parcel
import android.os.Parcelable

data class GuestSeat(
    val email: String?,
    val guest_id: Int,
    val name: String?,
    val phone: String?,
    val seat_code: String?,
    val seat_id: Int
) : Parcelable {
    constructor(source: Parcel) : this(
        source.readString(),
        source.readInt(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readInt()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(email)
        writeInt(guest_id)
        writeString(name)
        writeString(phone)
        writeString(seat_code)
        writeInt(seat_id)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<GuestSeat> = object : Parcelable.Creator<GuestSeat> {
            override fun createFromParcel(source: Parcel): GuestSeat = GuestSeat(source)
            override fun newArray(size: Int): Array<GuestSeat?> = arrayOfNulls(size)
        }
    }
}