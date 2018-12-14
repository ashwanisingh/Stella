package com.ns.networking.model

import android.os.Parcel
import android.os.Parcelable

data class Guest(
    val email: String?,
    val guest_id: Int,
    val name: String?,
    val phone: String?
) : Parcelable {
    constructor(source: Parcel) : this(
        source.readString(),
        source.readInt(),
        source.readString(),
        source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(email)
        writeInt(guest_id)
        writeString(name)
        writeString(phone)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Guest> = object : Parcelable.Creator<Guest> {
            override fun createFromParcel(source: Parcel): Guest = Guest(source)
            override fun newArray(size: Int): Array<Guest?> = arrayOfNulls(size)
        }
    }
}