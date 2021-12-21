package com.netzdev.ecommerce.ApiServices

import android.net.Uri
import com.netzdev.ecommerce.Consts.ApiConsts
import com.netzdev.ecommerce.Models.ApiResponse
import com.netzdev.ecommerce.Models.Photo
import com.netzdev.ecommerce.Models.PhotoDelete
import com.netzdev.ecommerce.RetrofitServices.ApiClient
import com.netzdev.ecommerce.RetrofitServices.RetrofitPhotoService
import com.netzdev.ecommerce.RetrofitServices.RetrofitProductService
import com.netzdev.ecommerce.Utilities.GlobalApp
import com.netzdev.ecommerce.Utilities.HelperService
import com.netzdev.ecommerce.Utilities.RealPathUtil
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class PhotoService {

    companion object {
        private var retrofitPhotoServiceWithInterceptor =
            ApiClient.buildService(ApiConsts.photoBaseUrl, RetrofitPhotoService::class.java, true)

        // Upload için bir metod oluşturduk ve parametre olarak seçilen fotonun Uri'sini alıyor. /* */
        suspend fun uploadPhoto (fileUri: Uri) : ApiResponse<Photo> {

            try {

                // İnternetten bir class bulduk ve Uri'sini verdiğimiz fotonun gerçek adresini buluyor. Amaç da oradaki adrese gidip fotoğrafı ordan almak. Buradan gerçek adresi aldık.
                var path = RealPathUtil.getRealPath(GlobalApp.getAppContext(), fileUri)

                // Belirtilen yolda bir dosya var dedik, dosyaya çevirdik.
                var file = File(path)

                // Dosya tipini image dedik.
                var requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())

                // MultipartBody ile photo keyword'ünde tipi image olan dosyayı oluşturuyoruz.
                var body = MultipartBody.Part.createFormData("photo", file.name, requestFile)

                // Sonra api'nin body'sinde gönderiyoruz.
                var response = retrofitPhotoServiceWithInterceptor.upload(body)

                if (!response.isSuccessful) return HelperService.handleApiError(response)

                return ApiResponse(true, response.body())
            } catch (ex: Exception) {
                return HelperService.handleException(ex)
            }
        }

        suspend fun deletePhoto (photoDelete: PhotoDelete) : ApiResponse<Unit> {

            try {
                var response = retrofitPhotoServiceWithInterceptor.delete(photoDelete)

                if (!response.isSuccessful) return HelperService.handleApiError(response)

                return ApiResponse(true)
            } catch (ex: Exception) {
                return HelperService.handleException(ex)
            }

        }

    }

}