package com.smf.customer.view.provideservicedetails

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.smf.customer.R
import com.smf.customer.app.base.BaseViewModel
import com.smf.customer.app.base.MyApplication
import com.smf.customer.app.constant.AppConstant
import com.smf.customer.data.model.dto.QuestionListItem
import com.smf.customer.data.model.request.*
import com.smf.customer.data.model.response.*
import com.smf.customer.data.model.response.QuestionnaireDto
import com.smf.customer.di.sharedpreference.SharedPrefConstant
import com.smf.customer.di.sharedpreference.SharedPrefsHelper
import io.reactivex.Observable
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class ProvideServiceViewModel : BaseViewModel() {
    var mileList = ArrayList<String>()
    var milePosition = MutableLiveData(0)
    var serviceDate = MutableLiveData<String>()
    var serviceDateErrorText = MutableLiveData("")
    var estimatedBudget = MutableLiveData<String>(null)
    var estimatedBudgetSymbol = MutableLiveData<String>("$")
    var totalAmount = MutableLiveData<String>("")
    var remainingAmount = MutableLiveData<String>("")
    var zipCode = MutableLiveData<String>("")
    var amountErrorMessage =
        MutableLiveData(MyApplication.appContext.resources.getString(R.string.estimated_budget_required))

    // Start question btn text
    var questionBtnText =
        MutableLiveData(MyApplication.appContext.resources.getString(R.string.start_questions))

    // Edit image icon visibility
    var remainingAmountVisibility = MutableLiveData<Boolean>(false)

    // Variables for error message visibility
    var serviceDateErrorVisibility = MutableLiveData<Boolean>(false)
    var timeSlotErrorVisibility = MutableLiveData<Boolean>(false)
    var amountErrorVisibility = MutableLiveData<Boolean>(false)
    var zipCodeErrorVisibility = MutableLiveData<Boolean>(false)

    var timeSlotList = MutableLiveData<ArrayList<String>>()
    var selectedSlotsPositionMap = HashMap<Int, Boolean>()
    var doBudgetAPICall = MutableLiveData<Boolean>(false)
    var budgetCalcInfo = MutableLiveData<BudgetCalcInfoDTO>(null)

    // Questions
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
                    estimatedBudget.value!!
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
                eventQuestionsResponseDTO.value = responseDTO
            }
            is EventInfoResponseDto -> {
                Log.d(TAG, "onSuccess: post success called $responseDTO")
                // Delete specific service details from shared preference
                removeServiceSharedPreference()
                callBackInterface?.onSaveClick()
            }
        }
    }

    fun onClickQuestionsBtn() {
        // Update entered values to shared preference
        setServiceSharedPreference()
        callBackInterface?.onClickQuestionsBtn(AppConstant.QUESTION_BUTTON)
    }

    fun onClickQuesEditBtn() {
        callBackInterface?.onClickQuestionsBtn(AppConstant.EDIT_BUTTON)
    }

    fun onClickCancelBtn() {
        callBackInterface?.onCancelClick()
    }

    fun onClickSaveBtn() {
        // Verify all mandatory questions answered before submit
        if (verifyMandatoryQuesAnswered(questionListItem, eventSelectedAnswerMap)) {
            if (userDetailsValidation()) {
                postServiceDescription(createServiceInfoDto())
            } else {
                showError()
            }
        } else {
            showToastMessage(MyApplication.appContext.resources.getString(R.string.please_answer_all_mandatory_questions))
        }
    }

    fun userDetailsValidation(): Boolean {
        return (serviceDate.value.isNullOrEmpty().not() &&
                serviceDate.value?.let { leadPeriodVerification(it) } == true &&
                selectedSlotsPositionMap.isEmpty().not()
                && estimatedBudget.value.isNullOrEmpty().not() &&
                zipCode.value.isNullOrEmpty().not())
    }

    private fun setServiceSharedPreference() {
        sharedPrefsHelper.put(SharedPrefConstant.SERVICE_DATE, serviceDate.value?.trim() ?: "")
        sharedPrefsHelper.putHashMap(
            SharedPrefConstant.SELECTED_SLOT_POSITION_MAP,
            selectedSlotsPositionMap
        )
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
        sharedPrefsHelper.putHashMap(
            SharedPrefConstant.SERVICE_SELECTED_ANSWER_MAP, eventSelectedAnswerMap
        )
    }

    private fun removeServiceSharedPreference() {
        sharedPrefsHelper.remove(SharedPrefConstant.SERVICE_DATE)
        sharedPrefsHelper.remove(SharedPrefConstant.SELECTED_SLOT_POSITION_MAP)
        sharedPrefsHelper.remove(SharedPrefConstant.ESTIMATED_BUDGET)
        sharedPrefsHelper.remove(SharedPrefConstant.TOTAL_AMOUNT)
        sharedPrefsHelper.remove(SharedPrefConstant.REMAINING_AMOUNT)
        sharedPrefsHelper.remove(SharedPrefConstant.SERVICE_ZIPCODE)
        sharedPrefsHelper.remove(SharedPrefConstant.SERVICE_MILES)
        sharedPrefsHelper.remove(SharedPrefConstant.SERVICE_SELECTED_ANSWER_MAP)
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
        if (selectedSlotsPositionMap.isEmpty()) {
            showTimeSlotError()
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

    // Lead period verification
    fun leadPeriodVerification(dateString: String): Boolean {
        val formatter = DateTimeFormatter.ofPattern(AppConstant.DATE_FORMAT)
        val date = LocalDate.parse(dateString, formatter)
        val leadPeriod = sharedPrefsHelper[SharedPrefConstant.LEAD_PERIOD, "0"].toLong()
        return LocalDate.now().plusDays(leadPeriod) < date
    }

    private fun createServiceInfoDto(): ServiceInfoDTO {
        val eventServiceDescriptionDto = createEventServiceDescriptionDto()
        val eventServiceId = sharedPrefsHelper[SharedPrefConstant.EVENT_SERVICE_ID, "0"].toInt()
        val questionnaireWrapperDto = QuestionnaireWrapperDto(
            eventQuestionsResponseDTO.value?.data?.noOfEventOrganizers ?: 0,
            eventQuestionsResponseDTO.value?.data?.noOfVendors ?: 0,
            createQuestionnaireDtoService()
        )
        return ServiceInfoDTO(eventServiceDescriptionDto, eventServiceId, questionnaireWrapperDto)
    }

    private fun createEventServiceDescriptionDto(): EventServiceDescriptionDto {
        val eventServiceBudgetDto =
            EventServiceBudgetDto(
                estimatedBudgetSymbol.value ?: "$",
                estimatedBudget.value?.toBigDecimal() ?: "0".toBigDecimal()
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
        val preferredSlots = ArrayList<String>()
        selectedSlotsPositionMap.keys.forEach {
            if (selectedSlotsPositionMap[it] == true) {
                timeSlotList.value?.let { it1 -> preferredSlots.add(it1[it]) }
            }
        }
        return EventServiceDateDto(
            null, sharedPrefsHelper[SharedPrefConstant.LEAD_PERIOD, "0"].toInt(),
            preferredSlots,
            serviceDate.value!!
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

    private var callBackInterface: CallBackInterface? = null

    // Initializing CallBack Interface Method
    fun setCallBackInterface(callback: CallBackInterface) {
        callBackInterface = callback
    }

    // CallBack Interface
    interface CallBackInterface {
        fun onSaveClick()
        fun onCancelClick()
        fun onClickQuestionsBtn(from: String)
    }
}