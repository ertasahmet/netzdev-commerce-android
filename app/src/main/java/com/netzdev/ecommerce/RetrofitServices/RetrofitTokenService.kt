package com.netzdev.ecommerce.RetrofitServices

import com.netzdev.ecommerce.Models.Introspect
import com.netzdev.ecommerce.Models.Token
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface RetrofitTokenService {

    @POST("connect/token")
    @FormUrlEncoded
    suspend fun getTokenWithClientCredentials (
        @Field("client_id") clientId:String,
        @Field("client_secret") clientSecret:String,
        @Field("grant_type") grantType:String
    ) : Response<Token>

    @POST("connect/introspect")
    @FormUrlEncoded
    suspend fun checkToken(
        @Field("token") token : String,
        @Header("Authorization") authorization : String
    ) : Response<Introspect>

    // Burada suspend kullanmıyoruz çünkü metodun senkron olmasını istiyoruz. Onun sebebi de refresh token'a geçerli olup olmadığına bakıcak. Bu olayı interceptor kısmında detaylı anlatıcak.
    @POST("connect/token")
    @FormUrlEncoded
    fun refreshToken (
        @Field("client_id") clientId:String,
        @Field("client_secret") clientSecret:String,
        @Field("grant_type") grantType:String,
        @Field("refresh_token") refreshToken:String
    ) : Call<Token>
}