package com.smf.customer.view.viewservicedetails

import com.smf.customer.app.base.BaseViewModel
import com.smf.customer.app.base.MyApplication
import com.smf.customer.data.model.response.GetServiceDetailsDTO
import com.smf.customer.data.model.response.QuestionnaireWrapperDto
import com.smf.customer.data.model.response.ResponseDTO
import io.reactivex.Observable

class ViewServiceDetailsViewModel : BaseViewModel() {

    var eventServiceDescriptionId: Long = 0
    var serviceTitle: String = ""
    var serviceIcon: String = ""

    init {
        MyApplication.applicationComponent?.inject(this)
    }

    fun getServiceDescription(eventServiceDescriptionId: Long) {
        val observable: Observable<GetServiceDetailsDTO> =
            retrofitHelper.getServiceRepository().getServiceDescription(
                getUserToken(),
                eventServiceDescriptionId
            )
        this.observable.value = observable as Observable<ResponseDTO>
        doNetworkOperation()
    }

    override fun onSuccess(responseDTO: ResponseDTO) {
        super.onSuccess(responseDTO)
        when (responseDTO) {
            is GetServiceDetailsDTO -> {
                // Update service details
                callBackInterface?.updateServiceDetails(responseDTO)
            }
        }
    }

    fun getQuesListItem(
        eventQuestionMetaDataDto: QuestionnaireWrapperDto
    ): ArrayList<String> {
        return ArrayList<String>().apply {
            eventQuestionMetaDataDto.questionnaireDtos.forEach {
                this.add(it.questionMetadata.question)
            }
        }
    }

    fun getAnswers(
        eventQuestionMetaDataDto: QuestionnaireWrapperDto
    ): ArrayList<String> {
        return ArrayList<String>().apply {
            eventQuestionMetaDataDto.questionnaireDtos.forEach { questionnaireDto ->
                this.add(questionnaireDto.questionMetadata.answer ?: "")
            }
        }
    }

    private var callBackInterface: CallBackInterface? = null

    // Initializing CallBack Interface Method
    fun setCallBackInterface(callback: CallBackInterface) {
        callBackInterface = callback
    }

    // CallBack Interface
    interface CallBackInterface {
        fun updateServiceDetails(getServiceDetailsDTO: GetServiceDetailsDTO)
    }
}