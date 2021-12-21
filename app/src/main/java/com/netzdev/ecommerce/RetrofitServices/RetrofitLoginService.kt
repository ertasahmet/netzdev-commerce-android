package com.netzdev.ecommerce.RetrofitServices

import com.netzdev.ecommerce.Models.Token
import com.netzdev.ecommerce.Models.UserSignUp
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface RetrofitLoginService {

    // Burada da retrofit service'i için bir interface oluşturduk. Post olarak göndereceğimizi söyleyip baseUrl'den sonraki adresini gösterdik.
    // Parametre olarak body kısmında userSignUp modeli alıyor yani kullanıcı adı, şifre, email falan. Header kısmında da authorization için
    // token göndereceğiz. Metodun başına koyduğumuz suspend olayı ise bu metodu c# taki async await olayı gibi asenkron metodları
    // normal senkronmuş gibi tek satırda çağırmamızı ve enqueue metoduyla callback olayını düşmememizi sağlıyor. Bu sayede çok kullanışlı
    // ve bu olayın adı Coroutine. Dönüş tipi olarak da Retrofit'in Response'u içinde ResponseBody dönüyoruz.
    @POST("api/user/signup")
    suspend fun signUp(@Body userSignUp: UserSignUp, @Header("Authorization") authorization:String) : Response<ResponseBody>

    // Burada bir post metodu daha tanımladık ve parametreleri postmandeki gibi FormUrlEncoded kısmında göndermemiz gerekiyordu, json gönderseydik
    // yukarıdaki gibi body ile modeli gönderebilirdik fakat farklı farklı parametreler göndermek gerekiyor. O yüzden FormUrlEncoded ile
    // işaretledik ve her bir parametreyi de Field ile gösterdik. Dönüş olarak da Token modeli döndük.
    @POST("connect/token")
    @FormUrlEncoded
    suspend fun signIn (
            @Field("client_id") clientId:String,
            @Field("client_secret") clientSecret:String,
            @Field("grant_type") grantType:String,
            @Field("username") userName:String,
            @Field("password") password:String
    ) : Response<Token>

}