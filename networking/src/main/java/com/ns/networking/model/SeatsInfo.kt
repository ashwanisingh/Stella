package com.ns.networking.model

import android.os.Parcel
import android.os.Parcelable

data class SeatsInfo(
    val seat_code: String?,
    val seat_id: Int?
) : Parcelable {
    constructor(source: Parcel) : this(
        source.readString(),
        source.readValue(Int::class.java.classLoader) as Int?
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(seat_code)
        writeValue(seat_id)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<SeatsInfo> = object : Parcelable.Creator<SeatsInfo> {
            override fun createFromParcel(source: Parcel): SeatsInfo = SeatsInfo(source)
            override fun newArray(size: Int): Array<SeatsInfo?> = arrayOfNulls(size)
        }
    }
}