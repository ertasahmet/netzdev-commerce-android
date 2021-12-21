package com.netzdev.ecommerce.ApiServices

import android.provider.SyncStateContract
import com.netzdev.ecommerce.BuildConfig
import com.netzdev.ecommerce.Consts.ApiConsts
import com.netzdev.ecommerce.Models.ApiResponse
import com.netzdev.ecommerce.Models.Token
import com.netzdev.ecommerce.Models.UserSignIn
import com.netzdev.ecommerce.Models.UserSignUp
import com.netzdev.ecommerce.RetrofitServices.ApiClient
import com.netzdev.ecommerce.RetrofitServices.RetrofitLoginService
import com.netzdev.ecommerce.RetrofitServices.RetrofitTokenService
import com.netzdev.ecommerce.Utilities.HelperService

// Login işlemleri için bir service oluşturduk.
class LoginService {

    companion object {

        // Önce retrofitLoginService class'ından instance ürettik.
        private var retrofitLoginService =
            ApiClient.buildService(ApiConsts.authBaseUrl, RetrofitLoginService::class.java, false)

        // SignUp metodu tanımladık.
        suspend fun signUp(userSignUp: UserSignUp): ApiResponse<Unit> {

            try {
                // TokenService'teki identity projesine istek atmak için gerekli token'ı alan metodu çağırdık.
                var tokenResponse = TokenService.getTokenWithClientCredentials()

                // Cevap successful değilse false döndük.
                if (!tokenResponse.isSuccessful) return ApiResponse(false, failure = tokenResponse.failure)

                // Response'un içindeki token class'ını aldık.
                var token = tokenResponse.success!!

                // İlgili bilgilerle signUp metoduna istek attık.
                var signUpResponse =
                    retrofitLoginService.signUp(userSignUp, "bearer ${token.AccessToken}")

                // Success değilse false döndük.
                if (!signUpResponse.isSuccessful) return HelperService.handleApiError(signUpResponse)

                // En son true döndük.
                return ApiResponse(true)
            } catch (ex : Exception){
                return HelperService.handleException(ex)
            }
        }

        // SignIn metodu oluşturduk.
        suspend fun signIn(userSignIn: UserSignIn): ApiResponse<Unit> {

            try {
                // İlgili bilgilerle Login Service'e istek attık.
                var response = retrofitLoginService.signIn(
                    BuildConfig.ClientId_ROP,
                    BuildConfig.Client_Secret_ROP,
                    ApiConsts.resourceOwnerPasswordCredentialGrantType,
                    userSignIn.Email,
                    userSignIn.Password
                )

                if (!response.isSuccessful) return HelperService.handleApiError(response)

                // Gelen cevabı Token class'ına çevirip return yaptık.
                var token = response.body() as Token

                HelperService.saveTokenSharedPreference(token)

                return ApiResponse(true)
            } catch (ex : Exception){
                return HelperService.handleException(ex)
            }



        }
    }

}