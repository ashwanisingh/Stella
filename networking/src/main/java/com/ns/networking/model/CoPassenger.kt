package com.ns.networking.model

import android.os.Parcel
import android.os.Parcelable

data class CoPassenger(
    val last_modified_by: String?,
    val modified_user_type: String?,
    val name: String?,
    val passenger: Int?,
    val phone: String?,
    val seats_info: SeatsInfo?,
    val status: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readParcelable(SeatsInfo::class.java.classLoader),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(last_modified_by)
        parcel.writeString(modified_user_type)
        parcel.writeString(name)
        if (passenger != null) {
            parcel.writeInt(passenger)
        }
        parcel.writeString(phone)
        parcel.writeParcelable(seats_info, flags)
        parcel.writeString(status)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CoPassenger> {
        override fun createFromParcel(parcel: Parcel): CoPassenger {
            return CoPassenger(parcel)
        }

        override fun newArray(size: Int): Array<CoPassenger?> {
            return arrayOfNulls(size)
        }
    }
}