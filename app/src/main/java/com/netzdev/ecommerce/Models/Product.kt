package com.netzdev.ecommerce.Models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

// Burada burayı parcelize olarak işaretledik ve Parcelable sınıfından miras aldık. Bunun amacı da navigatorHost ile fragmentler arası güvenli bir şekilde bu class'ı taşımamız gerekiyor. Bu yüzden ekledik.
@Parcelize
data class Product(
    @SerializedName("Id") var Id: Int,
    @SerializedName("Name") var Name: String,
    @SerializedName("Price") var Price: Double,
    @SerializedName("Color") var Color: String,
    @SerializedName("Stock") var Stock: Int,
    @SerializedName("PhotoPath") var PhotoPath: String,
    @SerializedName("Category_Id") var CategoryId: Int,
    @SerializedName("Category") var Category: Category?
) : Parcelable {}