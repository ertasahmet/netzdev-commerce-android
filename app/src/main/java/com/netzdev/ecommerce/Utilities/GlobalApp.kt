package com.netzdev.ecommerce.Utilities

import android.app.Application
import android.content.Context

// GlobalApp diye bir sınıf tanımlıyoruz ve Application sınıfından miras alıyor ve yandaki parantez ile de Application'ın cosntructor'ını çağırıyor.
class GlobalApp : Application()
{

    companion object {

        // Lateinit keywordu ile bu nesneyi şuan initialize etmiyorum, daha sonra initialize edicem diyoruz. Onu da aşağıdaki onCreate    metodunda yapıyoruz.
        // Ayrıca manifest dosyasında da application kısmının en üstüne  android:name=".Utilities.GlobalApp" bunu yapıştırıyoruz.
        private lateinit var mContext: Context

        // GetAppContext metodu ile context'i çağırıyoruz. Amaç uygulamanın heryerinden bu context'e ulaşabilmek.
        public fun getAppContext() : Context
        {
            return mContext
        }
    }

    // OnCreate metodunu override ediyoruz ve context'i dolduruyoruz.
    override fun onCreate() {
        super.onCreate()
        mContext = this
    }
}