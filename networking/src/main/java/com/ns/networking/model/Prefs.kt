package com.ns.networking.model

import android.os.Parcel
import android.os.Parcelable

data class Prefs(
    val co_passengers: List<CoPassenger>?,
    val main_passenger: MainPassenger?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.createTypedArrayList(CoPassenger),
        parcel.readParcelable(MainPassenger::class.java.classLoader)
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedList(co_passengers)
        parcel.writeParcelable(main_passenger, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Prefs> {
        override fun createFromParcel(parcel: Parcel): Prefs {
            return Prefs(parcel)
        }

        override fun newArray(size: Int): Array<Prefs?> {
            return arrayOfNulls(size)
        }
    }
}