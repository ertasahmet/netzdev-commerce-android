package com.netzdev.ecommerce.RetrofitServices

import com.netzdev.ecommerce.Models.Photo
import com.netzdev.ecommerce.Models.PhotoDelete
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface RetrofitPhotoService {

    // Fotoğrafları api'ye bu şekilde Multipart olarak gönderiyoruz.
    @Multipart
    @POST("api/photos")
    suspend fun upload(@Part photo: MultipartBody.Part) : Response<Photo>

    // Burada Http ile belirlenen api'ye bir delete isteği olduğunu söyledik. Yukarıdaki POST gibi DELETE yazabilirdik fakat apiden foto silme işlemlerinde burada delete yapınca problem oluyormuş. O yüzden bu şekilde daha sağlıklı.
    @HTTP(method = "DELETE", path = "api/photos", hasBody = true)
    suspend fun delete(@Body photoDelete: PhotoDelete) : Response<Unit>

}