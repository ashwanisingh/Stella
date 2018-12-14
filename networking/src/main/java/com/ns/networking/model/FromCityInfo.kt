package com.ns.networking.model

import android.os.Parcel
import android.os.Parcelable

data class FromCityInfo(
    val name: String?,
    val short_name: String?,
    val airport: String?
) : Parcelable {
    constructor(source: Parcel) : this(
        source.readString(),
        source.readString(),
        source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(name)
        writeString(short_name)
        writeString(airport)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<FromCityInfo> = object : Parcelable.Creator<FromCityInfo> {
            override fun createFromParcel(source: Parcel): FromCityInfo = FromCityInfo(source)
            override fun newArray(size: Int): Array<FromCityInfo?> = arrayOfNulls(size)
        }
    }
}