package com.netzdev.ecommerce.Models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Category(
    @SerializedName("Id") var Id: Int,
    @SerializedName("Name") var Name: String
) : Parcelable {

    override fun toString(): String {
        return Name
    }

}