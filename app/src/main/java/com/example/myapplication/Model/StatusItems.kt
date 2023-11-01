package com.example.myapplication.Model

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable

open class StatusItems(
    val uri: Uri?,
    val title:String,
    val path:String,
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readParcelable(Uri::class.java.classLoader),
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(uri, flags)
        parcel.writeString(title)
        parcel.writeString(path)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<StatusItems> {
        override fun createFromParcel(parcel: Parcel): StatusItems {
            return StatusItems(parcel)

        }

        override fun newArray(size: Int): Array<StatusItems?> {
            return arrayOfNulls(size)
        }
    }
}