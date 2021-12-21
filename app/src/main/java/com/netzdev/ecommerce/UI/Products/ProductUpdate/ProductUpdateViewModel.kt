package com.netzdev.ecommerce.UI.Products.ProductUpdate

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
import com.netzdev.ecommerce.Models.PhotoDelete
import com.netzdev.ecommerce.Models.Product
import com.netzdev.ecommerce.Utilities.IViewModelState
import com.netzdev.ecommerce.Utilities.LoadingState
import kotlinx.coroutines.launch

class ProductUpdateViewModel : ViewModel(), IViewModelState {
    override var loadingState: MutableLiveData<LoadingState> = MutableLiveData<LoadingState>()
    override var errorState: MutableLiveData<ApiError> = MutableLiveData<ApiError>()

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

    fun updateProduct(product: Product, fileUri: Uri?) : LiveData<Boolean> {
        loadingState.value = LoadingState.Loading

        var statusReturn = MutableLiveData<Boolean>()

        viewModelScope.launch {
            if (fileUri != null) {

                if (!product.PhotoPath.isNullOrEmpty()){
                    PhotoService.deletePhoto(PhotoDelete(product.PhotoPath))
                }

                var photoResponse = PhotoService.uploadPhoto(fileUri)

                if (photoResponse.isSuccessful) {
                    product.PhotoPath = photoResponse.success!!.Url
                } else {
                    errorState.value = photoResponse.failure
                }
            }

            var response = ProductService.updateProduct(product)

            if (response.isSuccessful) {
                statusReturn.value = true
            } else {
                errorState.value = response.failure
            }

            loadingState.value = LoadingState.Loaded
        }

        return statusReturn
    }

}