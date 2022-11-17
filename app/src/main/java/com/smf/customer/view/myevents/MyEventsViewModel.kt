package com.smf.customer.view.myevents

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.smf.customer.app.base.BaseViewModel
import com.smf.customer.app.base.MyApplication
import com.smf.customer.data.model.response.ResponseDTO
import com.smf.customer.view.dashboard.model.EventStatusDTO
import com.smf.customer.view.myevents.model.MyEventsDto
import io.reactivex.Observable

// 3262
class MyEventsViewModel : BaseViewModel() {

    var eventClickedPos: MutableLiveData<Int?>? = MutableLiveData<Int?>()
    var onClicked = MutableLiveData<Boolean>()
    var eventTypeList = MutableLiveData<ArrayList<EventStatusDTO>>()
    var lastOrientation = MutableLiveData<Int>()
    var clickedEventDetails = MutableLiveData<EventStatusDTO>()

    init {
        MyApplication.applicationComponent.inject(this)
    }

    val listMyEvents = ArrayList<EventStatusDTO>()


    // 3245 - Login User get Details  Method
    fun getEventType(idToken: String) {
        val observable: Observable<MyEventsDto> =
            retrofitHelper.getEventRepository().getMyEvents(idToken)
        this.observable.value = observable as Observable<ResponseDTO>
        doNetworkOperation()
    }

    override fun onSuccess(responseDTO: ResponseDTO) {
        super.onSuccess(responseDTO)
        var eventList = (responseDTO as MyEventsDto).data
        for (i in 0 until eventList.size) {
            Log.d(TAG, "onSuccess: ${eventList[i].eventName}")
            listMyEvents.add(
                EventStatusDTO(
                    eventList[i].eventTemplateId.toString(),
                    eventList[i].eventName
                )
            )
        }

        callBackInterface?.getMyevetList(listMyEvents)
    }

    override fun onError(throwable: Throwable) {
        super.onError(throwable)
        Log.d(TAG, "onError: ${throwable.message}")
        showLoading.value = false
    }

    private var callBackInterface: OnServiceClickListener? = null

    // Initializing Listener Interface
    fun setOnClickListener(listener: OnServiceClickListener) {
        callBackInterface = listener
    }

    // Interface For Invoice Click Listener
    interface OnServiceClickListener {
        fun getMyevetList(listMyEvents: ArrayList<EventStatusDTO>)
    }
}