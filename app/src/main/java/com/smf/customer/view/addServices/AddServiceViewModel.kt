package com.smf.customer.view.addServices

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.smf.customer.app.base.BaseViewModel
import com.smf.customer.app.base.MyApplication
import com.smf.customer.data.model.response.GetServicesDTO
import com.smf.customer.data.model.response.ResponseDTO
import com.smf.customer.data.model.response.ServiceData
import com.smf.customer.di.sharedpreference.SharedPrefsHelper
import io.reactivex.Observable
import javax.inject.Inject

class AddServiceViewModel : BaseViewModel() {

    var servicesList = MutableLiveData<ArrayList<ServiceData>>()
    var selectedServicePositionMap = HashMap<Int, Boolean>()
    var preSelectedServices = ArrayList<String>()

    @Inject
    lateinit var sharedPrefsHelper: SharedPrefsHelper

    init {
        MyApplication.applicationComponent?.inject(this)
    }

    fun getAddServices(eventId: Int) {
        val observable: Observable<GetServicesDTO> =
            retrofitHelper.getEventRepository().getAddServices(getUserToken(), eventId)
        this.observable.value = observable as Observable<ResponseDTO>
        doNetworkOperation()
    }

    override fun onSuccess(responseDTO: ResponseDTO) {
        super.onSuccess(responseDTO)
        when (responseDTO) {
            is GetServicesDTO -> {
                Log.d(TAG, "onSuccess: AddServices called")
                servicesList.value = responseDTO.data as ArrayList<ServiceData>
            }
        }
    }

}