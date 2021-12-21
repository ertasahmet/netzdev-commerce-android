package com.netzdev.ecommerce.UI.User

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.netzdev.ecommerce.R
import com.netzdev.ecommerce.Utilities.HelperService
import com.netzdev.ecommerce.Utilities.IViewModelState
import com.netzdev.ecommerce.Utilities.LoadingState
import kotlinx.android.synthetic.main.activity_user.*

class UserActivity : AppCompatActivity() {

    companion object {
        lateinit var loadingView: View

        // Burada da tek bir yerden loading olayını ayarladık ve State'imizde loadingState değiştiğinde burası çalışıp ona göre loading ekranını göstericek.
        fun setLoadingStatus (viewModel: IViewModelState, viewLifecycleOwner: LifecycleOwner) {
            viewModel.loadingState.observe(viewLifecycleOwner, {

                when (it) {
                    LoadingState.Loading -> loadingView.visibility = View.VISIBLE
                    LoadingState.Loaded -> loadingView.visibility = View.GONE
                }
            })
        }

        // Burada tek bir merkezden error durumlarını kontrol eden bir metod yazdım. Bir yerde errorState değiştiğinde burası direk çalışıp hataları ekrana yazdıracak.
        fun setErrorStatus (viewModel: IViewModelState, viewLifecycleOwner: LifecycleOwner) {
            viewModel.errorState.observe(viewLifecycleOwner, {
                HelperService.showErrorMessageByToast(it)
            })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        loadingView = full_page_loading_view
    }
}