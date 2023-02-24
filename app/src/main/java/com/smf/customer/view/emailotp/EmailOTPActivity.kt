package com.smf.customer.view.emailotp

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.smf.customer.R
import com.smf.customer.app.base.BaseActivity
import com.smf.customer.app.base.MyApplication
import com.smf.customer.app.constant.AppConstant
import com.smf.customer.databinding.EmailOtpActivityBinding
import com.smf.customer.di.sharedpreference.SharedPrefConstant
import com.smf.customer.di.sharedpreference.SharedPrefsHelper
import com.smf.customer.dialog.DialogConstant
import com.smf.customer.view.dashboard.DashBoardActivity
import com.smf.customer.view.login.LoginActivity
import javax.inject.Inject

class EmailOTPActivity : BaseActivity<EmailOTPViewModel>(), EmailOTPViewModel.CallBackInterface {
    private lateinit var mDataBinding: EmailOtpActivityBinding
    private var userName: String = ""
    private lateinit var otp0: EditText
    private lateinit var otp1: EditText
    private lateinit var otp2: EditText
    private lateinit var otp3: EditText

    @Inject
    lateinit var customPinView: CustomPinView

    @Inject
    lateinit var sharedPrefsHelper: SharedPrefsHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.email_otp_activity)
        mInitialize()
        mViewModelMethodCall()

    }

    // 3245 - Initialize method
    private fun mInitialize() {
        mDataBinding = DataBindingUtil.setContentView(this, R.layout.email_otp_activity)
        viewModel = ViewModelProvider(this)[EmailOTPViewModel::class.java]
        mDataBinding.otpviewmodel = viewModel
        mDataBinding.otpactivity = this
        mDataBinding.lifecycleOwner = this@EmailOTPActivity
        MyApplication.applicationComponent?.inject(this)
        //3372
        viewModel.bindingRoot.value = mDataBinding
        // Initialize CallBackInterface
        viewModel.setCallBackInterface(this)
        otp0 = mDataBinding.otp1ed
        otp1 = mDataBinding.otp2ed
        otp2 = mDataBinding.otp3ed
        otp3 = mDataBinding.otp4ed
    }

    // 3245 -  Method to call the the methods in ViewModel
    private fun mViewModelMethodCall() {
        userName = getUserID()
        customPinView.initializePin(mDataBinding)
        viewModel.otpTimerValidation(mDataBinding, userName)
    }

    // 3245 - When submit button is clicked this method will be called
    fun submitBtnClicked() {
        viewModel.showLoading.value = true
        otpValidation(
            otp0.text.toString(), otp1.text.toString(),
            otp2.text.toString(), otp3.text.toString()
        )

    }

    // 3245 - OTP Validation Method
    private fun otpValidation(
        otp0: String,
        otp1: String,
        otp2: String,
        otp3: String
    ): Boolean {
        return if (otp0.isEmpty()) {
            viewModel.showSnackMessage(
                AppConstant.ENTER_OTP
            )
            viewModel.showLoading.value = false
            false
        } else {
            viewModel.AwsAmplify().confirmSignIn(
                this, otp0 + otp1 + otp2 + otp3
            )
            true
        }
    }

    override suspend fun callBack(status: String) {
        when (status) {
            AppConstant.EMAIL_VERIFIED_TRUE_GOTO_DASHBOARD -> {
                viewModel.loginUser(true, getUserID())
            }
            AppConstant.RESEND_OTP -> {
                viewModel.otpTimerValidation(
                    mDataBinding,
                    userName
                )
            }
            getString(R.string.move_to_Dashboard) -> {
                viewModel.showLoading.value = true
                val intent = Intent(this, DashBoardActivity::class.java)
                startActivity(intent)

            }
            getString(R.string.move_to_sigin) -> {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        }

    }

    override fun awsErrorResponse(num: String) {
        if (num == resources.getString(R.string.Failed_to_connect_to_cognito_idp)) {
            viewModel.retryErrorMessage.value = R.string.internet_error
        } else {
            viewModel.loginUser(false, getUserID())
            if (num.toInt() >= 3) {
            } else {
                viewModel.showSnackMessage(
                    viewModel.toast
                )
            }
            mDataBinding.otp1ed.text = null
            mDataBinding.otp3ed.text = null
            mDataBinding.otp2ed.text = null
            mDataBinding.otp4ed.text = null
            mDataBinding.otp1ed.requestFocus()
        }
    }

    override fun showToast(resendRestriction: Int) {
        if (resendRestriction <= 5) {
            viewModel.showSnackMessage(
                getString(R.string.otp_sent_to_your_mail)
            )
            viewModel.showLoading.value = false
        } else {
            viewModel.showSnackMessage(
                getString(R.string.resend_clicked_multiple_time)
            )
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    override fun otpValidation(b: Boolean) {
        viewModel.showSnackMessage(
            AppConstant.ENTER_OTP
        )
    }

    private fun getUserID(): String {
        return sharedPrefsHelper[SharedPrefConstant.USER_ID, ""]
    }

    override fun onPositiveClick(dialogFragment: DialogFragment) {
        super.onPositiveClick(dialogFragment)
        when {
            dialogFragment.tag.equals(DialogConstant.INTERNET_DIALOG) -> {
                viewModel.hideRetryDialogFlag()
                viewModel.showLoading.value = true
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    override fun onStop() {
        super.onStop()
        viewModel.toastMessageG.value?.msg = ""
    }
}