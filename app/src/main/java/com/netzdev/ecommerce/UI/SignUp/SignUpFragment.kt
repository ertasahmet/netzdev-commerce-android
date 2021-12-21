package com.netzdev.ecommerce.UI.SignUp

import android.app.AlertDialog
import android.content.DialogInterface
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.netzdev.ecommerce.Models.UserSignUp
import com.netzdev.ecommerce.R
import com.netzdev.ecommerce.Utilities.HelperService
import com.netzdev.ecommerce.Utilities.LoadingState
import kotlinx.android.synthetic.main.sign_up_fragment.*
import kotlinx.android.synthetic.main.sign_up_fragment.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SignUpFragment : Fragment() {

    companion object {
        fun newInstance() = SignUpFragment()
    }

    private lateinit var viewModel: SignUpViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        // Önce viewModel'e ulaştık.
        viewModel = ViewModelProvider(this).get(SignUpViewModel::class.java)
        var fragmentView = inflater.inflate(R.layout.sign_up_fragment, container, false)

        // Burada aktivite'mizden bir nesneye ulaştık. requireActivity metoduyla aktivite'yi çağırıyoruz.
        var viewPagerLogin = requireActivity().findViewById<ViewPager2>(R.id.ViewPagerLogin)

        // Burada viewModel'deki loading olayını gözlemliyoruz ve değiştiğinde butona animasyon vericez.
        viewModel.loadingState.observe(viewLifecycleOwner, {
            when(it){
                LoadingState.Loading -> fragmentView.btnSignup.startAnimation()
                LoadingState.Loaded -> fragmentView.btnSignup.revertAnimation()
            }
        })

        viewModel.errorState.observe(viewLifecycleOwner, {
            HelperService.showErrorMessageByToast(it)
        })


        // Burada da butona eriştik ve onClick olayın böyle direk parantez açarak girdik. Kotlin bu imkanı sunuyor.
        fragmentView.btnSignup.setOnClickListener {

            // Textbox'lardaki bilgileri alıp bir UserSignUp nesnesi oluşturduk.
            var userSignUp = UserSignUp (
                TxtSignupUsername.editText?.text.toString(),
                TxtSignupEmail.editText?.text.toString(),
                TxtSignupPassword.editText?.text.toString(),
                TxtSignupCity.editText?.text.toString(),
             )

            // ViewModel'deki signUp metodunu çağırdık ve metoda observe olduk ki oradan veri geldiğinde bize haber gelsin ve ona göre işlem yapalım.
            viewModel.signUp(userSignUp).observe(viewLifecycleOwner, {

                // Oradan gelen veriyi kontrol ediyoruz eğer true ise işlem gerçekleşmiştir demektir ve ona göre işlem yaparız. Eğer false ise hata vardır ona göre hata yönetimi yaparız.
                if (it) {
                    viewPagerLogin.currentItem = 0

                    // Buradaki scope'larda Main olan dispatchers, kodları ui thread'te çalıştırır fakat ui thread'i bloklamaz ve ui'daki elemanlara erişebilirsiniz. Eğer IO'yu seçerseniz ui thread'den farklı bir thread'de çalıştırır, ui elemanlarına erişemezseniz. Arka tarafta apilere istek atacaksınız kullanabilirsiniz. Default Dispatcher ise genelde yoğun algoritmalarda, cpu'yu yoracak metodlar var ise default kullanabiliriz.
                    CoroutineScope(Dispatchers.Main).launch {

                        // Burada 1 saniyelik gecikme verdikten sonra ekrana yazdırıyoruz.
                        delay(1000)
                        onAlertDialog(fragmentView)
                    }

                } else {
                    // Hata var
                }
            })
        }
        return fragmentView
    }

    private fun onAlertDialog(view: View){
        var builder = AlertDialog.Builder(view.context)
        builder.setMessage("Bilgileriniz başarıyla kaydedilmiştir. Email ve şifreniz ile giriş yapabilirsiniz.")

        // Bu şekilde küme parantezlerinde işlem yapmak istemiyorsak ilgili parametre yerlerini boş yazarız ve sade bir lambda yazarız ve birşey yapmayız
        builder.setPositiveButton("Tamam"){_,_ ->}
        builder.show()

        // Bu kısım yukarıdaki yazımın uzun hali. Boşuna kod yazmış ve karıştırmış oluyoruz.
     /*   builder.setPositiveButton("Tamam", object:DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
                TODO("Not yet implemented")
            }
        })*/

        // Yukarıdakinin kısa hali de aşağıdakidir.
        builder.setPositiveButton("Tamam") { dialog, which -> TODO("Not yet implemented") }
    }

}