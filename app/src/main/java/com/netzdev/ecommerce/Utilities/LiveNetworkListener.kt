package com.netzdev.ecommerce.Utilities

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

class LiveNetworkListener {

    companion object {

        // Burada internet bağlantısı tipini almak için method yazıyoruz. Eğer sonuç 0 ise internet yok, 0 dan farklı ise internet var demektir.
        private fun getConnectionType(context: Context) : Int {
            var result = 0

            // Önce context'ten Connectivity Service'e bağlanıyoruz.
            var cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?

            // Burada kotlin'in run metodu ile bu nesneye araya girebiliyoruz.
            cm.run {

                // Burada this diyerek cm değişkenini yani ConnectivityManager'i kastediyoruz. Burada da getNetworkCapabilities ile run çalıştırıyoruz ki internet tipini aldığı zaman yakalayabilelim.
                this?.getNetworkCapabilities(this.activeNetwork)?.run {

                    // Eğer iletişim varsa ve wifi ile internet varsa 1 dönüyoruz.
                    if(hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        result = 1
                    }

                    // Telefonun mobil verisi ile internet varsa 2 dönüyoruz.
                    else if (hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        result = 2
                    }

                }
            }

            // Run metodu bittiğinde result'ı dönüyoruz.
            return result
        }

        fun isConnected() : Boolean {

            // Contexti aldık.
            var context = GlobalApp.getAppContext()

            // Bağlantı tipini alıyoruz.
            var result = getConnectionType(context)

            // Burada da result 0 dan farklı ise internet var demektir true döner, 0 ise false döner yani internet yok demektir.
            return result != 0
        }
    }

}