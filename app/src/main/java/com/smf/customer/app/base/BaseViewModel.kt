package com.smf.customer.app.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.smf.customer.R
import com.smf.customer.app.constant.AppConstant
import com.smf.customer.app.constant.AppConstant.INTERNAL_SERVER_ERROR_CODE
import com.smf.customer.app.constant.AppConstant.NOT_FOUND
import com.smf.customer.app.constant.AppConstant.SERVICE_UNAVAILABLE
import com.smf.customer.app.constant.AppConstant.SERVICE_UNAVAILABLE_2
import com.smf.customer.app.constant.AppConstant.TIMEOUT_EXCEPTION
import com.smf.customer.app.constant.AppConstant.TOO_MANY_REQUEST_CODE
import com.smf.customer.app.constant.AppConstant.UNAUTHORISED
import com.smf.customer.data.model.request.RequestDTO
import com.smf.customer.data.model.response.ResponseDTO
import com.smf.customer.di.retrofit.RetrofitHelper
import com.smf.customer.di.sharedpreference.SharedPrefsHelper
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject

abstract class BaseViewModel : ViewModel() {
    var showLoading = MutableLiveData<Boolean>()
    var retryErrorMessage = MutableLiveData<Int>()
    var logout = MutableLiveData<Boolean>()
    var userToken = MutableLiveData<String>()
    val TAG: String = this.javaClass.simpleName
    private val disposables = CompositeDisposable()
    var observable = MutableLiveData<Observable<ResponseDTO>>()
    var isLastPage: Boolean = false
    var isLoading: Boolean = false
    var skip = MutableLiveData<Int>()
    var count = 100

    @Inject
    lateinit var retrofitHelper: RetrofitHelper

    @Inject
    lateinit var preferenceHelper: SharedPrefsHelper

    private var retryDialog = MutableLiveData<Boolean>(false)

    fun showRetryDialogFlag() {
        retryDialog.value = true
    }

    fun hideRetryDialogFlag() {
        retryDialog.value = false
    }

    fun isRetryDialogVisible(): Boolean {
        return retryDialog.value!!
    }

    val toastMessage = MutableLiveData<String>()


    init {
        showLoading.value = false
    }


    /*open fun getUserToken(userId: String) {

        if (userId.isBlank()) {
            userToken.postValue("")
        } else {
            val data = hashMapOf(
                "userId" to userId,
                "push" to true
            )
            showLoading.postValue(true)
            functions = FirebaseFunctions.getInstance()
            functions
                .getHttpsCallable("getToken")
                .call(data)
                .continueWith { task ->
                    // This continuation runs on either success or failure, but if the task
                    // has failed then result will throw an Exception which will be
                    // propagated down.
                    if (task.isSuccessful) {
                        Log.d(TAG, "Task is Successful")
                        val result = task.result.data as String
                        showLoading.postValue(false)
                        userToken.postValue(result)
                    } else {
                        Log.d(TAG, "Task is Failed")
                        userToken.postValue(null)
                    }
                }
        }
    }
*/
    fun prepareRequest(requestDTO: RequestDTO): RequestDTO {
        //requestDTO.accessToken = preferenceHelper[SharedPrefConstant.ACCESS_TOKEN, ""]
        //requestDTO.appKey = BuildConfig.MY_APP_KEY
        return requestDTO
    }

    fun showToastMessage(messageCode: Int) {
        if (messageCode > 100000) {
            showToastMessage(MyApplication.appContext.getString(messageCode))
        }
    }

    fun showToastMessage(message: String?) {
        with(toastMessage) { setValue(message!!) }
    }

    open fun onError(throwable: Throwable) {
        try {
            when {
                throwable is IOException -> {
                    retryErrorMessage.value = (R.string.time_out_error)
                }
                throwable is SocketTimeoutException -> {
                    retryErrorMessage.value = (R.string.time_out_error)
                }
                (throwable as HttpException).code() == NOT_FOUND -> {
                    retryErrorMessage.value = (R.string.server_not_found)
                }
                (throwable).code() == UNAUTHORISED -> {
                    retryErrorMessage.value = R.string.invalid_credential
                }
                (throwable).code() == AppConstant.FORBIDDEN -> {
                    retryErrorMessage.value = R.string.unAuthorised_request
                }
                (throwable).code() == TOO_MANY_REQUEST_CODE -> {
                    retryErrorMessage.value = R.string.too_many_request
                }
                throwable.code() == SERVICE_UNAVAILABLE || (throwable).code() == SERVICE_UNAVAILABLE_2 -> {
                    retryErrorMessage.value = (R.string.service_not_available)
                }
                throwable.code() == TIMEOUT_EXCEPTION -> {
                    retryErrorMessage.value = (R.string.invalid_credential)
                    //getUserToken(preferenceHelper[SharedPrefConstant.USER_ID, ""])
                    logout.value = true
                }

                else -> {
                    try {
                        val response = throwable.response()!!.errorBody()!!.string()
                        val jObjError = JSONObject(response)
                        if (jObjError[AppConstant.ERROR_MESSAGE] == AppConstant.INVALID_USER) {
                            showToastMessage(AppConstant.INVALID_USER)
                        } else if (jObjError[AppConstant.MESSAGE_CODE] == INTERNAL_SERVER_ERROR_CODE
                        ) {
                            retryErrorMessage.value = (R.string.internal_server_error)
                        } else {
                            retryErrorMessage.value = (R.string.something_went_wrong)
                        }
                    } catch (e: Exception) {
                        retryErrorMessage.value = (R.string.something_went_wrong)
                    }
                }
            }
        } catch (e: Exception) {
            when (e) {
                is SocketTimeoutException -> {
                    retryErrorMessage.value = (R.string.time_out_error)
                }
                else -> {
                    retryErrorMessage.value = (R.string.something_went_wrong)
                }
            }
        } finally {
            showLoading.postValue(false)
        }
    }

    open fun onSuccess(responseDTO: ResponseDTO) {
        showLoading.value = false
    }

    open fun onSuccess(responseBody: ResponseBody) {
        showLoading.value = false
    }

    override fun onCleared() {
        disposables.clear()
        showLoading.value = false
    }

    open fun doNetworkOperation() {
        showLoading.value = true
        disposables.add(
            observable.value!!.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(this::onSuccess, this::onError)
        )
    }
}