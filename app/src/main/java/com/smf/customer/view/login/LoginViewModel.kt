package com.smf.customer.view.login

import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.amplifyframework.core.Amplify
import com.smf.customer.R
import com.smf.customer.app.base.BaseViewModel
import com.smf.customer.app.base.MyApplication
import com.smf.customer.app.constant.AppConstant
import com.smf.customer.data.model.response.GetUserDetails
import com.smf.customer.data.model.response.ResponseDTO
import com.smf.customer.di.sharedpreference.SharedPrefConstant
import com.smf.customer.di.sharedpreference.SharedPrefsHelper
import com.smf.customer.view.emailotp.EmailOTPActivity
import io.reactivex.Observable
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginViewModel : BaseViewModel() {

    private var userInfo: String = ""
    private var userName: String? = null
    private var firstName: String? = null
    private var emailId: String? = null
    var showPhnNumberError = MutableLiveData<Boolean>()
    var showEmailError = MutableLiveData<Boolean>()

    init {
        MyApplication.applicationComponent.inject(this)
        showPhnNumberError.value = false
        showEmailError.value = false
    }

    fun emailAndNumberValidation(
        phoneNumber: String,
        eMail: String,
        encodedMobileNo: String
    ): Boolean {
        showPhnNumberError.value = false
        showEmailError.value = false
        if (phoneNumber.isNotEmpty() || eMail.isNotEmpty()) {
            if (phoneNumber.isEmpty() || eMail.isEmpty()) {
                return if (phoneNumber.isEmpty()) {
                    userInfo = AppConstant.EMAIL
                    getUserDetails(eMail)
                    true
                } else {
                    userInfo = AppConstant.MOBILE
                    getUserDetails(encodedMobileNo)
                    true
                }
            } else {
                showToastMessage(MyApplication.appContext.getString(R.string.Please_Enter_Any_EMail_or_Phone_Number))
                return false
            }
        } else {
            showToastMessage(MyApplication.appContext.getString(R.string.Please_Enter_Email_or_MobileNumber))
        }
        return false
    }

    private fun getUserDetails(loginName: String) {
        val observable: Observable<GetUserDetails> =
            retrofitHelper.getUserRepository().getUserDetails(loginName)
        this.observable.value = observable as Observable<ResponseDTO>
        doNetworkOperation()
    }

    @Inject
    lateinit var sharedPrefsHelper:SharedPrefsHelper
    override fun onSuccess(responseDTO: ResponseDTO) {
        super.onSuccess(responseDTO)
        Log.d(TAG, "onSuccess: ${(responseDTO as GetUserDetails).data.userName}")
        userName = responseDTO.data.userName
        firstName = responseDTO.data.firstName
        emailId = responseDTO.data.email
        if (AppConstant.EVENT_ORGANIZER == responseDTO.data.role) {
            sharedPrefsHelper.put(SharedPrefConstant.USER_ID,responseDTO.data.userName)
            showLoading.value=true
            signIn(responseDTO.data.userName)
        } else {
            if (userInfo == AppConstant.EMAIL) {
                showEmailError.value = true
            } else {
                showPhnNumberError.value = true
            }
        }
    }

    // SignIn Method
    private fun signIn(userName: String) {
        Amplify.Auth.signIn(userName, null, { result ->
            Log.d(TAG, "signIn: completed")
            if (result.isSignInComplete) {
                Log.i(TAG, "Sign in succeeded $result")
                viewModelScope.launch {
                    callBackInterface?.callBack("signInCompletedGoToDashBoard")
                }
            } else {
                Log.i(TAG, "Sign in not complete $result")
                viewModelScope.launch {
                    callBackInterface?.callBack("SignInNotCompleted")
                }
            }

        }, {
            Log.e(TAG, "Failed to sign in ${it.cause!!.message!!.split(".")[0]}")
        })
    }
    private var callBackInterface: CallBackInterface? = null

    // Initializing CallBack Interface Method
    fun setCallBackInterface(callback: CallBackInterface) {
        callBackInterface = callback
    }

    // CallBackInterface
    interface CallBackInterface {
        fun callBack(status: String)
    }

}