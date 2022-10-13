package com.smf.customer.view.login

import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.amplifyframework.auth.options.AuthSignOutOptions
import com.amplifyframework.core.Amplify
import com.smf.customer.R
import com.smf.customer.app.base.BaseActivity
import com.smf.customer.app.base.MyApplication
import com.smf.customer.app.constant.AppConstant
import com.smf.customer.databinding.ActivityLoginBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.URLEncoder

class LoginActivity : BaseActivity<LoginViewModel>() {

    lateinit var binding: ActivityLoginBinding
    private lateinit var mobileNumberWithCountryCode: String
    private lateinit var encodedMobileNo: String
    private lateinit var eMail: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@LoginActivity, R.layout.activity_login)
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        binding.loginViewModel = viewModel
        binding.lifecycleOwner = this@LoginActivity
        MyApplication.applicationComponent.inject(this)
        // SignIn Button Listener
        signInClicked()
    }

    // Method for SignIn Button
    private fun signInClicked() {
        binding.signinbtn.setOnClickListener {
            signOut()
        }
    }

    // 3028 on Close Sign out
    private fun signOut() {
        Amplify.Auth.signOut(
            AuthSignOutOptions.builder().globalSignOut(true).build(),
            {
                CoroutineScope(Dispatchers.Main).launch {
                    afterSignOut()
                }
            },
            {
                CoroutineScope(Dispatchers.Main).launch {
                    afterSignOut()
                }
                Log.e(TAG, "Sign out failed", it)
            }
        )
    }

    private fun afterSignOut() {
        val phoneNumber = binding.editTextMobileNumber.text.toString().trim()
        val countryCode = binding.cppSignIn.selectedCountryCode
        mobileNumberWithCountryCode = "+".plus(countryCode).plus(phoneNumber)
        // Single Encoding
        encodedMobileNo = URLEncoder.encode(mobileNumberWithCountryCode, AppConstant.ENCODE)
        eMail = binding.editTextEmail.text.toString().trim()
        // Validate Mail and phone number
        viewModel.emailAndNumberValidation(phoneNumber, eMail, encodedMobileNo)
    }

}