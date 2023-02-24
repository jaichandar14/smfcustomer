package com.smf.customer.view.provideservicedetails

import androidx.lifecycle.MutableLiveData
import com.smf.customer.R
import com.smf.customer.app.base.BaseViewModel
import com.smf.customer.app.base.MyApplication
import com.smf.customer.di.sharedpreference.SharedPrefsHelper
import javax.inject.Inject

class ProvideServiceViewModel : BaseViewModel() {
    var mileList = ArrayList<String>()
    var eventDate = MutableLiveData<String>()
    var estimatedBudget = MutableLiveData<String>("")
    var estimatedBudgetSymbol = MutableLiveData<String>("$")
    var totalAmount = MutableLiveData<String>("")
    var remainingAmount = MutableLiveData<String>("")
    var zipCode = MutableLiveData<String>("")
    var milePosition = MutableLiveData<Int>(0)

    // Start question btn text
    var questionBtnText =
        MutableLiveData(MyApplication.appContext.resources.getString(R.string.start_questions))

    // Edit image icon visibility
    var editImageVisibility = MutableLiveData<Boolean>(false)

    // Variables for error message visibility
    var eventDateErrorVisibility = MutableLiveData<Boolean>(false)
    var amountErrorVisibility = MutableLiveData<Boolean>(false)
    var zipCodeErrorVisibility = MutableLiveData<Boolean>(false)

    // Avoid screen rotation api call
    var screenRotationStatus = MutableLiveData<Boolean>(false)
    var selectedSlotsPositionMap = HashMap<Int, Boolean>()

    @Inject
    lateinit var sharedPrefsHelper: SharedPrefsHelper

    init {
        MyApplication.applicationComponent?.inject(this)
        // Update CurrencyType ArrayList
        mileList = MyApplication.appContext.resources.getStringArray(R.array.mile_distance)
            .toList() as ArrayList<String>
    }

    fun onClickCancelBtn() {
        callBackInterface?.onCancelClick()
    }

    fun onClickSaveBtn() {
        if (!zipCode.value.isNullOrEmpty() && !estimatedBudget.value.isNullOrEmpty() &&
            eventDateErrorVisibility.value == false
        ) {
            callBackInterface?.onSaveClick()
        } else {
            showError()
        }
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

    fun setScreenRotationValueTrue() {
        screenRotationStatus.value = true
    }

    fun setScreenRotationValueFalse() {
        screenRotationStatus.value = false
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
    }
}