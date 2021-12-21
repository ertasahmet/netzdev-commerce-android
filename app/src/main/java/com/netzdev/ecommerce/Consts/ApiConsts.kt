package com.netzdev.ecommerce.Consts

// Burayı class değil de direk object olarak işaretliyoruz. Bunun amacı da static bir class içinde static değerlere erişmek için daha kolay
// bir yol oluyor. Direk buradan static veya singleton olaylarıyla uğraşmadan halledebiliyoruz.
object ApiConsts {

    // Buradaki 10.0.2.2 olayı da android tarafında localhost diye bişey olmadığı için android bize böyle bir url vermiş ve bu url bizi
    // localhost'a yönlendiriyor. Bu sayede localdeki projemiz çalışacaktır.
    const val authBaseUrl = "http://10.0.2.2:5001/"
    const val apiBaseUrl = "http://10.0.2.2:5000/"
    const val photoBaseUrl = "http://10.0.2.2:5002/"

    // Burada da grant type'larımızı belirttik.
    const val clientCredentialGrantType = "client_credentials"
    const val resourceOwnerPasswordCredentialGrantType = "password"
    const val refreshTokenCredentialGrantType = "refresh_token"

}