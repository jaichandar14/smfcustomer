package com.smf.customer.app.base

import androidx.lifecycle.MutableLiveData
import com.smf.customer.R
import com.smf.customer.app.constant.AppConstant
import com.smf.customer.data.model.request.RequestDTO
import com.smf.customer.data.model.response.ResponseDTO
import com.smf.customer.di.retrofit.RetrofitHelper
import com.smf.customer.di.sharedpreference.SharedPrefsHelper
import com.smf.customer.utility.CustomErrorAPI
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

abstract class BaseFragmentViewModel:BaseViewModel() {

}