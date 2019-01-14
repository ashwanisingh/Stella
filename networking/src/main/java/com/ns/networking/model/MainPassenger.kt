package com.ns.networking.model

import android.os.Parcel
import android.os.Parcelable

data class MainPassenger(
    val food_items: List<FoodItems>?,
    val name: String?
) : Parcelable {
    constructor(source: Parcel) : this(
        source.createTypedArrayList(FoodItems.CREATOR),
        source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeTypedList(food_items)
        writeString(name)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<MainPassenger> = object : Parcelable.Creator<MainPassenger> {
            override fun createFromParcel(source: Parcel): MainPassenger = MainPassenger(source)
            override fun newArray(size: Int): Array<MainPassenger?> = arrayOfNulls(size)
        }
    }
}