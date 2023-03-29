package com.smf.customer.view.dashboard.fragment.serviceFragment.sharedviewmodel

import com.smf.customer.R
import com.smf.customer.app.base.BaseDashboardViewModel
import com.smf.customer.app.base.MyApplication
import com.smf.customer.app.constant.AppConstant
import com.smf.customer.data.model.response.GetEventInfo
import com.smf.customer.data.model.response.GetEventServiceDataDto
import com.smf.customer.data.model.response.GetEventServiceInfo
import com.smf.customer.data.model.response.ResponseDTO
import com.smf.customer.view.dashboard.fragment.serviceFragment.eventListDashBoard.EventsDashBoardFragment
import com.smf.customer.view.dashboard.fragment.serviceFragment.eventListDashBoard.model.ItemClass
import com.smf.customer.view.dashboard.fragment.serviceFragment.servicedetailsdashboard.expandablelist.ChildData
import com.smf.customer.view.dashboard.fragment.serviceFragment.servicedetailsdashboard.expandablelist.ParentData
import io.reactivex.Observable

class EventsDashBoardViewModel : BaseDashboardViewModel() {

    private val itemClasses: MutableList<ItemClass> = ArrayList()
    val listData: MutableList<ParentData> = ArrayList()

    init {
        MyApplication.applicationComponent?.inject(this)
    }

    val LayoutTwo = 1
    var stepOne = arrayListOf(0, 1, 2, 2, 2, 2)
    var stepTwo = arrayListOf(0, 0, 0, 1, 2, 2)


    fun getEventInfo(eventId: Int) {
        val observable: Observable<GetEventInfo> =
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

    override fun onSuccess(responseDTO: ResponseDTO) {
        super.onSuccess(responseDTO)
        when (responseDTO) {
            is GetEventInfo -> {
                var response = responseDTO.data
            }
            is GetEventServiceInfo -> {
                var response = responseDTO.data
                callBackInterface?.getEventServiceInfo(response)
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

    // 3438 setting bidding details for expandable view
    fun setBiddingDetails(): MutableList<ParentData> {

        val parentData: Array<String> =
            arrayOf(AppConstant.BIDDING_RESPONSE,AppConstant.PAYMENT ,AppConstant.REVIEW_AND_FEEDBACK )

        // 3438 Currently using manual data
        val childDataData1: MutableList<ChildData> = mutableListOf(
            ChildData("Anathapur"), ChildData("Chittoor"), ChildData("Nellore"), ChildData("Guntur")
        )
        val childDataData2: MutableList<ChildData> = mutableListOf(
            ChildData("Rajanna Sircilla"), ChildData("Karimnagar"), ChildData("Siddipet")
        )
        val childDataData3: MutableList<ChildData> =
            mutableListOf(ChildData("Chennai"), ChildData("Erode"))

        val parentObj1 = ParentData(parentTitle = parentData[0], subList = childDataData1)
        val parentObj2 = ParentData(parentTitle = parentData[1], subList = childDataData2)
        val parentObj3 = ParentData(parentTitle = parentData[2])

        listData.add(parentObj1)
        listData.add(parentObj2)
        listData.add(parentObj3)

        return listData
    }

    private var callBackInterface: OnServiceClickListener? = null

    // Initializing Listener Interface
    fun setOnClickListener(listener: OnServiceClickListener) {
        callBackInterface = listener
    }

    // Interface For Invoice Click Listener
    interface OnServiceClickListener {
        fun getEventServiceInfo(listMyEvents: GetEventServiceDataDto)
    }

}