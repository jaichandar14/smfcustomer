package com.smf.customer.view.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.amplifyframework.auth.options.AuthSignOutOptions
import com.amplifyframework.core.Amplify
import com.smf.customer.R
import com.smf.customer.app.base.BaseActivity
import com.smf.customer.app.base.MyApplication
import com.smf.customer.app.constant.AppConstant
import com.smf.customer.databinding.ActivityLoginBinding
import com.smf.customer.dialog.DialogConstant
import com.smf.customer.view.emailotp.EmailOTPActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.URLEncoder

class LoginActivity : BaseActivity<LoginViewModel>(), LoginViewModel.CallBackInterface {

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
        // Initialize CallBackInterface
        viewModel.setCallBackInterface(this)
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

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun callBack(status: String) {
        when (status) {
            AppConstant.SIGN_IN_NOT_COMPLETED -> {
                val intent = Intent(this, EmailOTPActivity::class.java)
                startActivity(intent)
                viewModel.showLoading.value = false
            }
        }
    }

    override fun onPositiveClick(dialogFragment: DialogFragment) {
        super.onPositiveClick(dialogFragment)
        when {
            dialogFragment.tag.equals(DialogConstant.INTERNET_DIALOG) -> {
                viewModel.hideRetryDialogFlag()
                signOut()
            }
        }
    }

}