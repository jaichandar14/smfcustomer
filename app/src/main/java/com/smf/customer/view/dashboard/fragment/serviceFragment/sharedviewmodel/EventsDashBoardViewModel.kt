package com.smf.customer.view.dashboard.fragment.serviceFragment.sharedviewmodel

import com.smf.customer.R
import com.smf.customer.app.base.BaseDashboardViewModel
import com.smf.customer.app.base.MyApplication
import com.smf.customer.app.constant.AppConstant
import com.smf.customer.data.model.response.*
import com.smf.customer.di.sharedpreference.SharedPrefsHelper
import com.smf.customer.view.dashboard.fragment.serviceFragment.eventListDashBoard.model.ItemClass
import com.smf.customer.view.dashboard.fragment.serviceFragment.servicedetailsdashboard.expandablelist.ParentData
import io.reactivex.Observable
import javax.inject.Inject

class EventsDashBoardViewModel : BaseDashboardViewModel() {

    private val itemClasses: MutableList<ItemClass> = ArrayList()
    val listData: MutableList<ParentData> = ArrayList()
    private var biddingResponseData = ArrayList<ServiceProviderBiddingResponseDto>()

    @Inject
    lateinit var sharedPrefsHelper: SharedPrefsHelper
    private var countApi: Int = 1
    private var eventTrackStatus: String? = null
    var isExpandable = true

    init {
        MyApplication.applicationComponent?.inject(this)
        countApi = 1
    }

    private val LayoutTwo = 1
    var stepOne = arrayListOf(0, 1, 2, 2, 2, 2)
    var stepTwo = arrayListOf(0, 0, 0, 1, 2, 2)
    var stepThree = arrayListOf(0, 0, 0, 0, 1, 2)
    var eventInfoDTO: GetEventInfoDTO? = null

    fun getEventInfo(eventId: Int) {
        val observable: Observable<GetEventInfoDTO> =
            retrofitHelper.getEventRepository().getEventInfo(getUserToken(), eventId)
        this.observable.value = observable as Observable<ResponseDTO>
        doNetworkOperation()
    }

    fun getEventServiceInfo(eventId: Int) {
        val observable: Observable<GetEventServiceInfo> =
            retrofitHelper.getDashBoardRepository().getEventServiceInfo((getUserToken()), eventId)
        this.observable.value = observable as Observable<ResponseDTO>
        doNetworkOperation()
    }

    fun sendForApproval(eventId: Int) {
        val observable: Observable<EventInfoResponseDto> = retrofitHelper.getDashBoardRepository()
            .sendForApproval((getUserToken()), eventId, "undefined")
        this.observable.value = observable as Observable<ResponseDTO>
        doNetworkOperation()
    }

    fun sendEventTrackStatus(eventId: Int, eventTrackStatus: String) {
        val observable: Observable<ServiceInfoResponse> = retrofitHelper.getDashBoardRepository()
            .sendEventTrackStatus((getUserToken()), eventId, eventTrackStatus)
        this.observable.value = observable as Observable<ResponseDTO>
        doNetworkOperation()
    }

    fun getBiddingResponse(eventId: Int, eventServiceDescriptionId: Long) {
        val observable: Observable<EventServiceBiddingResponseDto> =
            retrofitHelper.getDashBoardRepository()
                .getBiddingResponse((getUserToken()), eventId, eventServiceDescriptionId)
        this.observable.value = observable as Observable<ResponseDTO>
        doNetworkOperation()
    }

    fun deleteService(eventServiceId: Int?) {
        val observable: Observable<BiddingInfoResponse> = retrofitHelper.getDashBoardRepository()
            .deleteService((getUserToken()), eventServiceId!!)
        this.observable.value = observable as Observable<ResponseDTO>
        doNetworkOperation()
    }

    override fun onSuccess(responseDTO: ResponseDTO) {
        super.onSuccess(responseDTO)
        when (responseDTO) {
            is GetEventInfoDTO -> {
                // Update event details to share provideServiceDetails page
                eventInfoDTO = responseDTO
                callBackInterface?.getEventInfo(responseDTO.data.eventMetaDataDto.eventInformationDto)
            }
            is GetEventServiceInfo -> {
                var response = responseDTO.data
                eventTrackStatus = response.eventTrackStatus
                callBackInterface?.getEventServiceInfo(response)
            }
            is EventInfoResponseDto -> {
                var response = responseDTO.data
                callBackInterface?.sendForApproval()
            }
            is ServiceInfoResponse -> {
                callBackInterface?.sendForTrackStatus()
            }
            is EventServiceBiddingResponseDto -> {
                var response = responseDTO.data
                biddingResponseData.addAll(response.serviceProviderBiddingResponseDtos)
                callBackServiceInterface?.getBiddingResponse(response)
            }
            is BiddingInfoResponse -> {
                callBackInterface?.deleteService()
            }

        }

    }

    fun setStatusFlowDetails(status: ArrayList<Int>): MutableList<ItemClass> {

        // pass the arguments
        itemClasses.run {
            // pass the arguments
            add(
                ItemClass(
                    MyApplication.appContext.getString(R.string.add_or_remove_services), status[0]
                )
            )
            add(
                ItemClass(
                    MyApplication.appContext.getString(R.string.add_service_details), status[1]
                )
            )
            add(
                ItemClass(
                    "${MyApplication.appContext.getString(R.string.submit_)}\n${
                        MyApplication.appContext.getString(
                            R.string._request
                        )
                    }", status[2]
                )
            )
            add(
                ItemClass(
                    "${MyApplication.appContext.getString(R.string.vendor_)}\n${
                        MyApplication.appContext.getString(
                            R.string._response
                        )
                    }", status[3]
                )
            )
            add(
                ItemClass(
                    MyApplication.appContext.getString(R.string.servicing_in_progress), status[4]
                )
            )
            add(
                ItemClass(
                    "",
                    0,
                    "${MyApplication.appContext.getString(R.string.closer_)}\n ${""}",
                    LayoutTwo,
                    status[5]
                )
            )
        }
        return itemClasses
    }

    val parentData: Array<String> = arrayOf(
        AppConstant.BIDDING_RESPONSE, AppConstant.PAYMENT, AppConstant.REVIEW_AND_FEEDBACK
    )
    private var callBackInterface: OnServiceClickListener? = null
    private var callBackServiceInterface: OnServiceDetailsClickListener? = null

    // Initializing Listener Interface
    fun setOnClickListener(listener: OnServiceClickListener) {
        callBackInterface = listener
    }

    // Initializing Listener Interface
    fun setOnServiceClickListener(listeners: OnServiceDetailsClickListener) {
        callBackServiceInterface = listeners
    }

    // Interface For Invoice Click Listener
    interface OnServiceClickListener {
        fun getEventServiceInfo(listMyEvents: GetEventServiceDataDto)
        fun sendForApproval()
        fun sendForTrackStatus()
        fun deleteService()
        fun getEventInfo(eventInformationDto: EventInformationDto)
    }

    interface OnServiceDetailsClickListener {
        fun getBiddingResponse(response: DataBidding)
    }

}