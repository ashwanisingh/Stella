package com.ns.networking.model

import android.os.Parcel
import android.os.Parcelable

data class FoodItems(
    val food_type: String?,
    val food_type_text: String?,
    val id: Int,
    val name: String?
) : Parcelable {
    constructor(source: Parcel) : this(
        source.readString(),
        source.readString(),
        source.readInt(),
        source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(food_type)
        writeString(food_type_text)
        writeInt(id)
        writeString(name)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<FoodItems> = object : Parcelable.Creator<FoodItems> {
            override fun createFromParcel(source: Parcel): FoodItems = FoodItems(source)
            override fun newArray(size: Int): Array<FoodItems?> = arrayOfNulls(size)
        }
    }
}