package com.netzdev.ecommerce.UI.Products.ProductDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.netzdev.ecommerce.ApiServices.ProductService
import com.netzdev.ecommerce.Models.ApiError
import com.netzdev.ecommerce.Models.Product
import com.netzdev.ecommerce.Utilities.IViewModelState
import com.netzdev.ecommerce.Utilities.LoadingState
import kotlinx.coroutines.launch

class ProductDetailViewModel : ViewModel(), IViewModelState {
    override var loadingState: MutableLiveData<LoadingState> = MutableLiveData<LoadingState>()
    override var errorState: MutableLiveData<ApiError> = MutableLiveData<ApiError>()


    fun getProduct(productId: Int) : LiveData<Product> {
        var productReturn = MutableLiveData<Product>()

        loadingState.value = LoadingState.Loading

        viewModelScope.launch {
            var response = ProductService.getProductById(productId)

            if (response.isSuccessful) {
                productReturn.value = response.success
            } else {
                errorState.value = response.failure
            }

            loadingState.value = LoadingState.Loaded
        }

        return productReturn
    }

    fun deleteProduct(productId: Int) : LiveData<Boolean> {
        var productReturn = MutableLiveData<Boolean>()

        loadingState.value = LoadingState.Loading

        viewModelScope.launch {
            var response = ProductService.deleteProduct(productId)

            if (response.isSuccessful) {
                productReturn.value = true
            } else {
                errorState.value = response.failure
            }

            loadingState.value = LoadingState.Loaded
        }

        return productReturn
    }

}