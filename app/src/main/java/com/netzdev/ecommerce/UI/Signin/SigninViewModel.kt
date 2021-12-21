package com.netzdev.ecommerce.UI.Signin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.netzdev.ecommerce.ApiServices.LoginService
import com.netzdev.ecommerce.Models.ApiError
import com.netzdev.ecommerce.Models.UserSignIn
import com.netzdev.ecommerce.Utilities.IViewModelState
import com.netzdev.ecommerce.Utilities.LoadingState
import kotlinx.coroutines.launch

class SigninViewModel : ViewModel(), IViewModelState {
    override var loadingState: MutableLiveData<LoadingState> = MutableLiveData<LoadingState>()
    override var errorState: MutableLiveData<ApiError> = MutableLiveData<ApiError>()

    fun signIn(userSignIn: UserSignIn) : LiveData<Boolean> {
        loadingState.value = LoadingState.Loading
        var status = MutableLiveData<Boolean>()

        viewModelScope.launch {
            var apiResponse = LoginService.signIn(userSignIn)

            if (!apiResponse.isSuccessful) {
                status.value = false
                errorState.value = apiResponse.failure
            } else {
                status.value = true
            }

            loadingState.value = LoadingState.Loaded
        }

        return status
    }
}