package com.smf.customer.view.provideservicedetails

import android.content.Intent
import androidx.lifecycle.MutableLiveData
import com.smf.customer.R
import com.smf.customer.app.base.BaseViewModel
import com.smf.customer.app.base.MyApplication
import com.smf.customer.app.constant.AppConstant
import com.smf.customer.data.model.dto.QuestionListItem
import com.smf.customer.data.model.request.*
import com.smf.customer.data.model.request.EventServiceBudgetDto
import com.smf.customer.data.model.request.EventServiceDateDto
import com.smf.customer.data.model.request.EventServiceDescriptionDto
import com.smf.customer.data.model.request.EventServiceVenueDto
import com.smf.customer.data.model.request.QuestionnaireWrapperDto
import com.smf.customer.data.model.response.*
import com.smf.customer.data.model.response.QuestionnaireDto
import com.smf.customer.di.sharedpreference.SharedPrefConstant
import com.smf.customer.di.sharedpreference.SharedPrefsHelper
import io.reactivex.Observable
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class ProvideServiceViewModel : BaseViewModel() {
    var serviceName = MutableLiveData<String>()
    var mileList = ArrayList<String>()
    var milePosition = MutableLiveData(0)
    var serviceDate = MutableLiveData<String>()
    var updateServiceDate = MutableLiveData(false)
    var serviceDateErrorText = MutableLiveData("")
    var estimatedBudget = MutableLiveData<String>(null)
    var estimatedBudgetSymbol = MutableLiveData<String>("$")
    var totalAmount = MutableLiveData<String>("")
    var updateTotalAmount: Any =
        0 // For update totalAmount while serviceAmount exceeds the totalAmount
    var remainingAmount = MutableLiveData<String>("")
    var zipCode = MutableLiveData<String>("")
    var amountErrorMessage =
        MutableLiveData(MyApplication.appContext.resources.getString(R.string.estimated_budget_required))

    // Start question btn text
    var questionBtnText =
        MutableLiveData(MyApplication.appContext.resources.getString(R.string.start_questions))

    // TimeSlot title visibility
    var timeSlotTitleVisibility = MutableLiveData(true)
    var timeSlotVisibility = MutableLiveData(true)

    // Edit image icon visibility
    var remainingAmountVisibility = MutableLiveData<Boolean>(false)

    // Variables for error message visibility
    var serviceDateErrorVisibility = MutableLiveData<Boolean>(false)
    var timeSlotErrorVisibility = MutableLiveData<Boolean>(false)
    var amountErrorVisibility = MutableLiveData<Boolean>(false)
    var zipCodeErrorVisibility = MutableLiveData<Boolean>(false)

    var timeSlotList = MutableLiveData<ArrayList<String>>()
    var selectedSlotsList = ArrayList<String>()
    var doBudgetAPICall = MutableLiveData<Boolean>(false)
    var budgetCalcInfo = MutableLiveData<BudgetCalcInfoDTO>(null)

    // Questions
    var updateQuestions = MutableLiveData(false)
    var clearEventSelectedAnswerMap = MutableLiveData(false)
    var eventQuestionsResponseDTO = MutableLiveData<EventQuestionsResponseDTO>(null)
    var questionListItem = ArrayList<QuestionListItem>()
    var questionNumberList = ArrayList<Int>()
    var eventSelectedAnswerMap = HashMap<Int, ArrayList<String>>()
    var startQuestionsBtnVisibility = MutableLiveData<Boolean>(false)
    var provideSummaryTxtVisibility = MutableLiveData<Boolean>(false)
    var editImageVisibility = MutableLiveData<Boolean>(false)

    @Inject
    lateinit var sharedPrefsHelper: SharedPrefsHelper

    init {
        MyApplication.applicationComponent?.inject(this)
        // Update CurrencyType ArrayList
        mileList = MyApplication.appContext.resources.getStringArray(R.array.mile_distance)
            .toList() as ArrayList<String>
        setCurrencyType()
    }

    private fun setCurrencyType() {
        val currencyTypeList =
            MyApplication.appContext.resources.getStringArray(R.array.currency_type)
                .toList() as ArrayList<String>
        when (currencyTypeList[sharedPrefsHelper[SharedPrefConstant.CURRENCY_TYPE, 0]]) {
            MyApplication.appContext.resources.getString(R.string.USD) -> {
                estimatedBudgetSymbol.value = "$"
            }
            MyApplication.appContext.resources.getString(R.string.GBP) -> {
                estimatedBudgetSymbol.value = "£"
            }
            MyApplication.appContext.resources.getString(R.string.INR) -> {
                estimatedBudgetSymbol.value = "₹"
            }
        }
    }

    fun getServiceSlots() {
        val observable: Observable<ServiceSlotsDTO> = retrofitHelper.getServiceRepository()
            .getServiceSlots(
                getUserToken(),
                sharedPrefsHelper[SharedPrefConstant.SERVICE_CATEGORY_ID, "0"].toInt(),
                serviceDate.value!!
            )
        this.observable.value = observable as Observable<ResponseDTO>
        doNetworkOperation()
    }

    fun getBudgetCalcInfo(estimatedBudget: String) {
        val observable: Observable<BudgetCalcInfoDTO> =
            retrofitHelper.getServiceRepository().getBudgetCalcInfo(
                getUserToken(),
                sharedPrefsHelper[SharedPrefConstant.EVENT_ID, 0],
                sharedPrefsHelper[SharedPrefConstant.EVENT_SERVICE_DESCRIPTION_ID, "0"].toLong(),
                estimatedBudget
            )
        this.observable.value = observable as Observable<ResponseDTO>
        doNetworkOperation()
    }

    fun putBudgetCalcInfo() {
        val observable: Observable<BudgetCalcResDTO> =
            retrofitHelper.getServiceRepository()
                .putBudgetCalcInfo(
                    getUserToken(),
                    sharedPrefsHelper[SharedPrefConstant.EVENT_ID, 0],
                    updateTotalAmount.toString()
                )
        this.observable.value = observable as Observable<ResponseDTO>
        doNetworkOperation()
    }

    fun getServiceDetailQuestions() {
        val observable: Observable<EventQuestionsResponseDTO> =
            retrofitHelper.getServiceRepository()
                .getServiceDetailQuestions(
                    getUserToken(),
                    sharedPrefsHelper[SharedPrefConstant.EVENT_SERVICE_ID, "0"].toInt()
                )
        this.observable.value = observable as Observable<ResponseDTO>
        doNetworkOperation()
    }

    private fun postServiceDescription(serviceInfo: ServiceInfoDTO) {
        val observable: Observable<EventInfoResponseDto> =
            retrofitHelper.getServiceRepository()
                .postServiceDescription(getUserToken(), serviceInfo)
        this.observable.value = observable as Observable<ResponseDTO>
        doNetworkOperation()
    }

    private fun putServiceDescription(serviceInfo: ServiceInfoDTO) {
        val observable: Observable<EventInfoResponseDto> =
            retrofitHelper.getServiceRepository()
                .putServiceDescription(getUserToken(), serviceInfo)
        this.observable.value = observable as Observable<ResponseDTO>
        doNetworkOperation()
    }

    fun getServiceDescription() {
        val observable: Observable<GetServiceDetailsDTO> =
            retrofitHelper.getServiceRepository().getServiceDescription(
                getUserToken(),
                sharedPrefsHelper[SharedPrefConstant.EVENT_SERVICE_DESCRIPTION_ID, "0"].toLong()
            )
        this.observable.value = observable as Observable<ResponseDTO>
        doNetworkOperation()
    }

    override fun onSuccess(responseDTO: ResponseDTO) {
        super.onSuccess(responseDTO)
        when (responseDTO) {
            is ServiceSlotsDTO -> {
                timeSlotList.value = responseDTO.data as ArrayList<String>
            }
            is BudgetCalcInfoDTO -> {
                budgetCalcInfo.value = responseDTO
            }
            is BudgetCalcResDTO -> {
                // Get updated budget values
                estimatedBudget.value?.let {
                    if (it.isNotEmpty()) {
                        getBudgetCalcInfo(it)
                    }
                }
            }
            is EventQuestionsResponseDTO -> {
                // Avoid to clear eventSelectedAnswerMap value in modify details page
                if (getClearEventSelectedAnswerMapValue()) {
                    eventSelectedAnswerMap.clear()
                }
                eventQuestionsResponseDTO.value = responseDTO
            }
            is EventInfoResponseDto -> {
                // Delete specific service details from shared preference
                removeServiceSharedPreference()
                callBackInterface?.onSaveClick()
            }
            is GetServiceDetailsDTO -> {
                getModifyServiceDetails(responseDTO)
                // Get Initial Questions API call
                updateQuestions(true)
                // Clear EventSelectedAnswerMap
                clearEventSelectedAnswerMap(false)
                // Get Initial time slots based on event date
                updateServiceDate(true)
                // Do budget Api call
                setDoBudgetAPICall(true)
            }
        }
    }

    private fun getModifyServiceDetails(responseDTO: GetServiceDetailsDTO) {
        serviceDate.value =
            responseDTO.data.eventServiceDescriptionDto.eventServiceDateDto.serviceDate
        selectedSlotsList =
            responseDTO.data.eventServiceDescriptionDto.eventServiceDateDto.preferredSlots as ArrayList<String>
        estimatedBudget.value =
            responseDTO.data.eventServiceDescriptionDto.eventServiceBudgetDto.estimatedBudget
        zipCode.value =
            responseDTO.data.eventServiceDescriptionDto.eventServiceVenueDto.zipCode
        milePosition.value =
            mileList.indexOf(responseDTO.data.eventServiceDescriptionDto.eventServiceVenueDto.redius)
        responseDTO.data.questionnaireWrapperDto.questionnaireDtos.forEach {
            if (it.questionMetadata.answer.isNullOrEmpty().not()) {
                val answerList = ArrayList<String>()
                if (it.questionMetadata.answer != null &&
                    it.questionMetadata.answer.contains(",")
                ) {
                    it.questionMetadata.answer.split(",").forEach { answer ->
                        answerList.add(answer)
                    }
                } else {
                    answerList.add(it.questionMetadata.answer ?: "")
                }
                eventSelectedAnswerMap[responseDTO.data.questionnaireWrapperDto
                    .questionnaireDtos.indexOf(it)] = answerList
            }
        }
    }

    fun onClickQuestionsBtn() {
        // Update entered values to shared preference
        setServiceSharedPreference()
        callBackInterface?.onClickQuestionsBtn(AppConstant.QUESTION_BUTTON)
    }

    fun onClickQuesEditBtn() {
        // Update entered values to shared preference
        setServiceSharedPreference()
        callBackInterface?.onClickQuestionsBtn(AppConstant.EDIT_BUTTON)
    }

    fun onClickSaveBtn() {
        // Verify all mandatory questions answered before submit
        if (verifyMandatoryQuesAnswered(questionListItem, eventSelectedAnswerMap)) {
            if (userDetailsValidation(AppConstant.ON_SAVE)) {
                if (sharedPrefsHelper[SharedPrefConstant.EVENT_SERVICE_DESCRIPTION_ID, "0"] == "0") {
                    postServiceDescription(createServiceInfoDto())
                } else {
                    putServiceDescription(createServiceInfoDto())
                }
            } else {
                showError()
            }
        } else {
            showToastMessage(MyApplication.appContext.resources.getString(R.string.please_answer_all_mandatory_questions))
        }
    }

    fun userDetailsValidation(from: String): Boolean {
        return (serviceDate.value.isNullOrEmpty().not() &&
                serviceDate.value?.let { leadPeriodVerification(it) } == true &&
                if (timeSlotList.value?.isNotEmpty() == true) {
                    selectedSlotsList.isEmpty().not()
                } else {
                    // For highlight Save button clickable
                    from == AppConstant.ON_SAVE
                }
                && estimatedBudget.value.isNullOrEmpty().not() &&
                zipCode.value.isNullOrEmpty().not())
    }

    private fun setServiceSharedPreference() {
        sharedPrefsHelper.put(SharedPrefConstant.SERVICE_DATE, serviceDate.value?.trim() ?: "")
        sharedPrefsHelper.putArrayList(
            SharedPrefConstant.TIME_SLOT_LIST,
            timeSlotList.value ?: ArrayList<String>()
        )
        sharedPrefsHelper.putArrayList(SharedPrefConstant.SELECTED_TIME_SLOTS, selectedSlotsList)
        sharedPrefsHelper.put(
            SharedPrefConstant.ESTIMATED_BUDGET,
            estimatedBudget.value?.trim() ?: ""
        )
        sharedPrefsHelper.put(SharedPrefConstant.TOTAL_AMOUNT, totalAmount.value?.trim() ?: "")
        sharedPrefsHelper.put(
            SharedPrefConstant.REMAINING_AMOUNT,
            remainingAmount.value?.trim() ?: ""
        )
        sharedPrefsHelper.put(SharedPrefConstant.SERVICE_ZIPCODE, zipCode.value?.trim() ?: "")
        sharedPrefsHelper.put(SharedPrefConstant.SERVICE_MILES, milePosition.value ?: 0)
        sharedPrefsHelper.putArrayList(SharedPrefConstant.QUESTION_LIST_ITEM, questionListItem)
        sharedPrefsHelper.putArrayList(SharedPrefConstant.QUESTION_NUMBER_LIST, questionNumberList)
        sharedPrefsHelper.putHashMap(
            SharedPrefConstant.SERVICE_SELECTED_ANSWER_MAP, eventSelectedAnswerMap
        )
        sharedPrefsHelper.putObject(
            SharedPrefConstant.SERVICE_QUESTIONS_RES_DTO, eventQuestionsResponseDTO.value
        )
    }

    fun removeServiceSharedPreference() {
        sharedPrefsHelper.remove(SharedPrefConstant.SERVICE_NAME)
        sharedPrefsHelper.remove(SharedPrefConstant.SERVICE_DATE)
        sharedPrefsHelper.remove(SharedPrefConstant.TIME_SLOT_LIST)
        sharedPrefsHelper.remove(SharedPrefConstant.SELECTED_TIME_SLOTS)
        sharedPrefsHelper.remove(SharedPrefConstant.ESTIMATED_BUDGET)
        sharedPrefsHelper.remove(SharedPrefConstant.TOTAL_AMOUNT)
        sharedPrefsHelper.remove(SharedPrefConstant.REMAINING_AMOUNT)
        sharedPrefsHelper.remove(SharedPrefConstant.SERVICE_ZIPCODE)
        sharedPrefsHelper.remove(SharedPrefConstant.SERVICE_MILES)
        sharedPrefsHelper.remove(SharedPrefConstant.QUESTION_LIST_ITEM)
        sharedPrefsHelper.remove(SharedPrefConstant.QUESTION_NUMBER_LIST)
        sharedPrefsHelper.remove(SharedPrefConstant.SERVICE_SELECTED_ANSWER_MAP)
        sharedPrefsHelper.remove(SharedPrefConstant.SERVICE_QUESTIONS_RES_DTO)
    }

    fun verifyMandatoryQuesAnswered(
        questionListItem: ArrayList<QuestionListItem>,
        eventSelectedAnswerMap: HashMap<Int, ArrayList<String>>
    ): Boolean {
        return if (questionListItem.isNotEmpty()) {
            val mandatoryQuesIndexList = ArrayList<Int>().apply {
                questionListItem.filter { it.isMandatory }.forEach {
                    add(questionListItem.indexOf(it))
                }
            }
            // Verify all mandatory questions are answered
            eventSelectedAnswerMap.keys.containsAll(mandatoryQuesIndexList)
        } else {
            true
        }
    }

    private fun showError() {
        if (serviceDate.value.isNullOrEmpty()) {
            setServiceDateErrorText(MyApplication.appContext.resources.getString(R.string.please_select_valid_date))
            showServiceDateError()
        }
        if (timeSlotList.value?.isNotEmpty() == true) {
            if (selectedSlotsList.isEmpty()) {
                showTimeSlotError()
            }
        }
        if (estimatedBudget.value.isNullOrEmpty()) {
            showAmountErrorText(MyApplication.appContext.resources.getString(R.string.please_enter_the_valid_))
        }
        if (zipCode.value.isNullOrEmpty()) {
            showZipCodeError()
        }
    }

    fun updateServiceDate(value: Boolean) {
        updateServiceDate.value = value
    }

    fun updateQuestions(value: Boolean) {
        updateQuestions.value = value
    }

    fun getUpdateQuestionsValue(): Boolean = updateQuestions.value ?: false

    fun clearEventSelectedAnswerMap(value: Boolean) {
        clearEventSelectedAnswerMap.value = value
    }

    private fun getClearEventSelectedAnswerMapValue(): Boolean =
        clearEventSelectedAnswerMap.value ?: false

    fun showAmountErrorText(message: String) {
        // Set error text
        amountErrorMessage.value = message
        amountErrorVisibility.value = true
    }

    fun hideAmountErrorText() {
        amountErrorVisibility.value = false
    }

    fun setDoBudgetAPICall(value: Boolean) {
        doBudgetAPICall.value = value
    }

    fun showRemainingAmountLayout() {
        remainingAmountVisibility.value = true
    }

    fun hideRemainingAmountLayout() {
        remainingAmountVisibility.value = false
    }

    fun setNullToEstimatedBudget() {
        estimatedBudget.value = null
    }

    fun setNullToBudgetCalcInfo() {
        budgetCalcInfo.value = null
    }

    fun setTotalAmount(amount: String) {
        totalAmount.value = amount
    }

    fun setRemainingAmount(amount: String) {
        remainingAmount.value = amount
    }

    fun showStartQuestionsBtn() {
        startQuestionsBtnVisibility.value = true
        provideSummaryTxtVisibility.value = true
    }

    fun hideStartQuestionsBtn() {
        startQuestionsBtnVisibility.value = false
        provideSummaryTxtVisibility.value = false
    }

    fun setServiceDateErrorText(message: String) {
        serviceDateErrorText.value = message
    }

    fun showServiceDateError() {
        serviceDateErrorVisibility.value = true
    }

    fun hideServiceDateError() {
        serviceDateErrorVisibility.value = false
    }

    private fun showTimeSlotError() {
        timeSlotErrorVisibility.value = true
    }

    fun hideTimeSlotError() {
        timeSlotErrorVisibility.value = false
    }

    private fun showZipCodeError() {
        zipCodeErrorVisibility.value = true
    }

    fun hideZipCodeError() {
        zipCodeErrorVisibility.value = false
    }

    fun showTimeSlot() {
        timeSlotTitleVisibility.value = true
        timeSlotVisibility.value = true
    }

    fun hideTimeSlot() {
        timeSlotTitleVisibility.value = false
        timeSlotVisibility.value = false
    }

    fun setServiceName() {
        serviceName.value =
            MyApplication.appContext.resources.getString(R.string.provide) +
                    " ${
                        sharedPrefsHelper[SharedPrefConstant.SERVICE_NAME, ""]
                            .replaceFirstChar(Char::lowercase)
                    } " +
                    MyApplication.appContext.resources.getString(R.string.details)
    }

    // Lead period verification
    private fun leadPeriodVerification(dateString: String): Boolean {
        val formatter = DateTimeFormatter.ofPattern(AppConstant.DATE_FORMAT)
        val date = LocalDate.parse(dateString, formatter)
        val leadPeriod = sharedPrefsHelper[SharedPrefConstant.LEAD_PERIOD, "0"].toLong()
        return LocalDate.now().plusDays(leadPeriod) < date
    }

    fun updateLeadPeriodVerification(dateString: String) {
        if (leadPeriodVerification(dateString)) {
            hideServiceDateError()
        } else {
            val message =
                "${MyApplication.appContext.resources.getString(R.string.sorry_you_cannot_add_this_as_service_date_it_needs_at_least_)} " +
                        "${sharedPrefsHelper[SharedPrefConstant.LEAD_PERIOD, "0"]} " +
                        MyApplication.appContext.resources.getString(R.string.days_lead_period_from_current_date)
            setServiceDateErrorText(message)
            showServiceDateError()
        }
    }

    private fun createServiceInfoDto(): ServiceInfoDTO {
        val eventServiceDescriptionDto = createEventServiceDescriptionDto()
        val eventServiceId = sharedPrefsHelper[SharedPrefConstant.EVENT_SERVICE_ID, "0"].toInt()
        val questionnaireWrapperDto = QuestionnaireWrapperDto(
            eventQuestionsResponseDTO.value?.data?.noOfEventOrganizers ?: 0,
            eventQuestionsResponseDTO.value?.data?.noOfVendors ?: 0,
            createQuestionnaireDtoService()
        )
        return ServiceInfoDTO(
            eventServiceDescriptionDto, eventServiceId, questionnaireWrapperDto,
            sharedPrefsHelper[SharedPrefConstant.EVENT_SERVICE_DESCRIPTION_ID, "0"].toLong(),
            sharedPrefsHelper[SharedPrefConstant.EVENT_SERVICE_STATUS, ""]
        )
    }

    private fun createEventServiceDescriptionDto(): EventServiceDescriptionDto {
        val eventServiceBudgetDto =
            EventServiceBudgetDto(
                estimatedBudgetSymbol.value ?: "$",
                estimatedBudget.value?.toBigDecimal() ?: "0".toBigDecimal(),
                null
            )
        val eventServiceDateDto = createEventServiceDateDto()
        val eventServiceVenueDto =
            EventServiceVenueDto(mileList[milePosition.value ?: 0], zipCode.value!!)
        return EventServiceDescriptionDto(
            eventServiceBudgetDto,
            eventServiceDateDto,
            eventServiceVenueDto
        )
    }

    private fun createEventServiceDateDto(): EventServiceDateDto {
        return EventServiceDateDto(
            null, sharedPrefsHelper[SharedPrefConstant.LEAD_PERIOD, "0"].toInt(),
            selectedSlotsList,
            serviceDate.value!!,
            serviceName.value!!
        )
    }

    private fun createQuestionnaireDtoService(): List<QuestionnaireDtoService> {
        val questionnaireDtoServiceList = ArrayList<QuestionnaireDtoService>()
        eventQuestionsResponseDTO.value?.data?.questionnaireDtos?.forEach {
            val qusNumber = eventQuestionsResponseDTO.value!!.data.questionnaireDtos.indexOf(it)
            val answer = eventSelectedAnswerMap[qusNumber]
            questionnaireDtoServiceList.add(
                QuestionnaireDtoService(
                    it.active,
                    answer ?: ArrayList(),
                    it.eventTemplateId,
                    it.id,
                    "Q${qusNumber + 1} - ${it.questionMetadata.question}",
                    createQuestionMetadataService(qusNumber, it),
                    sharedPrefsHelper[SharedPrefConstant.SERVICE_CATEGORY_ID, "0"].toInt()
                )
            )

        }
        return questionnaireDtoServiceList
    }

    private fun createQuestionMetadataService(
        qusNumber: Int,
        questionnaireDto: QuestionnaireDto
    ): QuestionMetadataService {
        val answer = eventSelectedAnswerMap[qusNumber]
        return QuestionMetadataService(
            answer?.joinToString() ?: "",
            questionnaireDto.questionMetadata.choices,
            questionnaireDto.questionMetadata.eventOrganizer,
            questionnaireDto.questionMetadata.filter,
            questionnaireDto.questionMetadata.isMandatory,
            questionnaireDto.questionMetadata.question,
            questionnaireDto.questionMetadata.questionType,
            questionnaireDto.questionMetadata.vendor
        )
    }

    // Values update from question activity
    fun updateEnteredValues() {
        serviceDate.value = sharedPrefsHelper[SharedPrefConstant.SERVICE_DATE, ""]
        // Lead period verification
        updateLeadPeriodVerification(serviceDate.value!!)
        timeSlotList.value =
            sharedPrefsHelper.getArrayList(SharedPrefConstant.TIME_SLOT_LIST) as ArrayList<String>
        selectedSlotsList =
            sharedPrefsHelper.getArrayList(SharedPrefConstant.SELECTED_TIME_SLOTS) as ArrayList<String>
        zipCode.value = sharedPrefsHelper[SharedPrefConstant.SERVICE_ZIPCODE, ""]
        estimatedBudget.value = sharedPrefsHelper[SharedPrefConstant.ESTIMATED_BUDGET, ""]
        totalAmount.value = sharedPrefsHelper[SharedPrefConstant.TOTAL_AMOUNT, ""]
        remainingAmount.value = sharedPrefsHelper[SharedPrefConstant.REMAINING_AMOUNT, ""]
        // show total and remaining amount to UI
        if (estimatedBudget.value.isNullOrEmpty()) {
            hideRemainingAmountLayout()
        } else {
            showRemainingAmountLayout()
        }
        milePosition.value = sharedPrefsHelper[SharedPrefConstant.SERVICE_MILES, 0]
        // Question data
        questionListItem =
            sharedPrefsHelper.getQuestionsList(SharedPrefConstant.QUESTION_LIST_ITEM)
        questionNumberList =
            sharedPrefsHelper.getArrayIntList(SharedPrefConstant.QUESTION_NUMBER_LIST)
        // Update selected questions answers
        eventSelectedAnswerMap =
            sharedPrefsHelper.getHashMap(SharedPrefConstant.SERVICE_SELECTED_ANSWER_MAP) as HashMap<Int, ArrayList<String>>
        eventQuestionsResponseDTO.value =
            sharedPrefsHelper.getObject(SharedPrefConstant.SERVICE_QUESTIONS_RES_DTO)
        // Update questions button visibility
        if (questionListItem.isNotEmpty()) {
            showStartQuestionsBtn()
            editButtonVisibility()
        }
    }

    fun getIntentDetails(intent: Intent) {
        sharedPrefsHelper.apply {
            this.put(
                SharedPrefConstant.SERVICE_NAME,
                intent.getStringExtra(AppConstant.SERVICE_NAME) ?: ""
            )
            this.put(
                SharedPrefConstant.EVENT_SERVICE_ID,
                intent.getStringExtra(AppConstant.EVENT_SERVICE_ID) ?: "0"
            )
            this.put(
                SharedPrefConstant.SERVICE_CATEGORY_ID,
                intent.getStringExtra(AppConstant.SERVICE_CATEGORY_ID) ?: "0"
            )
            this.put(
                SharedPrefConstant.EVENT_SERVICE_DESCRIPTION_ID,
                intent.getStringExtra(AppConstant.EVENT_SERVICE_DESCRIPTION_ID) ?: "0"
            )
            this.put(
                SharedPrefConstant.EVENT_SERVICE_STATUS,
                intent.getStringExtra(AppConstant.EVENT_SERVICE_STATUS) ?: ""
            )
            this.put(
                SharedPrefConstant.LEAD_PERIOD,
                intent.getStringExtra(AppConstant.LEAD_PERIOD) ?: "0"
            )
        }
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

    private var callBackInterface: CallBackInterface? = null

    // Initializing CallBack Interface Method
    fun setCallBackInterface(callback: CallBackInterface) {
        callBackInterface = callback
    }

    // CallBack Interface
    interface CallBackInterface {
        fun onSaveClick()
        fun onClickQuestionsBtn(from: String)
    }
}