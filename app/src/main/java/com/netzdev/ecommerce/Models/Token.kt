package com.netzdev.ecommerce.Models

import com.google.gson.annotations.SerializedName

// Burada yine data class tanımladık ver SerializedName attribute'ü ile gelen değerlerde ilgili keyword'a sahip değerleri istediğimiz isimdeki
// parametrelere aktarıyor.
data class Token(
        @SerializedName("access_token") var AccessToken: String,
        @SerializedName("expires_in") var Expires: Int,
        @SerializedName("token_type") var TokenType: String,
        @SerializedName("refresh_token") var RefreshToken: String,
        @SerializedName("scope") var Scope: String
) {

}