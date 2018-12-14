package com.ns.networking.model

import android.os.Parcel
import android.os.Parcelable

data class CustomerSeat(
    val seat_code: String?,
    val seat_id: Int
) : Parcelable {
    constructor(source: Parcel) : this(
        source.readString(),
        source.readInt()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(seat_code)
        writeInt(seat_id)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<CustomerSeat> = object : Parcelable.Creator<CustomerSeat> {
            override fun createFromParcel(source: Parcel): CustomerSeat = CustomerSeat(source)
            override fun newArray(size: Int): Array<CustomerSeat?> = arrayOfNulls(size)
        }
    }
}