package com.ns.networking.model

import android.os.Parcel
import android.os.Parcelable

data class CoPassenger(
    val food_items: FoodItems?,
    val name: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readParcelable(FoodItems::class.java.classLoader),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(food_items, flags)
        parcel.writeString(name)
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