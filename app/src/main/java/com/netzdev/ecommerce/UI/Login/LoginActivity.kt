package com.netzdev.ecommerce.UI.Login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.netzdev.ecommerce.R
import com.netzdev.ecommerce.UI.SignUp.SignUpFragment
import com.netzdev.ecommerce.UI.Signin.SigninFragment
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        var pagerAdapter = ScreenSlideAdapter(this)
        pagerAdapter.addFragment(SigninFragment())
        pagerAdapter.addFragment(SignUpFragment())

        // Kotlin'de findViewById bu şekilde kullanılıyor. Fakat ilgili extensionları gradle dosyasına yükleyerek findViewById olayından kurtulabiliriz. Aşağıdakiler yerine kodumuz şu şekilde olucak.
       // var viewPager = findViewById<ViewPager2>(R.id.ViewPagerLogin)
       // viewPager.adapter = pagerAdapter

        ViewPagerLogin.adapter = pagerAdapter
    }

    private inner class ScreenSlideAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        var fragments = ArrayList<Fragment>()

        override fun getItemCount(): Int {
            return fragments.size
        }

        override fun createFragment(position: Int): Fragment {
            return fragments[position]
        }

        fun addFragment(f: Fragment){
            fragments.add(f)
        }

    }
}