package com.ns.networking.model

import android.os.Parcel
import android.os.Parcelable

data class ToCityInfo(
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
        val CREATOR: Parcelable.Creator<ToCityInfo> = object : Parcelable.Creator<ToCityInfo> {
            override fun createFromParcel(source: Parcel): ToCityInfo = ToCityInfo(source)
            override fun newArray(size: Int): Array<ToCityInfo?> = arrayOfNulls(size)
        }
    }
}