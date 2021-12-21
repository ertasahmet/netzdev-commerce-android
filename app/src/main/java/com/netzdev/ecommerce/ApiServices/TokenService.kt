package com.netzdev.ecommerce.ApiServices

import android.content.Context
import com.google.gson.Gson
import com.netzdev.ecommerce.BuildConfig
import com.netzdev.ecommerce.Consts.ApiConsts
import com.netzdev.ecommerce.Models.ApiResponse
import com.netzdev.ecommerce.Models.Introspect
import com.netzdev.ecommerce.Models.Token
import com.netzdev.ecommerce.RetrofitServices.ApiClient
import com.netzdev.ecommerce.RetrofitServices.RetrofitTokenService
import com.netzdev.ecommerce.Utilities.GlobalApp
import com.netzdev.ecommerce.Utilities.HelperService

class TokenService {

    // Token işlemleri için bir service class'ı oluşturduk.
    companion object {

        // RetrofitTokenService class'ından instance alıyoruz.
        private var retrofitTokenService =
            ApiClient.buildService(ApiConsts.authBaseUrl, RetrofitTokenService::class.java, false)

        suspend fun getTokenWithClientCredentials(): ApiResponse<Token> {

            // Burada da try catch ekledik, sorun olursa catch'e düşücek. Catch kısmında da HelperService'teki handleExcetipn metodu sayesinde hatayı ele alıp ona göre cevap dönücez.
            try {
                // Retrofit token service'teki getTokenWithClientCredentials metoduna ilgili bilgilerle istek atıyoruz.
                var response = retrofitTokenService.getTokenWithClientCredentials(
                    BuildConfig.ClientId_CC,
                    BuildConfig.Client_Secret_CC,
                    ApiConsts.clientCredentialGrantType
                )

                // Başarılı değilse successfull'u false dönüyor.
                if (!response.isSuccessful) return ApiResponse(false)

                // Başarılı ise apiResponse ve successful olarak dönüyoruz ve parametre de gelen cevabı Token class'ına çevirip dönüyoruz.
                return ApiResponse(true, response.body() as Token)
            } catch (ex: Exception) {
                return HelperService.handleException(ex)
            }


        }

        fun refreshToken(refreshToken: String): ApiResponse<Token> {

            try {
                // Bu metodu asenkron değil de senkron olarak çalıştıracağımız için suspend kullanmadık ve retrofit sınıfından çağırdığımız metodun sonuna execute ekledik ki direk çalıştırsın.
                var response = retrofitTokenService.refreshToken(
                    BuildConfig.ClientId_ROP,
                    BuildConfig.Client_Secret_ROP,
                    ApiConsts.refreshTokenCredentialGrantType,
                    refreshToken
                ).execute()

                return if (response.isSuccessful) {
                    ApiResponse(true, response.body() as Token)
                } else {
                    ApiResponse(false)
                }
            } catch (ex: Exception) {
                return HelperService.handleException(ex)
            }


        }

        // Unit, void tipinin Kotlin'deki karşılığıdır. Yani geriye birşey dönmeyeceğiz diyor.
        suspend fun checkToken(): ApiResponse<Unit> {
            try {
                var preference = GlobalApp.getAppContext().getSharedPreferences("token_api", Context.MODE_PRIVATE)

                // Burada önce null olabilen bir string tanımlıyoruz ve ?: ile c# taki ?? gibi if'i kontrol ediyoruz. Eğer null ise successful false dönüyor.
                var tokenString: String? =
                    preference.getString("token", null) ?: return ApiResponse(false)

                // Kaydedilen tokenString'i json olarak kaydettiğimiz için burada json'ı Token class'ına çeviriyoruz.
                var token: Token = Gson().fromJson(tokenString, Token::class.java)

                // Basic auth kullanarak istek atacağız, o yüzden basic auth kullanan bir string oluşturduk, kullanıcı adı ve şifreyi girdik.
                var authorization: String =
                    okhttp3.Credentials.basic("resource_product_api", "apisecret")

                // İlgili token'ı basic auth ile istek atıp kontrol ediyoruz ve cevabı alıyoruz.
                var response = retrofitTokenService.checkToken(token.AccessToken, authorization)

                // Başarılı değilse successful false dönüyoruz.
                if (!response.isSuccessful) return ApiResponse(false)

                // Gelen cevabı Introspect class'ına çevirdik.
                var introspect = response.body() as Introspect

                // Gelen introspect cevabında token active değil ise false dönüyoruz.
                if (!introspect.Active) return ApiResponse(false)

                // En son successful true dönüyoruz yani token geçerlidir.
                return ApiResponse(true)
            } catch (ex: Exception) {
                return HelperService.handleException(ex)
            }


        }


    }

}