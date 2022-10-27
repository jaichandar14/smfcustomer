package com.smf.customer.view.emailotp

import android.annotation.SuppressLint
import android.content.Context
import android.os.CountDownTimer
import android.util.Log
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BaseObservable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.amplifyframework.auth.AuthUserAttributeKey
import com.amplifyframework.auth.cognito.AWSCognitoAuthSession
import com.amplifyframework.auth.result.AuthSessionResult
import com.amplifyframework.core.Amplify
import com.smf.customer.R
import com.smf.customer.app.base.BaseViewModel
import com.smf.customer.app.base.MyApplication
import com.smf.customer.app.constant.AppConstant
import com.smf.customer.data.model.response.GetLoginInfoDTO
import com.smf.customer.data.model.response.OTPValidationResponseDTO
import com.smf.customer.data.model.response.ResponseDTO
import com.smf.customer.databinding.EmailOtpActivityBinding
import com.smf.customer.di.sharedpreference.SharedPrefConstant
import com.smf.customer.di.sharedpreference.SharedPrefsHelper
import io.reactivex.Observable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class EmailOTPViewModel : BaseViewModel() {
    val userOtp1 = MutableLiveData<String>()
    val userOtp2 = MutableLiveData<String>()
    val userOtp3 = MutableLiveData<String>()
    val userOtp4 = MutableLiveData<String>()
    var num = 0
    var resendRestriction = 0
    private var isValid: Boolean = true
    val resendColor = MutableLiveData<Boolean>()
    init {
        MyApplication.applicationComponent.inject(this)
    }

    @Inject
    lateinit var sharedPrefsHelper: SharedPrefsHelper

    // 3245 - Android-OTP expires Validation Method
    fun otpTimerValidation(
        mDataBinding: EmailOtpActivityBinding?, userName: String
    ) {
        var counter = 30
        val countTime: TextView = mDataBinding!!.otpTimer
        resendColor.value=true
        mDataBinding.otpResend.isClickable = false
        object : CountDownTimer(30000, 1000) {
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                if (counter < 10) {
                    countTime.text = " 00:0${counter}"
                } else {
                    countTime.text = " 00:${counter}"
                }
                counter--
            }

            override fun onFinish() {
                resendRestriction += 1
                mDataBinding.otpResend.isClickable = true
                countTime.text = AppConstant.INITIAL_TIME

                if (resendRestriction <= 6) {
                    resendColor.value=false
                    mDataBinding.otpResend.setOnClickListener {
                        showLoading.value=true
                        if (resendRestriction <= 5) {
                            reSendOTP(userName)
                        } else {
                            callBackInterface?.showToast(resendRestriction)
                        }

                    }
                } else {
                    callBackInterface?.showToast(resendRestriction)
                }
            }
        }.start()
    }

    // 3245 - Android-OTP expires Validation
    // OTP Resend SignIn Method
    fun reSendOTP(userName: String) {
        Amplify.Auth.signIn(userName, null, {
            Log.d(TAG, "reSendOTP: called code resented successfully")
            viewModelScope.launch {
                callBackInterface?.showToast(resendRestriction)
                callBackInterface?.callBack(AppConstant.RESEND_OTP)
            }
        },
            {
                Log.e(TAG, "Failed to sign in", it)
                viewModelScope.launch {
                    val errMsg = it.cause!!.message!!.split(".")[0]
                    //   toastMessage = errMsg
                    if (errMsg.contains(MyApplication.appContext.resources.getString(R.string.Failed_to_connect_to_cognito_idp)) ||
                        errMsg.contains(MyApplication.appContext.resources.getString(R.string.Unable_to_resolve_host))
                    ) {
                        showLoading.value = false
                        callBackInterface!!.awsErrorResponse(MyApplication.appContext.resources.getString(R.string.Failed_to_connect_to_cognito_idp))
                    } else {
                        showToastMessage(errMsg)
                        callBackInterface!!.awsErrorResponse(num.toString())
                    }
                }
            })
    }

    private var callBackInterface: CallBackInterface? = null

    // Initializing CallBack Interface Method
    fun setCallBackInterface(callback: CallBackInterface) {
        callBackInterface = callback
    }

    // CallBack Interface
    interface CallBackInterface {
        suspend fun callBack(status: String)
        fun awsErrorResponse(num: String)
        fun showToast(resendRestriction: Int)
        fun otpValidation(b: Boolean)
    }

    // 3245 - Login User Validation Method
    fun loginUser(isValid: Boolean, userID: String) {
        this.isValid = isValid
        val observable: Observable<OTPValidationResponseDTO> =
            retrofitHelper.getUserRepository().setOTPValidation(isValid, userID)
        this.observable.value = observable as Observable<ResponseDTO>
        doNetworkOperation()
    }

    // 3245 - Login User get Details  Method
    private fun getLoginInfo(idToken: String) {
        val observable: Observable<GetLoginInfoDTO> =
            retrofitHelper.getUserRepository().getLoginInfo(idToken)
        this.observable.value = observable as Observable<ResponseDTO>
        doNetworkOperation()
    }

    override fun onSuccess(responseDTO: ResponseDTO) {
        super.onSuccess(responseDTO)
        when (responseDTO) {
            is OTPValidationResponseDTO -> {
                if (isValid) {
                    getLoginInfo(sharedPrefsHelper[SharedPrefConstant.ACCESS_TOKEN, ""])
                }

            }
            is GetLoginInfoDTO -> {
                setSpRegIdAndRollID(responseDTO)
                viewModelScope.launch {
                    callBackInterface?.callBack("Move_to_Dashboard")
                }
            }
        }
        Log.d(TAG, "onSuccess: $responseDTO")
    }

    // Method For Set SpRegId And RollID to SharedPreference From Login Api
    private fun setSpRegIdAndRollID(apiResponse: GetLoginInfoDTO) {
        sharedPrefsHelper.put(
            SharedPrefConstant.SP_REG_ID,
            apiResponse.data.spRegId
        )
        sharedPrefsHelper.put(
            SharedPrefConstant.ROLE_ID,
            apiResponse.data.roleId
        )
        sharedPrefsHelper.put(
            SharedPrefConstant.USER_ID,
            apiResponse.data.userName
        )
    }

    override fun onError(throwable: Throwable) {
        super.onError(throwable)
        if (negativeCode) {
            viewModelScope.launch {
                callBackInterface?.callBack("MOVE_TO_SIGNING")
            }
        }
    }

    inner class AwsAmplify {
        // Custom Confirm SignIn Function
        fun confirmSignIn(context: Context, otp: String) {
            num += 1
            Amplify.Auth.confirmSignIn(otp,
                {
                    Log.d(TAG, "confirmSignIn scess: $it")
                    if (it.isSignInComplete) {
                        // Aws method for Fetching Id Token
                        Log.d(TAG, "confirmSignIn scess: $it")
                        fetchIdToken(context)
                    } else {
                        Log.d(TAG, "confirmSignIn scess else: $it")
                        viewModelScope.launch {
                            // toastMessage = AppConstant.INVALID_OTP
                            toast = AppConstant.INVALID_OTP_TEXT
                            // showToastMessage( AppConstant.INVALID_OTP_TEXT)
                            callBackInterface?.awsErrorResponse(num.toString())
                        }
                    }
                },
                {
                    Log.d(TAG, "Failed to confirm signIn ${it.localizedMessage}", it)
                    val errMsg = it.cause!!.message!!.split(".")[0]
                    viewModelScope.launch {
                        if (it.cause?.message?.contains(context.resources.getString(R.string.OTP_expired)) == true || it.cause?.message?.contains(
                                context.resources.getString(R.string.Invalid_session_for_the_user)
                            ) == true
                        ) {
                            toast = context.resources.getString(R.string.OTP_is_expired)
                            callBackInterface!!.awsErrorResponse(num.toString())
                        } else if (errMsg.contains(context.resources.getString(R.string.Failed_to_connect_to_cognito_idp)) ||
                            errMsg.contains(context.resources.getString(R.string.Unable_to_resolve_host))
                        ) {
                            showLoading.value=false

                            callBackInterface!!.awsErrorResponse(context.resources.getString(R.string.Failed_to_connect_to_cognito_idp))
                        } else {
                            toast = AppConstant.INVALID_OTP.toString()
                            //   showToastMessage(AppConstant.INVALID_OTP)
                            callBackInterface!!.awsErrorResponse(num.toString())
//                        callBackInterface?.otpValidation(false)
                        }
                    }
                })
        }

        // Email Verification
        private fun eMailVerification() {
            Amplify.Auth.resendUserAttributeConfirmationCode(
                AuthUserAttributeKey.email(),
                {
                    viewModelScope.launch {
                        callBackInterface?.callBack(AppConstant.EMAIL_VERIFICATION_CODE_PAGE)
                    }
                },
                {
                    Log.e(TAG, "Failed to resend code", it)
                    viewModelScope.launch {
                        val errMsg = it.cause!!.message!!.split(".")[0]
                        showToastMessage(errMsg)
                        callBackInterface!!.awsErrorResponse(num.toString())
                    }
                })
        }

        // Aws Method for 6 digit Validation Check
        private fun emailCodeValidationCheck(context: Context) {
            Amplify.Auth.fetchUserAttributes(
                { result ->
                    if (result[1].value.equals(AppConstant.FALSE)) {
                        eMailVerification()
                    } else {
                        Log.i(TAG, "User attributes = successfully entered dashboard")
                        viewModelScope.launch {
                            callBackInterface?.callBack(AppConstant.EMAIL_VERIFIED_TRUE_GOTO_DASHBOARD)
                        }
                    }
                },
                {
                    Log.e(TAG, "Failed to fetch user attributes", it)
                    val errMsg = it.cause!!.message!!.split(".")[0]
                    Log.e(TAG, "Failed to fetch user attributes $errMsg")
                    viewModelScope.launch {
                        if (errMsg.contains(context.resources.getString(R.string.Unable_to_resolve_host)) ||
                            errMsg.contains(context.resources.getString(R.string.Failed_to_connect_to_cognito_idp))
                        ) {
                          callBackInterface!!.awsErrorResponse(context.resources.getString(R.string.Failed_to_connect_to_cognito_idp))
                        } else if (errMsg.contains(context.resources.getString(R.string.Operation_requires_a_signed_in_state))) {
                            callBackInterface!!.awsErrorResponse(context.resources.getString(R.string.Operation_requires_a_signed_in_state))
                        }
                    }
                })
        }

        // Aws method for Fetching Id Token
        private fun fetchIdToken(context: Context) {
            Amplify.Auth.fetchAuthSession(
                {
                    val session = it as AWSCognitoAuthSession
                    idToken =
                        AuthSessionResult.success(session.userPoolTokens.value?.idToken).value.toString()
                    sharedPrefsHelper.put(
                        SharedPrefConstant.ACCESS_TOKEN,
                        "${AppConstant.BEARER} $idToken"
                    )
                    Log.d(TAG, "confirmSignIn scess idToken: $it, $idToken")
                    viewModelScope.launch(Dispatchers.Main) {
                        //Aws Method for 6 digit Validation Check
                        emailCodeValidationCheck(context)
                    }
                },
                {
                    Log.e(TAG, "Failed to fetch session", it)
                    val errMsg = it.cause!!.message!!.split(".")[0]
                    viewModelScope.launch {
                        if (errMsg.contains(context.resources.getString(R.string.Failed_to_connect_to_cognito_idp)) ||
                            errMsg.contains(context.resources.getString(R.string.Unable_to_resolve_host))
                        ) {
                            callBackInterface!!.awsErrorResponse(context.resources.getString(R.string.Failed_to_connect_to_cognito_idp))
                        }
                    }
                }
            )
        }
    }

}