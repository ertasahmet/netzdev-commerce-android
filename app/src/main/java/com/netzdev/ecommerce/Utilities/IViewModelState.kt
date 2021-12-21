package com.netzdev.ecommerce.Utilities

import androidx.lifecycle.MutableLiveData
import com.netzdev.ecommerce.Models.ApiError

//  IViewModelState diye bir interface oluşturduk. Burada genel olarak tüm viewModel'lerin kullanacağı verileri tutuyoruz.
interface IViewModelState {

    // MutableLiveData özellliği ile LiveData sayesinde değişkenlerdeki değişiklikleri takip ediyoruz. Değerleri değiştiğinde aktivitelere ve fragment'lara bilgi verilecek ve onlar da ekranları güncelleyebilecekler.
    var loadingState: MutableLiveData<LoadingState>
    var errorState: MutableLiveData<ApiError>
}