package com.netzdev.ecommerce.ApiServices

import com.netzdev.ecommerce.Consts.ApiConsts
import com.netzdev.ecommerce.Models.ApiResponse
import com.netzdev.ecommerce.Models.Category
import com.netzdev.ecommerce.Models.ODataModel
import com.netzdev.ecommerce.RetrofitServices.ApiClient
import com.netzdev.ecommerce.RetrofitServices.RetrofitCategoryService
import com.netzdev.ecommerce.RetrofitServices.RetrofitLoginService
import com.netzdev.ecommerce.Utilities.HelperService

class CategoryService {

    companion object {

        // Her istekte araya interceptor ile girip token koyacağımız için interceptor var dedik.
        private var retrofitCategoryServiceWithInterceptor =
            ApiClient.buildService(ApiConsts.apiBaseUrl, RetrofitCategoryService::class.java, true)

        suspend fun categoryList() : ApiResponse<ArrayList<Category>> {
            try {
                var response = retrofitCategoryServiceWithInterceptor.categories()

                if (!response.isSuccessful) return HelperService.handleApiError(response)

                var oDaataCategory = response.body() as ODataModel<Category>
                return ApiResponse(true, oDaataCategory.Value)
            } catch (ex: Exception) {
                return HelperService.handleException(ex)
            }
        }

    }

}