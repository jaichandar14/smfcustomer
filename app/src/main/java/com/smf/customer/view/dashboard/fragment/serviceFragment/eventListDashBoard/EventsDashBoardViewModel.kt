package com.smf.customer.view.dashboard.fragment.serviceFragment.eventListDashBoard

import androidx.lifecycle.MutableLiveData
import com.smf.customer.app.base.BaseDashboardViewModel
import com.smf.customer.app.base.MyApplication
import com.smf.customer.data.model.response.*
import io.reactivex.Observable

class EventsDashBoardViewModel : BaseDashboardViewModel()  {

    var pending=MutableLiveData<Boolean>()
    var mTitleTxt=MutableLiveData<String>()
    init {
        MyApplication.applicationComponent?.inject(this)
    }
    fun onPending(){
        mTitleTxt.value="Jai"
        pending.value=true
    }
    fun onCompleted(){
        pending.value=false
    }

    fun getEventInfo(eventId: Int) {
        val observable: Observable<GetEventInfo> =
            retrofitHelper.getEventRepository().getEventInfo(getUserToken(), eventId)
        this.observable.value = observable as Observable<ResponseDTO>
        doNetworkOperation()
    }
    override fun onSuccess(responseDTO: ResponseDTO) {
        super.onSuccess(responseDTO)
        when (responseDTO) {
            is GetEventInfo -> {
                var response = responseDTO.data
                callBackInterface?.getEventInfo(response)
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
        fun getEventInfo(listMyEvents: GetDataDto)
    }

}