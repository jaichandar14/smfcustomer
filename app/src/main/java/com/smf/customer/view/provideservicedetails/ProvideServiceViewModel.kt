package com.smf.customer.view.provideservicedetails

import androidx.lifecycle.MutableLiveData
import com.smf.customer.R
import com.smf.customer.app.base.BaseViewModel
import com.smf.customer.app.base.MyApplication
import com.smf.customer.app.constant.AppConstant
import com.smf.customer.data.model.dto.QuestionListItem
import com.smf.customer.data.model.response.*
import com.smf.customer.di.sharedpreference.SharedPrefConstant
import com.smf.customer.di.sharedpreference.SharedPrefsHelper
import io.reactivex.Observable
import javax.inject.Inject

class ProvideServiceViewModel : BaseViewModel() {
    var mileList = ArrayList<String>()
    var milePosition = MutableLiveData(0)
    var serviceDate = MutableLiveData<String>()
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
    var eventDateErrorVisibility = MutableLiveData<Boolean>(false)
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
    }

    fun getServiceSlots() {
        val observable: Observable<ServiceSlotsDTO> = retrofitHelper.getServiceRepository()
            .getServiceSlots(getUserToken(), 289, "3/31/2023")
        this.observable.value = observable as Observable<ResponseDTO>
        doNetworkOperation()
    }

    fun getBudgetCalcInfo(amount: String) {
        val observable: Observable<BudgetCalcInfoDTO> =
            retrofitHelper.getServiceRepository().getBudgetCalcInfo(getUserToken(), amount)
        this.observable.value = observable as Observable<ResponseDTO>
        doNetworkOperation()
    }

    fun putBudgetCalcInfo() {
        val observable: Observable<BudgetCalcResDTO> =
            retrofitHelper.getServiceRepository()
                .putBudgetCalcInfo(getUserToken(), estimatedBudget.value!!)
        this.observable.value = observable as Observable<ResponseDTO>
        doNetworkOperation()
    }

    fun getServiceDetailQuestions() {
        val observable: Observable<EventQuestionsResponseDTO> =
            retrofitHelper.getServiceRepository()
                .getServiceDetailQuestions(getUserToken(), 3724)
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
//        if (!zipCode.value.isNullOrEmpty() && !estimatedBudget.value.isNullOrEmpty() &&
//            eventDateErrorVisibility.value == false
//        ) {
//            callBackInterface?.onSaveClick()
//        } else {
//            showError()
//        }
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
    }

    private fun showError() {
        if (zipCode.value.isNullOrEmpty()) {
            zipCodeErrorVisibility.value = true
        }
        if (estimatedBudget.value.isNullOrEmpty()) {
            amountErrorVisibility.value = true
        }
        if (eventDateErrorVisibility.value == true) {
            showToastMessage(
                MyApplication.appContext
                    .resources.getString(R.string.please_select_valid_date)
            )
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