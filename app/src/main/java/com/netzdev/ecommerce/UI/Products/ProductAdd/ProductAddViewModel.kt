package com.netzdev.ecommerce.UI.Products.ProductAdd

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.netzdev.ecommerce.ApiServices.CategoryService
import com.netzdev.ecommerce.ApiServices.PhotoService
import com.netzdev.ecommerce.ApiServices.ProductService
import com.netzdev.ecommerce.Models.ApiError
import com.netzdev.ecommerce.Models.Category
import com.netzdev.ecommerce.Models.Product
import com.netzdev.ecommerce.Utilities.IViewModelState
import com.netzdev.ecommerce.Utilities.LoadingState
import kotlinx.coroutines.launch

class ProductAddViewModel : ViewModel(), IViewModelState {

    override var loadingState: MutableLiveData<LoadingState> = MutableLiveData<LoadingState>()
    override var errorState: MutableLiveData<ApiError> = MutableLiveData<ApiError>()

    // Burada kategorileri çekiyoruz.
    fun getCategories() : LiveData<ArrayList<Category>> {
        loadingState.value = LoadingState.Loading

        var categoriesReturn = MutableLiveData<ArrayList<Category>>()

        viewModelScope.launch {
            var response = CategoryService.categoryList()

            if (response.isSuccessful) {
                categoriesReturn.value = response.success
            } else {
                errorState.value = response.failure
            }

            loadingState.value = LoadingState.Loaded
        }

        return categoriesReturn
    }

    // Burada ürün ekleme işlemi yapıyoruz.
    fun addProduct(product: Product, fileUri: Uri?) : LiveData<Product> {
        loadingState.value = LoadingState.Loading

        var productReturn = MutableLiveData<Product>()

        viewModelScope.launch {

            // Önce fotoğraf ile ürün yüklendiyse fotoğrafı yüklüyoruz.
            if (fileUri != null) {
                var photoResponse = PhotoService.uploadPhoto(fileUri)

                // Success olursa ürünün photoPath'ine atıyoruz.
                if (photoResponse.isSuccessful) {
                    product.PhotoPath = photoResponse.success!!.Url
                } else {
                    errorState.value = photoResponse.failure
                }
            }

            // Sonra foto ile ürünü ekliyoruz. ProductReturn MutableLiveData olduğu için fragment tarafında observe olan metoda response success olduğunda value diye atama yaptığımız için direk fragment'taki metod çalışacak.
            var productResponse = ProductService.addProduct(product)
            if (productResponse.isSuccessful) {
                productReturn.value = productResponse.success
            } else {
                errorState.value = productResponse.failure
            }

            loadingState.value = LoadingState.Loaded

        }

        return productReturn

    }

}