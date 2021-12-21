package com.netzdev.ecommerce.UI.Launch

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import androidx.lifecycle.ViewModelProvider
import com.netzdev.ecommerce.R
import com.netzdev.ecommerce.UI.Login.LoginActivity
import com.netzdev.ecommerce.UI.User.UserActivity
import com.netzdev.ecommerce.Utilities.LoadingState
import kotlinx.android.synthetic.main.activity_launch.*

class LaunchActivity : AppCompatActivity() {

    // İlgili activity'nin viewModel'ini tipinden nesne oluşturduk.
    lateinit var viewModel : LaunchActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch)

        // Burada da ViewModelProvider sayesinde ilgili viewModel tipinden sadece tek bir instance üretiyor ve memory'yi yormadan sadece onu kullanıyoruz ve ilgili değişikliklerden haberimiz oluyor. Owner kısmında bu viewModel'i kullanacak activity olarak bu activity'i this diye gösteriyoruz ve get ile ilgili ViewModel class'ının tipini veriyoruz.
        viewModel = ViewModelProvider(this).get(LaunchActivityViewModel::class.java)

        // Burada animasyon yaptık. X ve y koordinatlarına göre %20 büyütüyoruz.
        var scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1.2f)
        var scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1.2f)

        // İlgili bilgilere göre aniamtor nesnesi oluşturduk.
        var animator = ObjectAnimator.ofPropertyValuesHolder(imgCompanyLogo, scaleX, scaleY)

        // Animasyonun tekrarlanma olayını infinite yani sonsuz yaptık, süresini de 1 saniye yaptık, her işlemi 1 saniyede yapıcak.
        animator.repeatMode = ObjectAnimator.REVERSE
        animator.repeatCount = Animation.INFINITE
        animator.duration = 1000

        viewModel.loadingState.observe(this, {
            when(it) {
                LoadingState.Loading -> animator.start()
                LoadingState.Loaded -> animator.cancel()
            }
        })

        // Burada checkToken metodunu çalıştırıyoruz ve orayı observe ile gözlemliyoruz. Bu sayede metottan gelen değişikliklerden haberdar oluyoruz ve gelen verilere ulaşabiliyoruz. İlk parametrede bu viewModel'in sahibini yani bu class'ı veriyoruz.      İkinci parametre de observe olayında hangi kodlar çalışacak diye parantez açıyoruz ve içine giriyoruz. (Aslında burada observer class'ındaki bir metodu override edip onun içine kod yazıyoruz fakat kotlin syntax2i kolaylaştırıyor.)
        viewModel.checkToken().observe(this, {

            // Burada da bir intent değişkeni oluşturuyoruz ve when ile metottan gelen boolean değişkeni kontrol ediyoruz. True ise lambda ile giriyoruz ve UserActivity'i aç diyoruz çünkü token geçerlidir. Eğer false ise token geçerli değildir ve LoginActivity'i açıyoruz.
            var intent = when (it) {
                true -> {
                    Intent(this@LaunchActivity, UserActivity::class.java)
                }
                false -> {
                    Intent(this@LaunchActivity, LoginActivity::class.java)
                }
            }

            // En son da burada activity'yi başlatıyoruz.
            startActivity(intent)
        })

    }
}