package com.smf.customer.view.addServices

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.smf.customer.app.base.BaseViewModel
import com.smf.customer.app.base.MyApplication
import com.smf.customer.data.model.request.AddServicesReqDTO
import com.smf.customer.data.model.response.EventInfoResponseDto
import com.smf.customer.data.model.response.GetServicesDTO
import com.smf.customer.data.model.response.ResponseDTO
import com.smf.customer.data.model.response.ServiceData
import com.smf.customer.di.sharedpreference.SharedPrefsHelper
import io.reactivex.Observable
import javax.inject.Inject

class AddServiceViewModel : BaseViewModel() {

    var eventId = 0
    var eventTemplateId = 0
    var servicesList = MutableLiveData<ArrayList<ServiceData>>()
    var preSelectedServices = ArrayList<String>()
    var selectedServices = ArrayList<String>()

    @Inject
    lateinit var sharedPrefsHelper: SharedPrefsHelper

    init {
        MyApplication.applicationComponent?.inject(this)
    }

    fun getAddServices() {
        val observable: Observable<GetServicesDTO> =
            retrofitHelper.getEventRepository().getAddServices(getUserToken(), eventTemplateId)
        this.observable.value = observable as Observable<ResponseDTO>
        doNetworkOperation()
    }

    fun postAddServices() {
        val observable: Observable<EventInfoResponseDto> =
            retrofitHelper.getEventRepository()
                .postAddServices(
                    getUserToken(),
                    eventId,
                    getServicesList()
                )
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
            is EventInfoResponseDto -> {
                Log.d(TAG, "onSuccess: EventInfoResponseDto called $responseDTO")
                callBackInterface?.onClickSaveService()
            }
        }
    }

    private fun getServicesList(): ArrayList<AddServicesReqDTO> {
        val selectedServiceList = ArrayList<AddServicesReqDTO>()
        val preSelectedServicesList = getSelectedServices() as ArrayList<ServiceData>

        preSelectedServicesList.forEach {
            selectedServiceList.add(
                AddServicesReqDTO(
                    it.actualServiceBudget,
                    it.allocatedBudget,
                    it.bidRequestedCount,
                    it.biddingCutOffDate,
                    it.biddingResponseCount,
                    it.costingType,
                    it.currencyType,
                    it.customerServiceCategory,
                    it.defaultServiceCategory,
                    it.estimatedBudget,
                    it.eventServiceDescriptionId,
                    it.eventServiceId,
                    it.eventServiceStatus,
                    it.isServiceRequired,
                    it.leadPeriod,
                    it.remainingBudget,
                    it.serviceCategoryId,
                    it.serviceCloseDate,
                    it.serviceDate,
                    it.serviceName,
                    it.serviceProviderName,
                    it.serviceStartDate,
                    it.serviceTemplateIcon,
                    it.templateIcon
                )
            )
        }
        return selectedServiceList
    }

    private fun getSelectedServices(): List<ServiceData> {
        val serviceDataList = ArrayList<ServiceData>()
        serviceDataList.addAll(createSelectedServices(preSelectedServices))
        serviceDataList.addAll(createSelectedServices(selectedServices))
        return serviceDataList
    }

    private fun createSelectedServices(list: ArrayList<String>): ArrayList<ServiceData> {
        val serviceDataList = ArrayList<ServiceData>()
        list.forEach { selectedServiceName ->
            servicesList.value?.filter { serviceData ->
                serviceData.serviceName == selectedServiceName
            }?.forEach {
                serviceDataList.add(it)
            }
        }
        return serviceDataList
    }

    private var callBackInterface: CallBackInterface? = null

    // Initializing CallBack Interface Method
    fun setCallBackInterface(callback: CallBackInterface) {
        callBackInterface = callback
    }

    // CallBack Interface
    interface CallBackInterface {
        fun onClickSaveService()
    }

}