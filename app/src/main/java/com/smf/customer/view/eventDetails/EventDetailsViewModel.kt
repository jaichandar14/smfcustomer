package com.smf.customer.view.eventDetails

import androidx.lifecycle.MutableLiveData
import com.smf.customer.R
import com.smf.customer.app.base.BaseViewModel
import com.smf.customer.app.base.MyApplication

class EventDetailsViewModel : BaseViewModel() {
    var countryList: ArrayList<String> = ArrayList()
    var stateList: ArrayList<List<String>> = ArrayList()
    var currencyTypeList = ArrayList<String>()
    var iKnowVenue = MutableLiveData<Boolean>()
    var iKnowVenueLayoutVisibility = MutableLiveData<Boolean>()

    var currencyPosition = MutableLiveData<Int>()
    var eventName = MutableLiveData<String>()
    var eventDate = MutableLiveData<String>()
    var noOfAttendees = MutableLiveData<String>()
    var totalBudget = MutableLiveData<String>()
    var address1 = MutableLiveData<String>()
    var address2 = MutableLiveData<String>()
    var countryPosition = MutableLiveData<Int>()
    var statePosition = MutableLiveData<Int>()
    var city = MutableLiveData<String>()
    var zipCode = MutableLiveData<String>()
    var name = MutableLiveData<String>()
    var mobileNumber = MutableLiveData<String>()
    var emailId = MutableLiveData<String>()

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

    init {
        MyApplication.applicationComponent.inject(this)
        // Update CurrencyType ArrayList
        currencyTypeList = MyApplication.appContext.resources.getStringArray(R.array.currency_type)
            .toList() as ArrayList<String>
        iKnowVenue.value = true
    }

    fun venueVisibility(value: Boolean) {
        iKnowVenueLayoutVisibility.value = value
    }

    fun onClickNextButton() {
        if (iKnowVenue.value != true) {
            if (!eventName.value.isNullOrEmpty() && !eventDate.value.isNullOrEmpty() &&
                !noOfAttendees.value.isNullOrEmpty() && !totalBudget.value.isNullOrEmpty() &&
                !zipCode.value.isNullOrEmpty() && !name.value.isNullOrEmpty() &&
                !mobileNumber.value.isNullOrEmpty() &&
                !emailId.value.isNullOrEmpty()
            ) {
//                TODO Move to next screen
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
//             TODO Move to next screen
            } else {
                // Set error
                setIWillBeSelectingErrorVisible()
                setIKnowVenueErrorVisible()
            }
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

}