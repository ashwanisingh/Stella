package com.ns.networking.model

import android.os.Parcel
import android.os.Parcelable

data class SecondaryUserLoginResponse(
    val `data`: SecondaryUserData?,
    val message: String?,
    val resultcode: Int
) : Parcelable {
    constructor(source: Parcel) : this(
        source.readParcelable<SecondaryUserData>(SecondaryUserData::class.java.classLoader),
        source.readString(),
        source.readInt()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeParcelable(data, 0)
        writeString(message)
        writeInt(resultcode)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<SecondaryUserLoginResponse> =
            object : Parcelable.Creator<SecondaryUserLoginResponse> {
                override fun createFromParcel(source: Parcel): SecondaryUserLoginResponse =
                    SecondaryUserLoginResponse(source)

                override fun newArray(size: Int): Array<SecondaryUserLoginResponse?> = arrayOfNulls(size)
            }
    }
}

data class SecondaryUserData(
    val primary_users: List<PrimaryUser>?,
    val refresh_token: String?,
    val token: String?
) : Parcelable {
    constructor(source: Parcel) : this(
        ArrayList<PrimaryUser>().apply { source.readList(this, PrimaryUser::class.java.classLoader) },
        source.readString(),
        source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeList(primary_users)
        writeString(refresh_token)
        writeString(token)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<SecondaryUserData> = object : Parcelable.Creator<SecondaryUserData> {
            override fun createFromParcel(source: Parcel): SecondaryUserData = SecondaryUserData(source)
            override fun newArray(size: Int): Array<SecondaryUserData?> = arrayOfNulls(size)
        }
    }
}

data class PrimaryUser(
    val email: String?,
    val id: Int,
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
        writeInt(id)
        writeString(name)
        writeString(phone)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<PrimaryUser> = object : Parcelable.Creator<PrimaryUser> {
            override fun createFromParcel(source: Parcel): PrimaryUser = PrimaryUser(source)
            override fun newArray(size: Int): Array<PrimaryUser?> = arrayOfNulls(size)
        }
    }
}