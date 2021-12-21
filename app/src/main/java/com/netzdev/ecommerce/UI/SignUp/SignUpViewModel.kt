package com.netzdev.ecommerce.UI.SignUp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.netzdev.ecommerce.ApiServices.LoginService
import com.netzdev.ecommerce.Models.ApiError
import com.netzdev.ecommerce.Models.UserSignUp
import com.netzdev.ecommerce.Utilities.IViewModelState
import com.netzdev.ecommerce.Utilities.LoadingState
import kotlinx.coroutines.launch

class SignUpViewModel : ViewModel(), IViewModelState {

    // Burada yine ilgili interface'i ve değişkenlerini çağırdık.
    override var loadingState: MutableLiveData<LoadingState> = MutableLiveData<LoadingState>()
    override var errorState: MutableLiveData<ApiError> = MutableLiveData<ApiError>()

    // SignUp için bir metod yazdık. MutableLiveData boolean bir değişken oluşturduk ona göre geri döneceğiz. Loading durumunu ele alıyoruz.
    fun signUp(userSignUp: UserSignUp) : LiveData<Boolean> {
        var status = MutableLiveData<Boolean>()
        loadingState.value = LoadingState.Loading

        // ViewModelScope'u açtık ki apilere istek atalım.
        viewModelScope.launch {

            // Retrofit'ten apiyi çağırdık.
            var response = LoginService.signUp(userSignUp)

            // Gelen cevabı status'un value'suna atadık.
            status.value = response.isSuccessful
            loadingState.value = LoadingState.Loaded

            // Response success değilse errorState'i de doldurduk.
            if (!response.isSuccessful) errorState.value = response.failure
        }

        // En son status'u döndük.
        return status
    }

}