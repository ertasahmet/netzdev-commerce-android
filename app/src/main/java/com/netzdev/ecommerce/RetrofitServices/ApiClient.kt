package com.netzdev.ecommerce.RetrofitServices

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.netzdev.ecommerce.Interceptors.NetworkInterceptor
import com.netzdev.ecommerce.Interceptors.TokenInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient {

    // Api Client için bir class açtık. Burada companion object diye birşey tanımladık. Bu c# ta static bir eleman tanımlamakla aynı işlemi görür.
    // Bu parantezin içine yazdıklarımız direk class ismiyle erişilebiliyor.
    companion object {

        // Generic tip alan bir metod tanımladık, baseUrl alıyor, çalışacağı servisi alıyor ve interceptor olsun mu olmasın mı onu söylüyor.
        fun<T> buildService(baseUrl: String, retrofitService: Class<T>, existInterceptor: Boolean) : T {

            // ClientBuilder oluşturduk ve bir logging interceptor ekledik. Retrofitin logginginterceptor diye kütüphanesi var onu import ettik ve sonra buraya ekledik. Bu sayede her api isteğini log'da göreceğiz. Sonra network interceptor yazdık ve ekledik.
            var clientBuilder = OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
                .addInterceptor(NetworkInterceptor())

            // TokenInterceptor'ı kullanmak için existInterceptor true ise token'ı ekliyoruz.
            if(existInterceptor) {
                clientBuilder.addInterceptor(TokenInterceptor())
            }

            // Burada da Retrofit ile Builder işlemi yapıyoruz, baseUrl'i yazıyoruz, client'i ekliyoruz, GsonConverter edkliyoruz ve
            // bize gönderilen servisi ekliyoruz.
            return Retrofit.Builder().baseUrl(baseUrl).client(clientBuilder.build())
                    .addConverterFactory(GsonConverterFactory.create()).build().create(retrofitService);
        }
    }

}