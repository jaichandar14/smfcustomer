package com.smf.customer.view.provideservicedetails

import androidx.lifecycle.MutableLiveData
import com.smf.customer.R
import com.smf.customer.app.base.BaseViewModel
import com.smf.customer.app.base.MyApplication
import com.smf.customer.app.constant.AppConstant
import com.smf.customer.data.model.dto.QuestionListItem
import com.smf.customer.data.model.dto.ServiceDetailsDTO
import com.smf.customer.data.model.request.*
import com.smf.customer.data.model.request.EventServiceBudgetDto
import com.smf.customer.data.model.request.EventServiceDateDto
import com.smf.customer.data.model.request.EventServiceDescriptionDto
import com.smf.customer.data.model.request.EventServiceVenueDto
import com.smf.customer.data.model.request.QuestionnaireWrapperDto
import com.smf.customer.data.model.response.*
import com.smf.customer.di.sharedpreference.SharedPrefConstant
import com.smf.customer.di.sharedpreference.SharedPrefsHelper
import io.reactivex.Observable
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject

class ProvideServiceViewModel : BaseViewModel() {
    var serviceName = MutableLiveData<String>()
    var mileList = ArrayList<String>()
    var milePosition = MutableLiveData(0)
    var serviceDate = MutableLiveData<String>()
    var serviceDateErrorText = MutableLiveData("")
    var estimatedBudget = MutableLiveData<String>("")
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
    var eventQuestionsResponseDTO: EventQuestionsResponseDTO? = null
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
                serviceCategoryId,
                serviceDate.value ?: ""
            )
        this.observable.value = observable as Observable<ResponseDTO>
        doNetworkOperation()
    }

    fun getBudgetCalcInfo(estimatedBudget: String) {
        val observable: Observable<BudgetCalcInfoDTO> =
            retrofitHelper.getServiceRepository().getBudgetCalcInfo(
                getUserToken(),
                eventId,
                eventServiceDescriptionId,
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
                    eventId,
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
                    eventServiceId
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
            is ServiceSlotsDTO -> {
                callBackInterface?.updateTimeSlots(responseDTO.data as ArrayList<String>, true)
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
                callBackInterface?.updateQuestions(responseDTO)
            }
            is EventInfoResponseDto -> {
                callBackInterface?.onSaveClick()
            }
            is GetServiceDetailsDTO -> {
                // Update service details
                setModifyDetails(responseDTO)
            }
        }
    }

    private fun setModifyDetails(responseDTO: GetServiceDetailsDTO) {
        val serviceDetailsDTO = ServiceDetailsDTO(
            eventId = eventId,
            serviceName = responseDTO.data.eventServiceDescriptionDto.eventServiceDateDto.serviceName,
            eventServiceId = eventServiceId,
            serviceCategoryId = serviceCategoryId,
            eventServiceDescriptionId = eventServiceDescriptionId,
            eventServiceStatus = eventServiceStatus,
            leadPeriod = leadPeriod,
            serviceDate = responseDTO.data.eventServiceDescriptionDto.eventServiceDateDto.serviceDate,
            zipCode = responseDTO.data.eventServiceDescriptionDto.eventServiceVenueDto.zipCode,
            selectedSlotsList = responseDTO.data.eventServiceDescriptionDto.eventServiceDateDto.preferredSlots as ArrayList<String>,
            estimatedBudget = responseDTO.data.eventServiceDescriptionDto.eventServiceBudgetDto.estimatedBudget,
            milePosition = mileList.indexOf(responseDTO.data.eventServiceDescriptionDto.eventServiceVenueDto.redius),
            eventSelectedAnswerMap = getEventSelectedAnswerMap(responseDTO)
        )
        updateDetailsToUI(serviceDetailsDTO)
        // Get time slots
        getServiceSlots()
        // Do budget Api call
        setDoBudgetAPICall(true)
    }

    private fun getEventSelectedAnswerMap(responseDTO: GetServiceDetailsDTO): HashMap<Int, ArrayList<String>> {
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
        return eventSelectedAnswerMap
    }

    fun onClickQuestionsBtn() {
        callBackInterface?.onClickQuestionsBtn(AppConstant.QUESTION_BUTTON)
    }

    fun onClickQuesEditBtn() {
        callBackInterface?.onClickQuestionsBtn(AppConstant.EDIT_BUTTON)
    }

    fun onClickSaveBtn() {
        // Verify all mandatory questions answered before submit
        if (verifyMandatoryQuesAnswered(questionListItem, eventSelectedAnswerMap)) {
            if (userDetailsValidation(AppConstant.ON_SAVE)) {
                if (eventServiceDescriptionId.toString() == "0") {
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
                getAmountErrorVisibility().not() &&
                zipCode.value.isNullOrEmpty().not())
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

    fun showAmountErrorText(message: String) {
        // Set error text
        amountErrorMessage.value = message
        amountErrorVisibility.value = true
    }

    fun hideAmountErrorText() {
        amountErrorVisibility.value = false
    }

    private fun getAmountErrorVisibility(): Boolean = amountErrorVisibility.value!!

    fun setDoBudgetAPICall(value: Boolean) {
        doBudgetAPICall.value = value
    }

    fun showRemainingAmountLayout() {
        remainingAmountVisibility.value = true
    }

    fun hideRemainingAmountLayout() {
        remainingAmountVisibility.value = false
    }

    fun setEmptyToEstimatedBudget() {
        estimatedBudget.value = ""
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

    fun clearSelectedSlotsAndAnswers() {
        selectedSlotsList.clear()
        eventSelectedAnswerMap.clear()
    }

    private fun setServiceName(name: String) {
        serviceName.value =
            MyApplication.appContext.resources.getString(R.string.provide) +
                    " ${name.lowercase(Locale.getDefault())} " +
                    MyApplication.appContext.resources.getString(R.string.details)
    }

    // Lead period verification
    private fun leadPeriodVerification(dateString: String): Boolean {
        val formatter = DateTimeFormatter.ofPattern(AppConstant.DATE_FORMAT)
        val date = LocalDate.parse(dateString, formatter)
        return LocalDate.now().plusDays(leadPeriod) < date
    }

    fun updateLeadPeriodVerification(dateString: String) {
        if (leadPeriodVerification(dateString)) {
            hideServiceDateError()
        } else {
            val message =
                "${MyApplication.appContext.resources.getString(R.string.sorry_you_cannot_add_this_as_service_date_it_needs_at_least_)} " +
                        "$leadPeriod " +
                        MyApplication.appContext.resources.getString(R.string.days_lead_period_from_current_date)
            setServiceDateErrorText(message)
            showServiceDateError()
        }
    }

    private fun createServiceInfoDto(): ServiceInfoDTO {
        val eventServiceDescriptionDto = createEventServiceDescriptionDto()
        val questionnaireWrapperDto = QuestionnaireWrapperDto(
            eventQuestionsResponseDTO?.data?.noOfEventOrganizers ?: 0,
            eventQuestionsResponseDTO?.data?.noOfVendors ?: 0,
            createQuestionnaireDtoService()
        )
        return ServiceInfoDTO(
            eventServiceDescriptionDto, eventServiceId, questionnaireWrapperDto,
            eventServiceDescriptionId, eventServiceStatus
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
            null, leadPeriod.toInt(),
            selectedSlotsList,
            serviceDate.value ?: "",
            serviceName.value?.removePrefix(MyApplication.appContext.resources.getString(R.string.provide))
                ?.removeSuffix(MyApplication.appContext.resources.getString(R.string.details))
                ?.trim()
                ?: ""
        )
    }

    private fun createQuestionnaireDtoService(): List<QuestionnaireDtoService> {
        val questionnaireDtoServiceList = ArrayList<QuestionnaireDtoService>()
        eventQuestionsResponseDTO?.data?.questionnaireDtos?.forEach {
            val qusNumber = eventQuestionsResponseDTO!!.data.questionnaireDtos.indexOf(it)
            val answer = eventSelectedAnswerMap[qusNumber]
            questionnaireDtoServiceList.add(
                QuestionnaireDtoService(
                    it.active,
                    answer ?: ArrayList(),
                    it.eventTemplateId,
                    it.id,
                    "Q${qusNumber + 1} - ${it.questionMetadata.question}",
                    createQuestionMetadataService(qusNumber, it),
                    serviceCategoryId
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

    fun getQuesListItem(
        eventQuestionMetaDataDto: com.smf.customer.data.model.response.QuestionnaireWrapperDto
    ): ArrayList<QuestionListItem> {
        return ArrayList<QuestionListItem>().apply {
            eventQuestionMetaDataDto.questionnaireDtos.forEach { questionnaireDto ->
                this.add(
                    QuestionListItem(
                        questionnaireDto.questionMetadata.question,
                        questionnaireDto.questionMetadata.choices as ArrayList<String>,
                        questionnaireDto.questionMetadata.questionType,
                        questionnaireDto.questionMetadata.isMandatory
                    )
                )
            }
        }
    }

    fun getQuestionNumberList(questionListItemSize: Int): ArrayList<Int> {
        return ArrayList<Int>().apply {
            for (questionNumber in 0 until questionListItemSize) {
                this.add(questionNumber)
            }
        }
    }

    var eventId = 0
    var eventServiceId = 0
    var serviceCategoryId = 0
    var eventServiceDescriptionId: Long = 0
    var eventServiceStatus = ""
    var leadPeriod: Long = 0

    fun updateDetailsToUI(serviceDetailsDTO: ServiceDetailsDTO) {
        eventId = serviceDetailsDTO.eventId
        setServiceName(serviceDetailsDTO.serviceName)
        eventServiceId = serviceDetailsDTO.eventServiceId
        serviceCategoryId = serviceDetailsDTO.serviceCategoryId
        eventServiceDescriptionId = serviceDetailsDTO.eventServiceDescriptionId
        eventServiceStatus = serviceDetailsDTO.eventServiceStatus
        leadPeriod = serviceDetailsDTO.leadPeriod
        serviceDate.value = serviceDetailsDTO.serviceDate
        // Lead period verification
        updateLeadPeriodVerification(serviceDate.value ?: "")
        zipCode.value = serviceDetailsDTO.zipCode
        timeSlotList.value = serviceDetailsDTO.timeSlotList
        selectedSlotsList = serviceDetailsDTO.selectedSlotsList
        estimatedBudget.value = serviceDetailsDTO.estimatedBudget
        estimatedBudgetSymbol.value = serviceDetailsDTO.estimatedBudgetSymbol
        totalAmount.value = serviceDetailsDTO.totalAmount
        remainingAmount.value = serviceDetailsDTO.remainingAmount
        // show total and remaining amount to UI
        if (totalAmount.value.isNullOrEmpty() && remainingAmount.value.isNullOrEmpty()) {
            hideRemainingAmountLayout()
        } else {
            showRemainingAmountLayout()
        }
        milePosition.value = serviceDetailsDTO.milePosition
        // Question data
        questionListItem = serviceDetailsDTO.questionListItem
        questionNumberList = serviceDetailsDTO.questionNumberList
        // Update selected questions answers
        eventSelectedAnswerMap = serviceDetailsDTO.eventSelectedAnswerMap
        eventQuestionsResponseDTO = serviceDetailsDTO.eventQuestionsResponseDTO
        // Update questions button text
        if (questionListItem.isNotEmpty()) {
            showStartQuestionsBtn()
            editButtonVisibility()
        }
    }

    fun getUserEnteredValuesForQuesPage(): ServiceDetailsDTO {
        return getServiceDetailsDTO(
            eventId,
            serviceName.value?.removePrefix(MyApplication.appContext.resources.getString(R.string.provide))
                ?.removeSuffix(MyApplication.appContext.resources.getString(R.string.details))
                ?.trim()
                ?: "",
            eventServiceId,
            serviceCategoryId,
            eventServiceDescriptionId,
            eventServiceStatus,
            leadPeriod,
            serviceDate.value ?: "",
            zipCode.value ?: "",
            timeSlotList.value ?: ArrayList(),
            selectedSlotsList,
            estimatedBudget.value ?: "",
            estimatedBudgetSymbol.value ?: "$",
            totalAmount.value ?: "",
            remainingAmount.value ?: "",
            milePosition.value ?: 0,
            questionListItem,
            questionNumberList,
            eventSelectedAnswerMap,
            eventQuestionsResponseDTO
        )
    }

    fun getServiceDetailsDTO(
        eventId: Int,
        serviceName: String,
        eventServiceId: Int,
        serviceCategoryId: Int,
        eventServiceDescriptionId: Long,
        eventServiceStatus: String,
        leadPeriod: Long,
        serviceDate: String,
        zipCode: String,
        timeSlotList: ArrayList<String>,
        selectedSlotsList: ArrayList<String>,
        estimatedBudget: String,
        estimatedBudgetSymbol: String,
        totalAmount: String,
        remainingAmount: String,
        milePosition: Int,
        questionListItem: ArrayList<QuestionListItem>,
        questionNumberList: ArrayList<Int>,
        eventSelectedAnswerMap: HashMap<Int, ArrayList<String>>,
        eventQuestionsResponseDTO: EventQuestionsResponseDTO?
    ): ServiceDetailsDTO {
        return ServiceDetailsDTO(
            eventId,
            serviceName,
            eventServiceId,
            serviceCategoryId,
            eventServiceDescriptionId,
            eventServiceStatus,
            leadPeriod,
            serviceDate,
            zipCode,
            timeSlotList,
            selectedSlotsList,
            estimatedBudget,
            estimatedBudgetSymbol,
            totalAmount,
            remainingAmount,
            milePosition,
            questionListItem,
            questionNumberList,
            eventSelectedAnswerMap,
            eventQuestionsResponseDTO
        )
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
        fun updateTimeSlots(timeSlotList: ArrayList<String>, getQuestions: Boolean)
        fun updateQuestions(eventQuestionsResponseDTO: EventQuestionsResponseDTO)
        fun onSaveClick()
        fun onClickQuestionsBtn(from: String)
    }
}