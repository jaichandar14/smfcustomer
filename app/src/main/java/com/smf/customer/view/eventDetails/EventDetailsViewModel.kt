package com.smf.customer.view.eventDetails

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.smf.customer.R
import com.smf.customer.app.base.BaseViewModel
import com.smf.customer.app.base.MyApplication
import com.smf.customer.app.constant.AppConstant
import com.smf.customer.data.model.request.*
import com.smf.customer.data.model.response.EventInfoResponseDto
import com.smf.customer.data.model.response.EventQuestionsResponseDTO
import com.smf.customer.data.model.response.ResponseDTO
import com.smf.customer.di.sharedpreference.SharedPrefConstant
import com.smf.customer.di.sharedpreference.SharedPrefsHelper
import io.reactivex.Observable
import javax.inject.Inject

class EventDetailsViewModel : BaseViewModel() {
    var countryList: ArrayList<String> = ArrayList()
    var stateList: ArrayList<List<String>> = ArrayList()
    var currencyTypeList = ArrayList<String>()
    var iKnowVenue = MutableLiveData<Boolean>()
    var iKnowVenueLayoutVisibility = MutableLiveData<Boolean>()

    var currencyPosition = MutableLiveData<Int>(0)
    var eventName = MutableLiveData<String>("")
    var eventDate = MutableLiveData<String>("")
    var noOfAttendees = MutableLiveData<String>("")
    var totalBudget = MutableLiveData<String>("")
    var address1 = MutableLiveData<String>("")
    var address2 = MutableLiveData<String>("")
    var countryPosition = MutableLiveData<Int>(0)
    var statePosition = MutableLiveData<Int>(0)
    var city = MutableLiveData<String>("")
    var zipCode = MutableLiveData<String>("")
    var name = MutableLiveData<String>("")
    var mobileNumber = MutableLiveData<String>("")
    var emailId = MutableLiveData<String>("")

    var eventNameError = MutableLiveData<Boolean>()
    var eventDateError = MutableLiveData<Boolean>()
    var noOfAttendeesError = MutableLiveData<Boolean>()
    var totalBudgetError = MutableLiveData<Boolean>()
    var address1Error = MutableLiveData<Boolean>()
    var address2Error = MutableLiveData<Boolean>()
    var countryPositionError = MutableLiveData<Boolean>()
    var statePositionError = MutableLiveData<Boolean>()
    var cityError = MutableLiveData<Boolean>()
    var zipCodeError = MutableLiveData<Boolean>()
    var nameError = MutableLiveData<Boolean>()
    var mobileNumberError = MutableLiveData<Boolean>()
    var emailIdError = MutableLiveData<Boolean>()

    // Avoid screen rotation api call
    var screenRotationStatus = MutableLiveData<Boolean>(false)

    // Start question btn text
    var questionBtnText =
        MutableLiveData(MyApplication.appContext.resources.getString(R.string.start_questions))
    var editImageVisibility = MutableLiveData<Boolean>(false)

    @Inject
    lateinit var sharedPrefsHelper: SharedPrefsHelper

    // Activity Variables
    var eventQuestionsResponseDTO: EventQuestionsResponseDTO? = null
    var templateId: Int? = 0
    var selectedAnswerPositionMap = HashMap<Int, Int>()
    var questionStatus: String = ""
    var questionNumber: Int = 0
    var viewOrderQuestionNumber: Int = 0

    init {
        MyApplication.applicationComponent.inject(this)
        // Update CurrencyType ArrayList
        currencyTypeList = MyApplication.appContext.resources.getStringArray(R.array.currency_type)
            .toList() as ArrayList<String>
        iKnowVenue.value = true
        // set idToken value
        idToken = sharedPrefsHelper[SharedPrefConstant.ACCESS_TOKEN, ""]
    }

    fun venueVisibility(value: Boolean) {
        iKnowVenueLayoutVisibility.value = value
    }

    fun onClickNextButton() {
        if (iKnowVenue.value != true) {
            if (!eventName.value.isNullOrEmpty() && !eventDate.value.isNullOrEmpty() &&
                !noOfAttendees.value.isNullOrEmpty() && !totalBudget.value.isNullOrEmpty() &&
                !zipCode.value.isNullOrEmpty() && !name.value.isNullOrEmpty() &&
                !mobileNumber.value.isNullOrEmpty() && !emailId.value.isNullOrEmpty()
            ) {
                if (questionStatus == AppConstant.SUBMIT) {
                    // Post Event info details
                    postEventInfo(idToken, createEventInfoDto())

                } else {
                    showToastMessage("Please submit all questions")
                }
            } else {
                // Set error
                setIWillBeSelectingErrorVisible()
            }
        } else {
            if (!eventName.value.isNullOrEmpty() && !eventDate.value.isNullOrEmpty() &&
                !noOfAttendees.value.isNullOrEmpty() && !totalBudget.value.isNullOrEmpty() &&
                !address1.value.isNullOrEmpty() && !address2.value.isNullOrEmpty() &&
                countryPosition.value != 0 && !city.value.isNullOrEmpty() &&
                !name.value.isNullOrEmpty() && !mobileNumber.value.isNullOrEmpty() &&
                !emailId.value.isNullOrEmpty() && countryPosition.value != null &&
                statePosition.value != null
            ) {
                if (questionStatus == AppConstant.SUBMIT) {
                    // Post Event info details
                    postEventInfo(idToken, createEventInfoDto())

                } else {
                    showToastMessage("Please submit all questions")
                }
            } else {
                // Set error
                setIWillBeSelectingErrorVisible()
                setIKnowVenueErrorVisible()
            }
        }
    }

    private fun postEventInfo(
        idToken: String, eventInfo: EventInfoDTO
    ) {
        val observable: Observable<EventInfoResponseDto> =
            retrofitHelper.getEventRepository().postEventInfo(idToken, eventInfo)
        this.observable.value = observable as Observable<ResponseDTO>
        doNetworkOperation()
    }

    private fun setIWillBeSelectingErrorVisible() {
        if (eventName.value.isNullOrEmpty()) {
            eventNameError.value = true
        }
        if (eventDate.value.isNullOrEmpty()) {
            eventDateError.value = true
        }
        if (noOfAttendees.value.isNullOrEmpty()) {
            noOfAttendeesError.value = true
        }
        if (totalBudget.value.isNullOrEmpty()) {
            totalBudgetError.value = true
        }
        if (zipCode.value.isNullOrEmpty()) {
            zipCodeError.value = true
        }
        if (name.value.isNullOrEmpty()) {
            nameError.value = true
        }
        if (mobileNumber.value.isNullOrEmpty()) {
            mobileNumberError.value = true
        }
        if (emailId.value.isNullOrEmpty()) {
            emailIdError.value = true
        }
    }

    private fun setIKnowVenueErrorVisible() {
        if (address1.value.isNullOrEmpty()) {
            address1Error.value = true
        }
        if (address2.value.isNullOrEmpty()) {
            address2Error.value = true
        }
        if (countryPosition.value == 0) {
            countryPositionError.value = true
        }
        if (stateList[countryPosition.value!!][statePosition.value!!] == "Select your state") {
            statePositionError.value = true
        }
        if (city.value.isNullOrEmpty()) {
            cityError.value = true
        }
    }

    private var eventQuestionDialog = MutableLiveData<Boolean>(false)

    fun showEventQuestionDialogFlag() {
        eventQuestionDialog.value = true
    }

    fun hideEventQuestionDialogFlag() {
        eventQuestionDialog.value = false
    }

    fun isEventQuestionDialogVisible(): Boolean {
        return eventQuestionDialog.value!!
    }

    private var datePickerDialog = MutableLiveData<Boolean>(false)

    fun showDatePickerDialogFlag() {
        datePickerDialog.value = true
    }

    fun hideDatePickerDialogFlag() {
        datePickerDialog.value = false
    }

    fun isDatePickerDialogVisible(): Boolean {
        return datePickerDialog.value!!
    }

    fun getEventDetailsQuestions(
        idToken: String, eventTemplateId: Int
    ) {
        val observable: Observable<EventQuestionsResponseDTO> =
            retrofitHelper.getEventRepository().getEventDetailQuestions(idToken, eventTemplateId)
        this.observable.value = observable as Observable<ResponseDTO>
        doNetworkOperation()
    }

    override fun onSuccess(responseDTO: ResponseDTO) {
        super.onSuccess(responseDTO)
        when (responseDTO) {
            is EventQuestionsResponseDTO -> {
                this.eventQuestionsResponseDTO = responseDTO as EventQuestionsResponseDTO
                callBackInterface?.updateQuestions(responseDTO as EventQuestionsResponseDTO)
            }
            is EventInfoResponseDto -> {
                // Go to dashboard
                callBackInterface?.onClickNext()
            }
        }
    }

    override fun onError(throwable: Throwable) {
        super.onError(throwable)
        Log.d(TAG, "onSuccess: event info error")
    }

    private var callBackInterface: CallBackInterface? = null

    // Initializing CallBack Interface Method
    fun setCallBackInterface(callback: CallBackInterface) {
        callBackInterface = callback
    }

    // CallBack Interface
    interface CallBackInterface {
        fun updateQuestions(eventQuestionsResponseDTO: EventQuestionsResponseDTO)
        fun onClickNext()
    }

    private fun createEventInfoDto(): EventInfoDTO {
        val eventId: Int = 0
        val eventMetaDataDto = createEventMetaDataDto()
        val eventOrganizerId: String = sharedPrefsHelper[SharedPrefConstant.USER_ID, ""]
        val eventQuestionMetaDataDto = createEventQuestionMetaDataDto()
        val eventTypeId: Int = templateId!!
        val id: String = ""
        return EventInfoDTO(
            eventId,
            eventMetaDataDto,
            eventOrganizerId,
            eventQuestionMetaDataDto,
            eventTypeId,
            id
        )
    }

    private fun createEventQuestionMetaDataDto(): EventQuestionMetaDataDto {
        val noOfEventOrganizers: Int = eventQuestionsResponseDTO!!.data.noOfEventOrganizers
        val noOfVendors: Int = eventQuestionsResponseDTO!!.data.noOfVendors
        val questionnaireDtoList = ArrayList<QuestionnaireDto>()
        eventQuestionsResponseDTO!!.data.questionnaireDtos.forEach {
            val qusNumber = eventQuestionsResponseDTO!!.data.questionnaireDtos.indexOf(
                it
            )
            val answerNumber = selectedAnswerPositionMap[qusNumber]
            Log.d(
                TAG,
                "createEvent: answer ${it.questionMetadata.choices[answerNumber!!]}"
            )
            val questionMetadata = QuestionMetadata(
                it.questionMetadata.choices[answerNumber!!], it.questionMetadata.choices,
                it.questionMetadata.eventOrganizer, it.questionMetadata.filter,
                it.questionMetadata.question, it.questionMetadata.questionType,
                it.questionMetadata.vendor
            )
            questionnaireDtoList.add(
                QuestionnaireDto(
                    it.active,
                    listOf<String>(),
                    it.eventTemplateId,
                    it.id,
                    "questionFormat",
                    questionMetadata,
                    it.serviceCategoryId
                )
            )
        }
        return EventQuestionMetaDataDto(noOfEventOrganizers, noOfVendors, questionnaireDtoList)
    }

    private fun createEventMetaDataDto(): EventMetaDataDto {
        val eventInformationDto = EventInformationDto(
            "",
            noOfAttendees.value!!,
            currencyTypeList[currencyPosition.value!!],
            totalBudget.value!!.toInt(),
            eventDate.value!!,
            eventName.value!!
        )
        val hostInformationDto =
            HostInformationDto(emailId.value!!, mobileNumber.value!!, name.value!!)

        val venueInformationDto = VenueInformationDto(
            address1.value!!,
            address2.value!!,
            city.value!!,
            countryList[countryPosition.value!!],
            iKnowVenue.value!!,
            stateList[countryPosition.value!!][statePosition.value!!],
            zipCode.value!!
        )
        return EventMetaDataDto(
            eventInformationDto, hostInformationDto, venueInformationDto
        )
    }

}