package com.netzdev.ecommerce.Utilities

import android.content.Context
import android.widget.Toast
import com.google.gson.Gson
import com.netzdev.ecommerce.Exceptions.OfflineException
import com.netzdev.ecommerce.Models.ApiError
import com.netzdev.ecommerce.Models.ApiResponse
import com.netzdev.ecommerce.Models.Token
import com.netzdev.ecommerce.R
import retrofit2.Response

class HelperService {

    companion object {

        fun <T> handleException(ex: Exception): ApiResponse<T> {

            // Burada exception olduğu sürece bu when'e giriyor.
            return when (ex) {

                // Buranın anlamı kullanıcının interneti yoksa OfflineException fırlatıyor.
                // Burada diyoruz ki ex tipi Offline exception ise c#'taki => lambda gibi burada -> ile gir.
                is OfflineException -> {

                    // String'lerden ilgili hata mesajını alıyoruz ve listeye ekliyoruz. Buradaki val ise let keywordu ile aynı anlama geliyor. Değiştirmeyeceğimiz bir değişken ise val kullanmak daha uygundur.
                    val exMessage =
                        arrayListOf(GlobalApp.getAppContext().resources.getString(R.string.ex_no_exception))

                    // Sonra bir apiError nesnesi oluşturup listeyi oraya koyuyoruz ve isShow true diyoruz.
                    var apiError = ApiError(exMessage, 500, true)

                    // Sonra ApiResponse sınıfı oluşturup error nesnesini ona veriyoruz.
                    ApiResponse(false, failure = apiError)
                }

                else -> {
                    val exMessage =
                        arrayListOf(GlobalApp.getAppContext().resources.getString(R.string.ex_common_error))

                    // Sonra bir apiError nesnesi oluşturup listeyi oraya koyuyoruz ve isShow true diyoruz.
                    var apiError = ApiError(exMessage, 500, true)

                    // Sonra ApiResponse sınıfı oluşturup error nesnesini ona veriyoruz.
                    ApiResponse(false, failure = apiError)
                }
            }
        }


        // Burada bize gönderilen token nesnesini Gson kütüphanesi ile json şeklinde bir string olarak sharedPreferences'a kaydettik.
        fun saveTokenSharedPreference(token: Token) {

            var preference =
                GlobalApp.getAppContext().getSharedPreferences("token_api", Context.MODE_PRIVATE)
            var editor = preference.edit()
            editor.putString("token", Gson().toJson(token))
            editor.apply()
        }

        fun getTokenSharedPreference(): Token? {
            var preference = GlobalApp.getAppContext().getSharedPreferences("token_api", Context.MODE_PRIVATE)

            var tokenString: String? = preference.getString("token", null) ?: return null

            return Gson().fromJson(tokenString, Token::class.java)
        }

        // Api'den gelen hataları ilgili modele atmak için bir model yazdık. Parametre olarak retrofit'in isteğinden gelen response class'ından nesne aldık.
        fun <T1, T2> handleApiError(response: Response<T1>): ApiResponse<T2> {

            // ApiError sınıfından nesne türettik.
            var apiError: ApiError? = null

            // Response'un erroBody kısmı null değilse
            if (response.errorBody() != null) {

                // Response'un errorBody'sini okuduk.
                var errorBody = response.errorBody()!!.string()

                // Gelen error body'sini bizim Api Error sınıfımıza cast ettik.
                apiError = Gson().fromJson(errorBody, ApiError::class.java)
            }

            // Geriye yine ApiResponse dönüyoruz fakat apiError nesnesi bu sefer dolu oluyor.
            return ApiResponse(false, null, apiError)
        }

        fun showErrorMessageByToast(apiError: ApiError?) {
            if (apiError == null) return

            var errorBuilder = StringBuilder()

            if (apiError.IsShow) {
                for (error in apiError.Errors) {
                    errorBuilder.append(error + "\n")
                }
            }

            Toast.makeText(GlobalApp.getAppContext(), errorBuilder.toString(), Toast.LENGTH_LONG)
                .show()

        }
    }

}