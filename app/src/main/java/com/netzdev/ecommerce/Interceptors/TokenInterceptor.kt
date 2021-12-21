package com.netzdev.ecommerce.Interceptors

import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.gson.Gson
import com.netzdev.ecommerce.ApiServices.TokenService
import com.netzdev.ecommerce.Models.Token
import com.netzdev.ecommerce.UI.Login.LoginActivity
import com.netzdev.ecommerce.Utilities.GlobalApp
import com.netzdev.ecommerce.Utilities.HelperService
import okhttp3.Interceptor
import okhttp3.Response

class TokenInterceptor : Interceptor {

    // Token için bir interceptor yapıyoruz.
    override fun intercept(chain: Interceptor.Chain): Response {

        // Bir token nesnesi oluşturuyoruz.
        var token: Token? = null

        // Var olan request'e erişiyoruz.
        var request = chain.request()

        // SharedPreferences'e erişip token'ı alıyoruz.
        var preference = GlobalApp.getAppContext().getSharedPreferences("token_api", Context.MODE_PRIVATE)
        var tokenString = preference.getString("token", null)

        // Token varsa buraya giriyoruz.
        if (tokenString != null) {

            // Loglama yapıyoruz.
            Log.i("OkHttp", "token sharedpreference'den okundu")

            // SharedPreferences2ten aldığımız token string'ini token nesnesine cast ediyoruz.
            token = Gson().fromJson(tokenString, Token::class.java)!!

            // İlgili request'in header'ına Authorization olarak bearer token'ı ekliyoruz.
            request = request.newBuilder().addHeader("Authorization", "Bearer ${token.AccessToken}")
                .build()
        }

        // Burada da request'e devam et diyoruz ve response'u alıyoruz.
        var response = chain.proceed(request)

        // Eğer response 401 ise yani token geçerli değilse
        if (response.code == 401) {

            // Loglama yapıyoruz.
            Log.i("OkHttp", "AccessToken geçersiz 401'e girdi.")

            // Önce token varsa ve geçerli değilse'yi kontrol ediyoruz ona göre refresh token ile istek atıcaz. Eğer o da geçerli değilse zaten tekrardan login ekranına dönmesi gerekiyor.
            if (token != null) {

                // RefreshToken ile istek atıyoruz ve bu istek senkrondur yani cevap gelene kadar burada bekliyor kodlar.
                var apiResponse = TokenService.refreshToken(token.RefreshToken)

                // Eğer gelen cevap başarılı ise
                if (apiResponse.isSuccessful) {

                    // Token string'i SharedPreferences'e kaydediyoruz.
                    HelperService.saveTokenSharedPreference(apiResponse.success!!)

                    // Token class'ına cast ediyoruz gelen token cevabını
                    var newToken = apiResponse.success!!

                    // 401 alan ilgili request'ı tekrardan token ekleyip istek atıyoruz.
                    request = request.newBuilder()
                        .addHeader("Authorization", "Bearer ${newToken.AccessToken}").build()

                    // Ve cevabı alıyoruz.
                    response = chain.proceed(request)

                } else {

                    // Burada iki token da geçersiz ise logi nactivity'e yönlendiriyoruz. Flag olarak da NEW TASK diyerek bunu yeni bir activity olduğunu belirtiyoruz çünkü şuan interceptor içerisindeyiz, bu şekilde anca activity çağırabiliriz.
                    var intent = Intent(GlobalApp.getAppContext(), LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    GlobalApp.getAppContext().startActivity(intent)
                }
            }
        }

        // Burada da cevabı dönüyoruz.
        return response
    }

}