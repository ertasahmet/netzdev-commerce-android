package com.netzdev.ecommerce.ApiServices

import com.netzdev.ecommerce.Consts.ApiConsts
import com.netzdev.ecommerce.Models.ApiResponse
import com.netzdev.ecommerce.Models.ODataModel
import com.netzdev.ecommerce.Models.Product
import com.netzdev.ecommerce.RetrofitServices.ApiClient
import com.netzdev.ecommerce.RetrofitServices.RetrofitCategoryService
import com.netzdev.ecommerce.RetrofitServices.RetrofitProductService
import com.netzdev.ecommerce.Utilities.HelperService

class ProductService {

    companion object {

        private var retrofitProductServiceWithInterceptor =
            ApiClient.buildService(ApiConsts.apiBaseUrl, RetrofitProductService::class.java, true)

        suspend fun productList(page : Int) : ApiResponse<ArrayList<Product>> {
            try {
                var response = retrofitProductServiceWithInterceptor.products(page)

                if (!response.isSuccessful) return HelperService.handleApiError(response)

                var oDataProduct = response.body() as ODataModel<Product>
                return ApiResponse(true, oDataProduct.Value)
            } catch (ex: Exception) {
                return HelperService.handleException(ex)
            }
        }

        suspend fun getProductById(productId : Int) : ApiResponse<Product> {
            try {
                var response = retrofitProductServiceWithInterceptor.getProduct(productId)

                if (!response.isSuccessful) return HelperService.handleApiError(response)

                var oDataProduct = response.body() as ODataModel<Product>
                return ApiResponse(true, oDataProduct.Value[0])
            } catch (ex: Exception) {
                return HelperService.handleException(ex)
            }
        }

        suspend fun addProduct(product: Product) : ApiResponse<Product> {
            try {
                var response = retrofitProductServiceWithInterceptor.addProduct(product)

                if (!response.isSuccessful) return HelperService.handleApiError(response)

                var oDataProduct = response.body() as ODataModel<Product>
                return ApiResponse(true, oDataProduct.Value[0])
            } catch (ex: Exception) {
                return HelperService.handleException(ex)
            }
        }

        suspend fun updateProduct(product: Product) : ApiResponse<Unit> {
            try {
                var response = retrofitProductServiceWithInterceptor.updateProduct(product, product.Id)

                if (!response.isSuccessful) return HelperService.handleApiError(response)
                return ApiResponse(true)
            } catch (ex: Exception) {
                return HelperService.handleException(ex)
            }
        }

        suspend fun deleteProduct(productId: Int) : ApiResponse<Unit> {
            try {
                var response = retrofitProductServiceWithInterceptor.deleteProduct(productId)

                if (!response.isSuccessful) return HelperService.handleApiError(response)
                return ApiResponse(true)
            } catch (ex: Exception) {
                return HelperService.handleException(ex)
            }
        }

    }

}