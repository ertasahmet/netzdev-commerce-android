package com.netzdev.ecommerce.RetrofitServices

import com.netzdev.ecommerce.Models.ODataModel
import com.netzdev.ecommerce.Models.Product
import retrofit2.Response
import retrofit2.http.*

interface RetrofitProductService {

    // Bu içinde skip olan query sorgusu aşağıdaki sorguya ek olarak &$skip=10 gibi bir değer ekleyecek.
    @GET("odata/products?\$expand=category(\$select=Name)&\$select=id,name,stock,price,photoPath&\$orderby=id desc")
    suspend fun products(@Query("\$skip") page: Int) : Response<ODataModel<Product>>

    @GET("odata/products({productId})?\$expand=category")
    suspend fun getProduct(@Path("productId") productId: Int) : Response<ODataModel<Product>>

    @POST("odata/products")
    suspend fun addProduct(@Body product: Product) : Response<ODataModel<Product>>

    @PUT("odata/products({productId})")
    suspend fun updateProduct(
        @Body product: Product,
        @Path("productId") productId: Int
    ) : Response<Unit>

    @DELETE("odata/products({productId})")
    suspend fun deleteProduct(@Path("productId") productId: Int) : Response<Unit>
}