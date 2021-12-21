package com.netzdev.ecommerce.UI.Products.ProductList

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.netzdev.ecommerce.ApiServices.ProductService
import com.netzdev.ecommerce.Models.ApiError
import com.netzdev.ecommerce.Models.Product
import com.netzdev.ecommerce.Utilities.IViewModelState
import com.netzdev.ecommerce.Utilities.LoadingState
import kotlinx.coroutines.launch

class ProductListViewModel : ViewModel(), IViewModelState {

    override var loadingState: MutableLiveData<LoadingState> = MutableLiveData<LoadingState>()
    override var errorState: MutableLiveData<ApiError> = MutableLiveData<ApiError>()
    var products = MutableLiveData<ArrayList<Product>>()

    fun getProducts(page: Int) {

        if (page == 0) {
            loadingState.value = LoadingState.Loading
        }

        viewModelScope.launch {
            var response = ProductService.productList(page)

            if (response.isSuccessful) {
                products.value = response.success!!
            } else {
                errorState.value = response.failure
            }

            if (page == 0) {
                loadingState.value = LoadingState.Loaded
            }
        }
    }


}