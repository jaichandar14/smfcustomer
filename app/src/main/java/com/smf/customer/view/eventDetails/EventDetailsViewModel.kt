package com.smf.customer.view.eventDetails

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.smf.customer.R
import com.smf.customer.app.base.BaseViewModel
import com.smf.customer.app.base.MyApplication
import com.smf.customer.data.model.dto.QuestionListItem
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
    var iKnowVenue2 = MutableLiveData<Boolean>(false)
    var iKnowVenueLayoutVisibility = MutableLiveData<Boolean>()

    var currencyPosition = MutableLiveData<Int>(0)
    var eventName = MutableLiveData<String>("")
    var eventDate = MutableLiveData<String>("")
    var noOfAttendees = MutableLiveData<String>("")
    var totalBudget = MutableLiveData<String>(null)
    var address1 = MutableLiveData<String>("")
    var address2 = MutableLiveData<String>("")
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

    // Variable for update country and state position
    var selectedCountryPosition: Int = 0
    var selectedStatePosition: Int = 0

    // Start question btn text
    var questionBtnText =
        MutableLiveData(MyApplication.appContext.resources.getString(R.string.start_questions))
    var editImageVisibility = MutableLiveData<Boolean>(false)

    @Inject
    lateinit var sharedPrefsHelper: SharedPrefsHelper

    // Activity Variables
    var eventQuestionsResponseDTO: EventQuestionsResponseDTO? = null
    var templateId: Int? = 0
    var eventSelectedAnswerMap = HashMap<Int, ArrayList<String>>()
    var viewOrderQuestionNumber: Int = 0

    var questionListItem = ArrayList<QuestionListItem>()
    var questionNumberList = ArrayList<Int>()

    init {
        MyApplication.applicationComponent?.inject(this)
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

    fun onClickQuestionsBtn() {
        // Update entered values to shared preference
        setSharedPreference()
        callBackInterface?.onClickQuestionsBtn()
    }

    fun onClickNextButton() {
        // Verify all mandatory questions answered before submit
        if (verifyMandatoryQuesAnswered(questionListItem, eventSelectedAnswerMap)) {
            if (iKnowVenue.value != true) {
                if (eventName.value.isNullOrEmpty().not() && eventDate.value.isNullOrEmpty()
                        .not() &&
                    noOfAttendees.value.isNullOrEmpty().not() && totalBudget.value.isNullOrEmpty()
                        .not() && totalBudgetError.value != true &&
                    zipCode.value.isNullOrEmpty().not() && name.value.isNullOrEmpty().not() &&
                    mobileNumber.value.isNullOrEmpty().not() && emailId.value.isNullOrEmpty().not()
                ) {
                    // Post Event info details
                    postEventInfo(createEventInfoDto())
                } else {
                    // Set error
                    setIWillBeSelectingErrorVisible()
                }
            } else {
                if (eventName.value.isNullOrEmpty().not() && eventDate.value.isNullOrEmpty().not()
                    && noOfAttendees.value.isNullOrEmpty().not() &&
                    totalBudget.value.isNullOrEmpty().not() && totalBudgetError.value != true &&
                    address1.value.isNullOrEmpty().not() && address2.value.isNullOrEmpty().not()
                    && selectedCountryPosition != 0 && city.value.isNullOrEmpty().not()
                    && zipCode.value.isNullOrEmpty().not() &&
                    name.value.isNullOrEmpty().not() && mobileNumber.value.isNullOrEmpty().not() &&
                    emailId.value.isNullOrEmpty().not()
                ) {
                    // Post Event info details
                    postEventInfo(createEventInfoDto())
                } else {
                    // Set error
                    setIWillBeSelectingErrorVisible()
                    setIKnowVenueErrorVisible()
                }
            }
        }
    }

    fun verifyMandatoryQuesAnswered(
        questionListItem: ArrayList<QuestionListItem>,
        eventSelectedAnswerMap: HashMap<Int, ArrayList<String>>
    ): Boolean {
        if (questionListItem.isNotEmpty()) {
            val mandatoryQuesIndexList = ArrayList<Int>().apply {
                questionListItem.filter { it.isMandatory }.forEach {
                    add(questionListItem.indexOf(it))
                }
            }
            // Verify all mandatory questions are answered
            return if (eventSelectedAnswerMap.keys.containsAll(mandatoryQuesIndexList)) {
                true
            } else {
                if (MyApplication.getAppContextInitialization()) {
                    showToastMessage(MyApplication.appContext.resources.getString(R.string.please_answer_all_mandatory_questions))
                }
                false
            }
        } else {
            return true
        }
    }

    fun postEventInfo(eventInfo: EventInfoDTO) {
        val observable: Observable<EventInfoResponseDto> =
            retrofitHelper.getEventRepository().postEventInfo(getUserToken(), eventInfo)
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
        if (selectedCountryPosition == 0) {
            countryPositionError.value = true
        }
        if (stateList[selectedCountryPosition][selectedStatePosition] ==
            MyApplication.appContext.resources.getString(R.string.select_your_state)
        ) {
            statePositionError.value = true
        }
        if (city.value.isNullOrEmpty()) {
            cityError.value = true
        }
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

    fun getEventDetailsQuestions(eventTemplateId: Int) {
        val observable: Observable<EventQuestionsResponseDTO> =
            retrofitHelper.getEventRepository()
                .getEventDetailQuestions(getUserToken(), eventTemplateId)
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
                // 3420 responseDTO.data.key is  event id we getting from event_info post response
                val response = responseDTO.data.key
                response.let {
                    sharedPrefsHelper.put(SharedPrefConstant.EVENT_ID, it)
                }
                // Update entered values to shared preference
                setSharedPreference()
                // Go to dashboard
                callBackInterface?.onClickNext()
            }
        }
    }

    private fun setSharedPreference() {
        eventName.value?.let {
            sharedPrefsHelper.put(SharedPrefConstant.EVENT_NAME, it.trim())
        }
        eventDate.value?.let {
            sharedPrefsHelper.put(SharedPrefConstant.EVENT_DATE, it)
        }
        noOfAttendees.value?.let {
            sharedPrefsHelper.put(SharedPrefConstant.NO_OF_ATTENDEES, it.trim())
        }
        currencyPosition.value?.let {
            sharedPrefsHelper.put(SharedPrefConstant.CURRENCY_TYPE, it)
        }
        totalBudget.value?.let { sharedPrefsHelper.put(SharedPrefConstant.BUDGET, it.trim()) }
        iKnowVenue.value?.let {
            sharedPrefsHelper.put(SharedPrefConstant.VENUE, it)
        }
        address1.value?.let { sharedPrefsHelper.put(SharedPrefConstant.ADDRESS_1, it.trim()) }
        address2.value?.let { sharedPrefsHelper.put(SharedPrefConstant.ADDRESS_2, it.trim()) }
        sharedPrefsHelper.put(SharedPrefConstant.COUNTRY, selectedCountryPosition)
        sharedPrefsHelper.put(SharedPrefConstant.STATE, selectedStatePosition)
        city.value?.let { sharedPrefsHelper.put(SharedPrefConstant.CITY, it) }
        zipCode.value?.let {
            sharedPrefsHelper.put(SharedPrefConstant.ZIPCODE, it.trim())
        }
        // Update Host Details
        name.value?.let { sharedPrefsHelper.put(SharedPrefConstant.HOST_NAME, it.trim()) }
        mobileNumber.value?.let {
            sharedPrefsHelper.put(SharedPrefConstant.HOST_NUMBER, it.trim())
        }
        emailId.value?.let { sharedPrefsHelper.put(SharedPrefConstant.HOST_EMAIL, it.trim()) }
        sharedPrefsHelper.putHashMap(
            SharedPrefConstant.EVENT_SELECTED_ANSWER_MAP, eventSelectedAnswerMap
        )
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
        fun onClickQuestionsBtn()
    }

    private fun createEventInfoDto(): EventInfoDTO {
        val eventId: Int = 0
        val eventMetaDataDto = createEventMetaDataDto()
        val eventOrganizerId: String = sharedPrefsHelper[SharedPrefConstant.USER_ID, ""]
        val eventQuestionMetaDataDto = createEventQuestionMetaDataDto()
        val eventTypeId: Int = templateId!!
        val id: String = ""
        return EventInfoDTO(
            eventId, eventMetaDataDto, eventOrganizerId, eventQuestionMetaDataDto, eventTypeId, id
        )
    }

    private fun createEventQuestionMetaDataDto(): EventQuestionMetaDataDto {
        val noOfEventOrganizers: Int = eventQuestionsResponseDTO!!.data.noOfEventOrganizers
        val noOfVendors: Int = eventQuestionsResponseDTO!!.data.noOfVendors
        val questionnaireDtoList = ArrayList<QuestionnaireDto>()
        eventQuestionsResponseDTO!!.data.questionnaireDtos.forEach {
            val qusNumber = eventQuestionsResponseDTO!!.data.questionnaireDtos.indexOf(it)
            val answer = eventSelectedAnswerMap[qusNumber]
            val questionMetadata = QuestionMetadata(
                answer?.joinToString(),
                it.questionMetadata.choices,
                it.questionMetadata.eventOrganizer,
                it.questionMetadata.filter,
                it.questionMetadata.question,
                it.questionMetadata.questionType,
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
            noOfAttendees.value ?: "",
            currencyTypeList[currencyPosition.value ?: 0],
            totalBudget.value?.toBigDecimal() ?: "0".toBigDecimal(),
            eventDate.value ?: "",
            eventName.value ?: ""
        )
        val hostInformationDto = HostInformationDto(
            emailId.value ?: "", mobileNumber.value ?: "", name.value ?: ""
        )

        val venueInformationDto = VenueInformationDto(
            address1.value ?: "",
            address2.value ?: "",
            city.value ?: "",
            countryList[selectedCountryPosition],
            iKnowVenue.value ?: false,
            stateList[selectedCountryPosition][selectedStatePosition],
            zipCode.value ?: ""
        )
        return EventMetaDataDto(
            eventInformationDto, hostInformationDto, venueInformationDto
        )
    }

}