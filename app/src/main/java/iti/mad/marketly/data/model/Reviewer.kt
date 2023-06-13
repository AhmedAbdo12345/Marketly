package iti.mad.marketly.data.model

import android.os.Parcel
import android.os.Parcelable

data class Reviewer(var name:String,var rate:Double,var img:String,var comment:String):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()?:"",
        parcel.readDouble()?:0.0,
        parcel.readString()?:"",
        parcel.readString()?:""
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeDouble(rate)
        parcel.writeString(img)
        parcel.writeString(comment)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Reviewer> {
        override fun createFromParcel(parcel: Parcel): Reviewer {
            return Reviewer(parcel)
        }

        override fun newArray(size: Int): Array<Reviewer?> {
            return arrayOfNulls(size)
        }
    }
}