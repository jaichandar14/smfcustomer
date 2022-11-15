package com.smf.customer.view.dashboard

import android.util.Log
import com.smf.customer.app.base.BaseViewModel
import com.smf.customer.app.base.MyApplication
import com.smf.customer.data.model.response.ResponseDTO
import com.smf.customer.view.dashboard.responsedto.EventCountDto
import com.smf.customer.view.dashboard.responsedto.EventStatusData
import com.smf.customer.view.dashboard.responsedto.EventStatusResponse
import com.smf.customer.view.dashboard.responsedto.MyEventData
import io.reactivex.Observable

// 3262
class DashBoardViewModel : BaseViewModel() {
    init {
        MyApplication.applicationComponent.inject(this)
    }

    // 3285
    fun getEventCount(idToken: String, username: String) {
        val observable: Observable<EventCountDto> =
            retrofitHelper.getDashBoardRepository().getEventCount(idToken, username)
        this.observable.value = observable as Observable<ResponseDTO>
        doNetworkOperation()
    }

    fun getEventStatus(idToken: String, username: String, status: ArrayList<String>) {
        val observable: Observable<EventStatusResponse> =
            retrofitHelper.getDashBoardRepository().getEventStatus(idToken, username, status)
        this.observable.value = observable as Observable<ResponseDTO>
        doNetworkOperation()
    }

    override fun onSuccess(responseDTO: ResponseDTO) {
        super.onSuccess(responseDTO)
        Log.d(TAG, "onSuccess in Dashboard: ${(responseDTO)}")
        when (responseDTO) {
            is EventCountDto -> {
                Log.d(TAG, "onSuccess in Dashboard: ${(responseDTO.data.approvedEventsCount)}")
                var response = responseDTO.data
                showLoading.value = true
                callBackInterface?.getMyevetList(response)

            }
            is EventStatusResponse -> {
                var response = responseDTO.data
                callBackInterface?.getEventStatus(response)
            }
        }
    }


    override fun onError(throwable: Throwable) {
        super.onError(throwable)

        Log.d(TAG, "onError:${throwable} ")
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