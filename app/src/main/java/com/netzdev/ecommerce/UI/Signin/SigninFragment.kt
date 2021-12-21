package com.netzdev.ecommerce.UI.Signin

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.netzdev.ecommerce.Models.UserSignIn
import com.netzdev.ecommerce.R
import com.netzdev.ecommerce.UI.User.UserActivity
import com.netzdev.ecommerce.Utilities.HelperService
import com.netzdev.ecommerce.Utilities.LoadingState
import kotlinx.android.synthetic.main.sign_up_fragment.view.*
import kotlinx.android.synthetic.main.signin_fragment.view.*

class SigninFragment : Fragment() {

    companion object {
        fun newInstance() = SigninFragment()
    }

    private lateinit var viewModel: SigninViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(SigninViewModel::class.java)
        var root = inflater.inflate(R.layout.signin_fragment, container, false)

        root.btnSignIn.setOnClickListener {
            val userSignIn = UserSignIn(
                root.TxtSignInEmail.editText?.text.toString(),
                root.TxtSignInPassword.editText?.text.toString()
            )
            viewModel.signIn(userSignIn).observe(viewLifecycleOwner, {
                if (it) {
                    var intent = Intent(requireActivity(), UserActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                }
            })
        }

        viewModel.loadingState.observe(viewLifecycleOwner, {
            when(it){
                LoadingState.Loading -> root.btnSignIn.startAnimation()
                LoadingState.Loaded -> root.btnSignIn.revertAnimation()
            }
        })

        viewModel.errorState.observe(viewLifecycleOwner, {
            HelperService.showErrorMessageByToast(it)
        })

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // TODO: Use the ViewModel
    }

}