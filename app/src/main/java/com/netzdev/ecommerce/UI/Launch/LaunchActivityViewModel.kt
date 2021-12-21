package com.netzdev.ecommerce.UI.Launch

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.netzdev.ecommerce.ApiServices.TokenService
import com.netzdev.ecommerce.Models.ApiError
import com.netzdev.ecommerce.Utilities.HelperService
import com.netzdev.ecommerce.Utilities.IViewModelState
import com.netzdev.ecommerce.Utilities.LoadingState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LaunchActivityViewModel : ViewModel(), IViewModelState {

    override var loadingState: MutableLiveData<LoadingState> = MutableLiveData<LoadingState>()
    override var errorState: MutableLiveData<ApiError> = MutableLiveData<ApiError>()

    var isSuccessful = MutableLiveData<Boolean>()

    private fun refreshTokenCheck() {

        // Burada refresh token'ı kontrol ederken retrofit tarafında refresh token metodunu senkron olarak yazdık. Burada da refresh ttoken ile ilgili arayüzle işimiz olmadığı için, arka tarafta apilere istek attığımız için CoroutineScope'lardan IO olanı kullandık. Bu yüzden de LiveData nesneslerine normalde .value diyerek değer atıyorduk fakat şuan IO thread'inde yani farklı bir thread'te olduğumuz için .postValue diyerek değer atadık.
        CoroutineScope(Dispatchers.IO).launch {
            var token = HelperService.getTokenSharedPreference()

            if (token != null) {
                var response = TokenService.refreshToken(token.RefreshToken)

                Log.i("OkHttp", "Refresh token istek yapıldı. Sonuç: ${response.isSuccessful}")

                if(response.isSuccessful){
                    HelperService.saveTokenSharedPreference(response.success!!)
                }

                isSuccessful.postValue(response.isSuccessful)

            } else {
                isSuccessful.postValue(false)
            }

            loadingState.postValue(LoadingState.Loaded)
        }
    }

    // Token'ı kontrol etmek için bir metod tanımlıyoruz ve LiveData ile Boolean dönüş tipinde dönecek. LiveData ile anlık olarak değişikliği takip ediyoruz. Buradaki LiveData sadece okunmak için yazılıyor. Eğer MutableLiveData olsaydı değerler değiştirilebilirdi activity tarafından.
    fun checkToken() : LiveData<Boolean>
    {
        // Loading State'i değiştirdik, api'ye istek attığımız için state artık loading oluyor.
        loadingState.value = LoadingState.Loading

        // ViewModelScope ui thread'de çalışan fakat ui thread'i engellemeyen bir scope fonksiyon türüdür ve Coroutine ile ilgilidir. Amacımız suspend ile işaretlenen asenkron metodlarımızı arayüzü bloklamadan çağırmak ve işlem yapmaktır. Bu scope'ta bu işlemleri yapabiliyoruz. Aynı zamanda bu scope içerisindeki istekler sadece bu activity veya fragment ayakta iken çalışacak, kullanıcı sayfadan ayrıldığı anda bu istekler bitecek ve bu da memory açısından çok önemlidir.
        viewModelScope.launch {

            // Burada istek attık ve cevabı aldık.
            var response = TokenService.checkToken()

            // Burada da response'u kontrol ediyoruz, successful ise true dönüyoruz, değilse refresh token'ı deniyoruz.
            if (response.isSuccessful){
                isSuccessful.value = response.isSuccessful
                loadingState.value = LoadingState.Loaded
            } else {
                errorState.value = response.failure
                refreshTokenCheck()
            }
        }

        // En son da status'u dönüyoruz.
        return isSuccessful
    }

}