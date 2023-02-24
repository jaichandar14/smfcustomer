package com.smf.customer.view.dashboard.fragment

import androidx.lifecycle.MutableLiveData
import com.smf.customer.app.base.BaseFragmentViewModel
import com.smf.customer.app.base.MyApplication
import com.smf.customer.data.model.response.ResponseDTO
import com.smf.customer.di.sharedpreference.SharedPrefsHelper
import com.smf.customer.view.dashboard.responsedto.EventCountDto
import com.smf.customer.view.dashboard.responsedto.EventStatusData
import com.smf.customer.view.dashboard.responsedto.EventStatusResponse
import com.smf.customer.view.dashboard.responsedto.MyEventData
import io.reactivex.Observable
import javax.inject.Inject

class MainDashBoardViewModel : BaseFragmentViewModel() {
    var listMyEvents = MutableLiveData<MyEventData>()
    var eventStatusData = MutableLiveData<EventStatusData>()
    var noEventVisible = MutableLiveData<Boolean>()
    var screenRotationValue = MutableLiveData<Boolean>(false)

    @Inject
    lateinit var sharedPrefsHelper: SharedPrefsHelper

    init {
        MyApplication.applicationComponent?.inject(this)
    }

    // 3285
    fun getEventCount(username: String) {
        val observable: Observable<EventCountDto> =
            retrofitHelper.getDashBoardRepository().getEventCount(getUserToken(), username)
        this.observable.value = observable as Observable<ResponseDTO>
        doNetworkOperation()
    }


    fun getEventStatus(username: String, status: ArrayList<String>) {
        val observable: Observable<EventStatusResponse> =
            retrofitHelper.getDashBoardRepository().getEventStatus(getUserToken(), username, status)
        this.observable.value = observable as Observable<ResponseDTO>
        doNetworkOperation()
    }

    override fun onSuccess(responseDTO: ResponseDTO) {
        super.onSuccess(responseDTO)
        when (responseDTO) {
            is EventCountDto -> {
                var response = responseDTO.data
                callBackInterface?.getMyevetList(response)
            }
            is EventStatusResponse -> {
                var response = responseDTO.data
                callBackInterface?.getEventStatus(response)
            }
        }
    }


    private var callBackInterface: OnServiceClickListener? = null

    // Initializing Listener Interface
    fun setOnClickListener(listener: OnServiceClickListener) {
        callBackInterface = listener
    }

    // Interface For Invoice Click Listener
    interface OnServiceClickListener {
        fun getMyevetList(listMyEvents: MyEventData)
        fun getEventStatus(response: EventStatusData)
    }
}