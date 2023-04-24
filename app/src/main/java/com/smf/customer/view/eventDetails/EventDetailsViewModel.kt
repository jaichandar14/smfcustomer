package com.smf.customer.view.eventDetails

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.smf.customer.R
import com.smf.customer.app.base.BaseViewModel
import com.smf.customer.app.base.MyApplication
import com.smf.customer.app.constant.AppConstant
import com.smf.customer.data.model.dto.EventDetailsDTO
import com.smf.customer.data.model.dto.QuestionListItem
import com.smf.customer.data.model.request.*
import com.smf.customer.data.model.request.EventInformationDto
import com.smf.customer.data.model.request.EventMetaDataDto
import com.smf.customer.data.model.request.HostInformationDto
import com.smf.customer.data.model.request.VenueInformationDto
import com.smf.customer.data.model.response.*
import com.smf.customer.data.model.response.QuestionnaireWrapperDto
import com.smf.customer.di.sharedpreference.SharedPrefConstant
import com.smf.customer.di.sharedpreference.SharedPrefsHelper
import com.smf.customer.utility.CountryCodeSeparator
import com.smf.customer.utility.Util
import io.reactivex.Observable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

class EventDetailsViewModel : BaseViewModel() {
    var countryList: ArrayList<String> = ArrayList()
    var stateList: ArrayList<List<String>> = ArrayList()
    var currencyTypeList = ArrayList<String>()
    var countryCodeList = ArrayList<String>()
    var iKnowVenue = MutableLiveData<Boolean>()
    var iKnowVenue2 = MutableLiveData<Boolean>()
    var iKnowVenueLayoutVisibility = MutableLiveData<Boolean>()

    var currencyPosition = MutableLiveData<Int>(0)
    var eventTitle = MutableLiveData<String>("")
    var eventName = MutableLiveData<String>("")
    var eventDate = MutableLiveData<String>("")
    var noOfAttendees = MutableLiveData<String>("")
    var totalBudget = MutableLiveData<String>(null)
    var address1 = MutableLiveData<String>("")
    var address2 = MutableLiveData<String>("")
    var city = MutableLiveData<String>("")
    var zipCode = MutableLiveData<String>("")
    var name = MutableLiveData<String>("")
    var emailId = MutableLiveData<String>("")
    var mobileNumber = MutableLiveData<String>("")
    var countryCode = MutableLiveData(0)

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
    var startQuestionsBtnVisibility = MutableLiveData<Boolean>(false)
    var provideEventDetailsTxtVisibility = MutableLiveData<Boolean>(false)
    var editImageVisibility = MutableLiveData<Boolean>(false)

    @Inject
    lateinit var sharedPrefsHelper: SharedPrefsHelper

    // Activity Variables
    var eventQuestionsResponseDTO: EventQuestionsResponseDTO? = null
    var templateId: Int = 0
    var eventId: Int = 0
    var questionListItem: ArrayList<QuestionListItem> = ArrayList<QuestionListItem>()
    var questionNumberList: ArrayList<Int> = ArrayList<Int>()
    var eventSelectedAnswerMap: HashMap<Int, ArrayList<String>> = HashMap<Int, ArrayList<String>>()

    init {
        MyApplication.applicationComponent?.inject(this)
        // Update CurrencyType ArrayList
        currencyTypeList = MyApplication.appContext.resources.getStringArray(R.array.currency_type)
            .toList() as ArrayList<String>
        countryCodeList =
            MyApplication.appContext.resources.getStringArray(R.array.CountryCodes)
                .toList() as ArrayList<String>
    }

    fun getEventDetailsQuestions(eventTemplateId: Int) {
        val observable: Observable<EventQuestionsResponseDTO> =
            retrofitHelper.getEventRepository()
                .getEventDetailQuestions(getUserToken(), eventTemplateId)
        this.observable.value = observable as Observable<ResponseDTO>
        doNetworkOperation()
    }

    // Modify event details
    fun getEventInfo(eventId: Int) {
        val observable: Observable<GetEventInfoDTO> =
            retrofitHelper.getEventRepository()
                .getEventInfo(getUserToken(), eventId)
        this.observable.value = observable as Observable<ResponseDTO>
        doNetworkOperation()
    }

    fun postEventInfo(eventRegistrationDto: EventRegistrationDto) {
        val observable: Observable<EventInfoResponseDto> =
            retrofitHelper.getEventRepository().postEventInfo(getUserToken(), eventRegistrationDto)
        this.observable.value = observable as Observable<ResponseDTO>
        doNetworkOperation()
    }

    private fun putEventInfo(eventRegistrationDto: EventRegistrationDto) {
        val observable: Observable<EventInfoResponseDto> =
            retrofitHelper.getEventRepository().putEventInfo(getUserToken(), eventRegistrationDto)
        this.observable.value = observable as Observable<ResponseDTO>
        doNetworkOperation()
    }

    override fun onSuccess(responseDTO: ResponseDTO) {
        super.onSuccess(responseDTO)
        when (responseDTO) {
            is EventQuestionsResponseDTO -> {
                callBackInterface?.updateQuestions(responseDTO)
            }
            is EventInfoResponseDto -> {
                // Get and update eventId
                eventId = responseDTO.data.key
                // Go to dashboard
                callBackInterface?.onClickNext()
            }
            is GetEventInfoDTO -> {
                // Modify event details
                Log.d(TAG, "modify onSuccess: $responseDTO")
                viewModelScope.launch {
                    val eventDetailsDTO =
                        withContext(Dispatchers.IO) {
                            updateEventDetailsForModify(responseDTO)
                        }
                    updateDetailsToUI(eventDetailsDTO)
                }
            }
        }
    }

    private fun updateEventDetailsForModify(responseDTO: GetEventInfoDTO): EventDetailsDTO {
        val selectedCountryPosition = Util.getCountryPosition(
            countryList,
            responseDTO.data.eventMetaDataDto.venueInformationDto.country
        )
        val selectedStatePosition = Util.getStatePosition(
            countryList, stateList, responseDTO.data.eventMetaDataDto.venueInformationDto.country,
            responseDTO.data.eventMetaDataDto.venueInformationDto.state
        )
        val questionListItem = getQuesListItem(responseDTO.data.eventQuestionMetaDataDto)
        val questionNumberList = getQuestionNumberList(questionListItem.size)
        val eventSelectedAnswerMap =
            getSelectedAnswerMap(responseDTO.data.eventQuestionMetaDataDto)
        val eventQuestionsResponseDTO = getEventQuestionsResponseDTO(responseDTO)

        return EventDetailsDTO(
            setEventTitle(responseDTO.data.eventTypeName),
            responseDTO.data.eventTypeId,
            responseDTO.data.eventId,
            responseDTO.data.eventMetaDataDto.eventInformationDto.eventName,
            responseDTO.data.eventMetaDataDto.eventInformationDto.eventDate,
            responseDTO.data.eventMetaDataDto.eventInformationDto.attendeesCount.toString(),
            currencyTypeList.indexOf(responseDTO.data.eventMetaDataDto.eventInformationDto.currencyType),
            responseDTO.data.eventMetaDataDto.eventInformationDto.estimatedEventBudget,
            responseDTO.data.eventMetaDataDto.venueInformationDto.knownVenue,
            responseDTO.data.eventMetaDataDto.venueInformationDto.addressLine1,
            responseDTO.data.eventMetaDataDto.venueInformationDto.addressLine2,
            selectedCountryPosition,
            selectedStatePosition,
            responseDTO.data.eventMetaDataDto.venueInformationDto.city,
            responseDTO.data.eventMetaDataDto.venueInformationDto.zipCode,
            responseDTO.data.eventMetaDataDto.hostInformationDto.name,
            responseDTO.data.eventMetaDataDto.hostInformationDto.email,
            responseDTO.data.eventMetaDataDto.hostInformationDto.mobile,
            questionListItem, questionNumberList, eventSelectedAnswerMap,
            eventQuestionsResponseDTO
        )
    }

    fun getQuesListItem(
        eventQuestionMetaDataDto: QuestionnaireWrapperDto
    ): ArrayList<QuestionListItem> {
        return ArrayList<QuestionListItem>().apply {
            eventQuestionMetaDataDto.questionnaireDtos.forEach {
                this.add(
                    QuestionListItem(
                        it.questionMetadata.question,
                        it.questionMetadata.choices as ArrayList<String>,
                        it.questionMetadata.questionType,
                        it.questionMetadata.isMandatory
                    )
                )
            }
        }
    }

    fun getQuestionNumberList(questionListItemSize: Int): ArrayList<Int> {
        return ArrayList<Int>().apply {
            for (i in 0 until questionListItemSize) {
                this.add(i)
            }
        }
    }

    fun getSelectedAnswerMap(
        eventQuestionMetaDataDto: QuestionnaireWrapperDto
    ): HashMap<Int, ArrayList<String>> {
        return HashMap<Int, ArrayList<String>>().apply {
            eventQuestionMetaDataDto.questionnaireDtos.forEach { questionnaireDto ->
                questionnaireDto.questionMetadata.answer?.let { answerList ->
                    // Update only selected answer position
                    this[eventQuestionMetaDataDto.questionnaireDtos.indexOf(questionnaireDto)] =
                        ArrayList<String>().apply {
                            this.addAll(answerList.split(","))
                        }
                }
            }
        }
    }

    private fun getEventQuestionsResponseDTO(responseDTO: GetEventInfoDTO): EventQuestionsResponseDTO {
        return EventQuestionsResponseDTO(
            QuestionnaireWrapperDto(
                responseDTO.data.eventQuestionMetaDataDto.noOfEventOrganizers,
                responseDTO.data.eventQuestionMetaDataDto.noOfVendors,
                responseDTO.data.eventQuestionMetaDataDto.questionnaireDtos
            ),
            responseDTO.result,
            responseDTO.success
        )
    }

    fun onClickQuestionsBtn() {
        callBackInterface?.onClickQuestionsBtn(AppConstant.QUESTION_BUTTON)
    }

    fun onClickQuesEditBtn() {
        callBackInterface?.onClickQuestionsBtn(AppConstant.EDIT_BUTTON)
    }

    fun onClickNextButton() {
        // Verify all mandatory questions answered before submit
        if (verifyMandatoryQuesAnswered(questionListItem, eventSelectedAnswerMap)) {
            if (venueValidation()) {
                if (eventId == 0) {
                    // Post Event info details
                    postEventInfo(createEventRegistrationDto())
                } else {
                    // Put Event info details
                    putEventInfo(createEventRegistrationDto())
                }
            } else {
                updateError()
            }
        } else {
            showToastMessage(MyApplication.appContext.resources.getString(R.string.please_answer_all_mandatory_questions))
        }
    }

    fun verifyMandatoryQuesAnswered(
        questionListItem: ArrayList<QuestionListItem>?,
        eventSelectedAnswerMap: HashMap<Int, ArrayList<String>>?
    ): Boolean {
        return if (questionListItem?.isNotEmpty() == true) {
            val mandatoryQuesIndexList = ArrayList<Int>().apply {
                questionListItem.filter { it.isMandatory }.forEach {
                    add(questionListItem.indexOf(it))
                }
            }
            // Verify all mandatory questions are answered
            eventSelectedAnswerMap?.keys?.containsAll(mandatoryQuesIndexList) ?: false
        } else {
            true
        }
    }

    private fun venueValidation(): Boolean {
        if (iKnowVenue.value != true) {
            return eventName.value.isNullOrEmpty().not() && eventDate.value.isNullOrEmpty()
                .not() &&
                    noOfAttendees.value.isNullOrEmpty().not() && totalBudget.value.isNullOrEmpty()
                .not() && totalBudgetError.value != true &&
                    zipCode.value.isNullOrEmpty().not() && name.value.isNullOrEmpty().not() &&
                    mobileNumber.value.isNullOrEmpty().not() && emailId.value.isNullOrEmpty().not()
        } else {
            return eventName.value.isNullOrEmpty().not() && eventDate.value.isNullOrEmpty().not()
                    && noOfAttendees.value.isNullOrEmpty().not() &&
                    totalBudget.value.isNullOrEmpty().not() && totalBudgetError.value != true &&
                    address1.value.isNullOrEmpty().not() && address2.value.isNullOrEmpty().not()
                    && selectedCountryPosition != 0 && city.value.isNullOrEmpty().not()
                    && zipCode.value.isNullOrEmpty().not() &&
                    name.value.isNullOrEmpty().not() && mobileNumber.value.isNullOrEmpty().not() &&
                    emailId.value.isNullOrEmpty().not()
        }
    }

    private fun updateError() {
        if (iKnowVenue.value != true) {
            setIWillBeSelectingErrorVisible()
        } else {
            setIWillBeSelectingErrorVisible()
            setIKnowVenueErrorVisible()
        }
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

    fun venueVisibility(value: Boolean) {
        iKnowVenueLayoutVisibility.value = value
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

    fun editButtonVisibility() {
        // Update questions button text
        if (eventSelectedAnswerMap.isNotEmpty()) {
            questionBtnText.value =
                MyApplication.appContext.resources.getString(R.string.view_order) + " {${eventSelectedAnswerMap.keys.size}}"
            editImageVisibility.value = true
        } else {
            questionBtnText.value =
                MyApplication.appContext.resources.getString(R.string.start_questions)
            editImageVisibility.value = false
        }
    }

    fun showStartQuestionsBtn() {
        startQuestionsBtnVisibility.value = true
        provideEventDetailsTxtVisibility.value = true
    }

    fun hideStartQuestionsBtn() {
        startQuestionsBtnVisibility.value = false
        provideEventDetailsTxtVisibility.value = false
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
        fun onClickQuestionsBtn(from: String)
        fun updateCountryCode(code: Int)
        fun updateCountryAndState(selectedCountryPosition: Int, selectedStatePosition: Int)
    }

    private fun createEventRegistrationDto(): EventRegistrationDto {
        val eventId: Int = eventId
        val eventTypeId: Int = templateId
        val eventTypeName: String = eventTitle.value ?: ""
        val eventMetaDataDto = createEventMetaDataDto()
        val eventOrganizerId: String = sharedPrefsHelper[SharedPrefConstant.USER_ID, ""]
        val eventQuestionMetaDataDto = createEventQuestionMetaDataDto()

        return EventRegistrationDto(
            eventId = eventId,
            eventTypeId = eventTypeId,
            eventTypeName = eventTypeName,
            eventMetaDataDto = eventMetaDataDto,
            eventQuestionMetaDataDto = eventQuestionMetaDataDto,
            eventOrganizerId = eventOrganizerId
        )
    }

    private fun createEventMetaDataDto(): EventMetaDataDto {
        val eventInformationDto = EventInformationDto(
            "",
            noOfAttendees.value ?: "",
            currencyTypeList[currencyPosition.value ?: 0],
            totalBudget.value ?: "0",
            eventDate.value ?: "",
            eventName.value ?: ""
        )
        val hostInformationDto = HostInformationDto(
            emailId.value ?: "",
            "+".plus(countryCode.value).plus(mobileNumber.value ?: ""),
            name.value ?: ""
        )
        val venueInformationDto = if (iKnowVenue.value == false) {
            VenueInformationDto(
                "", "", "", "", false, "",
                zipCode.value ?: ""
            )
        } else {
            VenueInformationDto(
                address1.value ?: "", address2.value ?: "", city.value ?: "",
                countryList[selectedCountryPosition], iKnowVenue.value ?: false,
                stateList[selectedCountryPosition][selectedStatePosition], zipCode.value ?: ""
            )
        }
        return EventMetaDataDto(
            eventInformationDto, hostInformationDto, venueInformationDto
        )
    }

    private fun createEventQuestionMetaDataDto(): QuestionnaireWrapperDto {
        val noOfEventOrganizers: Int = eventQuestionsResponseDTO?.data?.noOfEventOrganizers ?: 0
        val noOfVendors: Int = eventQuestionsResponseDTO?.data?.noOfVendors ?: 0
        val questionnaireDtoList = ArrayList<QuestionnaireDto>()
        eventQuestionsResponseDTO?.data?.questionnaireDtos?.let { questionnaireDtos ->
            questionnaireDtos.forEach {
                val qusNumber = eventQuestionsResponseDTO?.data?.questionnaireDtos?.indexOf(it)
                val answer = eventSelectedAnswerMap[qusNumber]
                val questionMetadata = QuestionMetadata(
                    answer?.joinToString(),
                    it.questionMetadata.choices,
                    it.questionMetadata.eventOrganizer,
                    it.questionMetadata.filter,
                    it.questionMetadata.question,
                    it.questionMetadata.questionType,
                    it.questionMetadata.vendor,
                    it.questionMetadata.isMandatory
                )
                questionnaireDtoList.add(
                    QuestionnaireDto(
                        it.active,
                        it.eventTemplateId,
                        it.id,
                        questionMetadata,
                        it.serviceCategoryId
                    )
                )
            }
        }
        return QuestionnaireWrapperDto(noOfEventOrganizers, noOfVendors, questionnaireDtoList)
    }

    fun updateDetailsToUI(eventDetailsDTO: EventDetailsDTO) {
        eventTitle.value = eventDetailsDTO.eventTitle
        templateId = eventDetailsDTO.templateId
        eventId = eventDetailsDTO.eventId
        eventName.value = eventDetailsDTO.eventName
        eventDate.value = eventDetailsDTO.eventDate
        noOfAttendees.value = eventDetailsDTO.noOfAttendees
        currencyPosition.value = eventDetailsDTO.currencyPosition
        totalBudget.value = eventDetailsDTO.totalBudget
        iKnowVenue.value = eventDetailsDTO.iKnowVenue
        iKnowVenue2.value = eventDetailsDTO.iKnowVenue.not()
        address1.value = eventDetailsDTO.address1
        address2.value = eventDetailsDTO.address2
        selectedCountryPosition = eventDetailsDTO.selectedCountryPosition
        selectedStatePosition = eventDetailsDTO.selectedStatePosition
        callBackInterface?.updateCountryAndState(selectedCountryPosition, selectedStatePosition)
        city.value = eventDetailsDTO.city
        zipCode.value = eventDetailsDTO.zipCode
        name.value = eventDetailsDTO.name
        emailId.value = eventDetailsDTO.emailId
        countryCode.value = CountryCodeSeparator.getCountry(
            countryCodeList, eventDetailsDTO.mobileNumberWithCountryCode
        ).substringAfter("+").toInt()
        callBackInterface?.updateCountryCode(countryCode.value ?: 91)
        mobileNumber.value =
            eventDetailsDTO.mobileNumberWithCountryCode.substringAfter(countryCode.value.toString())
        // Question data
        questionListItem = eventDetailsDTO.questionListItem
        questionNumberList = eventDetailsDTO.questionNumberList
        // Update selected questions answers
        eventSelectedAnswerMap = eventDetailsDTO.eventSelectedAnswerMap
        eventQuestionsResponseDTO = eventDetailsDTO.eventQuestionsResponseDTO
        // Update questions button text
        if (questionListItem.isNotEmpty()) {
            showStartQuestionsBtn()
            editButtonVisibility()
        }
    }

    fun getEventDetailsDTO(
        eventTitle: String,
        templateId: Int,
        eventId: Int,
        eventName: String,
        eventDate: String,
        noOfAttendees: String,
        currencyPosition: Int,
        totalBudget: String,
        iKnowVenue: Boolean,
        address1: String,
        address2: String,
        selectedCountryPosition: Int,
        selectedStatePosition: Int,
        city: String,
        zipCode: String,
        name: String,
        emailId: String,
        mobileNumberWithCountryCode: String,
        questionListItem: ArrayList<QuestionListItem>,
        questionNumberList: ArrayList<Int>,
        eventSelectedAnswerMap: HashMap<Int, ArrayList<String>>,
        eventQuestionsResponseDTO: EventQuestionsResponseDTO?,
    ): EventDetailsDTO {
        return EventDetailsDTO(
            eventTitle,
            templateId,
            eventId,
            eventName,
            eventDate,
            noOfAttendees,
            currencyPosition,
            totalBudget,
            iKnowVenue,
            address1,
            address2,
            selectedCountryPosition,
            selectedStatePosition,
            city,
            zipCode,
            name,
            emailId,
            mobileNumberWithCountryCode,
            questionListItem,
            questionNumberList,
            eventSelectedAnswerMap,
            eventQuestionsResponseDTO,
        )
    }

    fun getUserEnteredValuesForQuesPage(): EventDetailsDTO {
        return getEventDetailsDTO(
            eventTitle.value?.removeSuffix(
                MyApplication.appContext.resources.getString(R.string.event_Details)
            ) ?: "",
            templateId,
            eventId,
            eventName.value ?: "",
            eventDate.value ?: "",
            noOfAttendees.value ?: "",
            currencyPosition.value ?: 0,
            totalBudget.value ?: "",
            iKnowVenue.value ?: true,
            address1.value ?: "",
            address2.value ?: "",
            selectedCountryPosition,
            selectedStatePosition,
            city.value ?: "",
            zipCode.value ?: "",
            name.value ?: "",
            emailId.value ?: "",
            "+".plus(countryCode.value).plus(mobileNumber.value ?: ""),
            questionListItem,
            questionNumberList,
            eventSelectedAnswerMap,
            eventQuestionsResponseDTO
        )
    }

    fun setEventTitle(eventTitle: String): String {
        return eventTitle.trim().replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
        } + " " + MyApplication.appContext.resources.getString(R.string.event_Details).trim()
    }

}