package com.smf.customer.view.login

import android.util.Log
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

    @Inject
    lateinit var sharedPrefsHelper: SharedPrefsHelper

    init {
        MyApplication.applicationComponent?.inject(this)
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
        // Verify both number and email empty
        if (phoneNumber.isEmpty() && eMail.isEmpty()) {
            if (MyApplication.getAppContextInitialization()) {
                showSnackMessage(
                    MyApplication.appContext.getString(R.string.please_Enter_Any_EMail_or_Phone_Number)
                )
            }
            return false
        }
        // Verify number
        if (phoneNumber.isNotEmpty()) {
            userInfo = AppConstant.MOBILE
            if (MyApplication.getAppContextInitialization()) {
                getUserDetails(encodedMobileNo)
            }
            return true
        }
        // Verify email
        if (eMail.isNotEmpty()) {
            userInfo = AppConstant.EMAIL
            if (MyApplication.getAppContextInitialization()) {
                getUserDetails(eMail)
            }
            return true
        }
        return false
    }

    fun getUserDetails(loginName: String) {
        val observable: Observable<GetUserDetails> =
            retrofitHelper.getUserRepository().getUserDetails(loginName)
        this.observable.value = observable as Observable<ResponseDTO>
        doNetworkOperation()
    }

    override fun onSuccess(responseDTO: ResponseDTO) {
        super.onSuccess(responseDTO)
        val getUserDetails = responseDTO as GetUserDetails
        userName = getUserDetails.data.userName
        firstName = getUserDetails.data.firstName
        emailId = getUserDetails.data.email
        if (AppConstant.EVENT_ORGANIZER == responseDTO.data.role) {
            sharedPrefsHelper.put(SharedPrefConstant.USER_ID, getUserDetails.data.userName)
            sharedPrefsHelper.put(SharedPrefConstant.FIRST_NAME, getUserDetails.data.firstName)
            sharedPrefsHelper.put(SharedPrefConstant.LAST_NAME, getUserDetails.data.lastName)
            sharedPrefsHelper.put(SharedPrefConstant.EMAIL_ID, getUserDetails.data.email)
            sharedPrefsHelper.put(
                SharedPrefConstant.MOBILE_NUMBER_WITH_COUNTRY_CODE,
                getUserDetails.data.mobileNumber
            )
            showLoading.value = true
            signIn(getUserDetails.data.userName)
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
            if (result.isSignInComplete) {
                viewModelScope.launch {
                    callBackInterface?.callBack(AppConstant.SIGN_IN_COMPLETED_GOTO_DASH_BOARD)
                }
            } else {
                viewModelScope.launch {
                    callBackInterface?.callBack(AppConstant.SIGN_IN_NOT_COMPLETED)
                }
            }

        }, {
            Log.d(TAG, "Failed to sign in ${it.cause!!.message!!.split(".")[0]}")
            viewModelScope.launch {
                val errMsg = it.cause!!.message!!.split(".")[0]
                if (errMsg == MyApplication.appContext.resources.getString(R.string.CreateAuthChallenge_failed_with_error)) {
                    showSnackMessage(
                        errMsg
                    )
                } else if (errMsg.contains(MyApplication.appContext.resources.getString(R.string.Failed_to_connect_to_cognito_idp)) ||
                    errMsg.contains(MyApplication.appContext.resources.getString(R.string.Unable_to_resolve_host))

                ) {
                    retryErrorMessage.value = R.string.internet_error
                } else {
                    showSnackMessage(
                        errMsg
                    )
                }
            }
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