package com.netzdev.ecommerce.RetrofitServices

import com.netzdev.ecommerce.Models.Category
import com.netzdev.ecommerce.Models.ODataModel
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface RetrofitCategoryService {

    @GET("odata/categories")
    suspend fun categories() : Response<ODataModel<Category>>
}