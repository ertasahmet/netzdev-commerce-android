package com.netzdev.ecommerce.Interceptors

import com.netzdev.ecommerce.Exceptions.OfflineException
import com.netzdev.ecommerce.Utilities.LiveNetworkListener
import okhttp3.Interceptor
import okhttp3.Response

class NetworkInterceptor : Interceptor{

    // Interceptor class'ından miras alan custom bir interceptor yazdık. Intercept metodunu override ettik. Bu interceptor'ın amacı retrofit ile istek atılırken araya girip bişeyley yapmaktır.
    override fun intercept(chain: Interceptor.Chain): Response {

        // Biz de araya girdik dedik ki eğer internet bağlantısı yoksa bizim custom exception'ımız olan OfflineException fırlat.
        if(!LiveNetworkListener.isConnected()){
            throw OfflineException("")
        }

        // Request'i alıyoruz ve chain.proceed diye isteğe devam et diyoruz.
        var request = chain.request()
        return chain.proceed(request)
    }

}