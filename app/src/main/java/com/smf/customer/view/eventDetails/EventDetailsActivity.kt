package com.smf.customer.view.eventDetails

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.datepicker.MaterialDatePicker
import com.smf.customer.R
import com.smf.customer.app.base.BaseActivity
import com.smf.customer.app.base.MyApplication
import com.smf.customer.app.constant.AppConstant
import com.smf.customer.data.model.dto.QuestionListItem
import com.smf.customer.databinding.EventDetailsBinding
import com.smf.customer.dialog.EventQuestionsDialog
import com.smf.customer.listener.DialogThreeButtonListener
import com.smf.customer.utility.DatePicker
import com.smf.customer.utility.Util
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class EventDetailsActivity : BaseActivity<EventDetailsViewModel>(), DialogThreeButtonListener {
    lateinit var binding: EventDetailsBinding
    private var picker: MaterialDatePicker<Long> = DatePicker.newInstance
    var currentCountryName = ""

    companion object {
        var selectedAnswerPositionMap = HashMap<Int, Int>()
        val questionListItem = ArrayList<QuestionListItem>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@EventDetailsActivity, R.layout.event_details)
        viewModel = ViewModelProvider(this)[EventDetailsViewModel::class.java]
        binding.eventDetailsViewModel = viewModel
        binding.lifecycleOwner = this@EventDetailsActivity
        MyApplication.applicationComponent.inject(this)
        currentCountryName = binding.cppSignIn.defaultCountryName
        init()

//        var local = (getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager).simCountryIso
//        val locale = Locale.getDefault()
//        locale.country
//        Log.d(TAG, "onCreate: $local")
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        // Verify event question dialog isVisible
        if (viewModel.isEventQuestionDialogVisible()) {
            showEventQuestionDialog()
        }
        // Verify date picker dialog isVisible
        if (viewModel.isDatePickerDialogVisible()) {
            showDatePickerDialog()
        }
    }

    private fun init() {
        // Initialize currency type
        initializeCurrencyType()
        // Get country and state list
        getCountryAndStateList()
        // Initialize country and state list
        initializeCountryAndStateSpinner()
        // Venue details observer
        venueDetailsObserver()
        // Setting User Details
        setUserDetails()
        // Error details observer
        errorDetailsObserver()
        // Event date edittext Listener
        eventDateListener()
        // Start question button listener
        onClickStartQuestionBtn()
    }

    private fun onClickStartQuestionBtn() {
        binding.startQusBtn.setOnClickListener {
            val questions = ArrayList<String>()
            questions.add("Want cake?")
            questions.add("Want drink?")

            val choiceList = ArrayList<String>()
            choiceList.add("Yes")
            choiceList.add("No")

            val choice = ArrayList<ArrayList<String>>()
            choice.add(choiceList)
            choice.add(choiceList)

            Log.d(TAG, "onClickStartQuestionBtn: $questions $choice")
            questionListItem.clear()
            questionListItem.add(
                QuestionListItem("want cake?", choiceList)
            )
            questionListItem.add(
                QuestionListItem("want drink?", choiceList)
            )
            questionListItem.add(
                QuestionListItem("want decoration?", choiceList)
            )
            showEventQuestionDialog()
        }
    }

    private fun showEventQuestionDialog() {
        // set dialog flag true
        viewModel.showEventQuestionDialogFlag()
        EventQuestionsDialog.newInstance(questionListItem, this)
            .show(supportFragmentManager, AppConstant.EVENT_QUESTIONS_DIALOG)
    }

    override fun onPositiveClick(dialogFragment: DialogFragment) {
        super.onPositiveClick(dialogFragment)
        when {
            dialogFragment.tag.equals(AppConstant.EVENT_QUESTIONS_DIALOG) -> {

            }
        }
    }

    override fun onNegativeClick(dialogFragment: DialogFragment) {
        super.onNegativeClick(dialogFragment)
        when {
            dialogFragment.tag.equals(AppConstant.EVENT_QUESTIONS_DIALOG) -> {

            }
        }
    }

    override fun onCancelClick(dialogFragment: DialogFragment) {
        Log.d(TAG, "eventQuestionDialogLiveData: called onCancelClick")
        viewModel.hideEventQuestionDialogFlag()
    }

    override fun onDialogDismissed() {
        Log.d(TAG, "eventQuestionDialogLiveData: called onDialogDismissed")
        super.onDialogDismissed()
//        viewModel.hideEventQuestionDialogFlag()
    }

    private fun initializeCurrencyType() {
        val arrayAdapterCurrency =
            ArrayAdapter(
                applicationContext,
                R.layout.spinner_text_view,
                viewModel.currencyTypeList
            )
        binding.currencyType.adapter = arrayAdapterCurrency
    }

    private fun venueDetailsObserver() {
        viewModel.iKnowVenue.observe(this, Observer {
            viewModel.venueVisibility(it)
        })
    }

    private fun initializeCountryAndStateSpinner() {
        Log.d(TAG, "init: ${viewModel.countryList}")
        Log.d(TAG, "init: stateList ${viewModel.stateList}")
        val arrayAdapterCountry =
            ArrayAdapter(applicationContext, R.layout.spinner_text_view, viewModel.countryList)
        binding.country.adapter = arrayAdapterCountry

        val arrayAdapterStates =
            ArrayAdapter(applicationContext, R.layout.spinner_text_view, viewModel.stateList[0])
        binding.state.adapter = arrayAdapterStates

        viewModel.countryPosition.observe(this, Observer { position ->
            updateStateBaseOnCountry(position)
        })

    }

    private fun updateStateBaseOnCountry(position: Int) {
        val stateListForSpinner = ArrayList<String>()
        viewModel.stateList[position].forEach {
            stateListForSpinner.add(it)
        }
        val arrayAdapterStatesSpinner =
            ArrayAdapter(
                applicationContext,
                R.layout.spinner_text_view,
                stateListForSpinner
            )
        binding.state.adapter = arrayAdapterStatesSpinner
    }

    private fun getCountryAndStateList() {
        val obj = JSONObject(
            Util.getCountriesAndStatesList(
                this,
                "countriesAndStateList.json"
            )
        )
        val jsonArray = obj.getJSONArray(AppConstant.COUNTRIES)
        viewModel.countryList.add(getString(R.string.Select_your_country))
        viewModel.stateList.add(listOf(getString(R.string.Select_your_state)))
        for (i in 0 until jsonArray.length() - 1) {
            val jsonObject = jsonArray[i] as JSONObject
            viewModel.countryList.add(jsonObject.getString(AppConstant.COUNTRY))
            val stateArray = jsonObject.getJSONArray(AppConstant.STATES)
            val localStateList = ArrayList<String>()
            for (j in 0 until stateArray.length() - 1) {
                localStateList.add(stateArray[j] as String)
            }
            viewModel.stateList.add(localStateList)
        }
    }

    private fun eventDateListener() {
        binding.eventDate.setOnClickListener {
            if (!picker.isVisible) {
                showDatePickerDialog()
            }
        }
        binding.calendarImage.setOnClickListener {
            if (!picker.isVisible) {
                showDatePickerDialog()
            }
        }
    }

    private fun showDatePickerDialog() {
        viewModel.showDatePickerDialogFlag()
        val myFormat = AppConstant.DATE_FORMAT
        val sdf = SimpleDateFormat(myFormat)
        picker.addOnPositiveButtonClickListener {
            val date = Date(it)
            val formattedDate = sdf.format(date) // date selected by the user
            viewModel.eventDate.value = formattedDate
        }
        // show picker using this
        picker.show(supportFragmentManager, AppConstant.MATERIAL_DATE_PICKER)
    }

    private fun errorDetailsObserver() {
        viewModel.eventName.observe(this, Observer {
            viewModel.eventNameError.value = false
        })
        viewModel.eventDate.observe(this, Observer {
            viewModel.eventDateError.value = false
        })
        viewModel.noOfAttendees.observe(this, Observer {
            viewModel.noOfAttendeesError.value = false
        })
        viewModel.totalBudget.observe(this, Observer {
            viewModel.totalBudgetError.value = false
        })
        viewModel.address1.observe(this, Observer {
            viewModel.address1Error.value = false
        })
        viewModel.address2.observe(this, Observer {
            viewModel.address2Error.value = false
        })
        viewModel.countryPosition.observe(this, Observer {
            viewModel.countryPositionError.value = false
        })
        viewModel.statePosition.observe(this, Observer {
            viewModel.statePositionError.value = false
        })
        viewModel.city.observe(this, Observer {
            viewModel.cityError.value = false
        })
        viewModel.zipCode.observe(this, Observer {
            viewModel.zipCodeError.value = false
        })
        viewModel.name.observe(this, Observer {
            viewModel.nameError.value = false
        })
        viewModel.mobileNumber.observe(this, Observer {
            viewModel.mobileNumberError.value = false
        })
        viewModel.emailId.observe(this, Observer {
            viewModel.emailIdError.value = false
        })
    }

    private fun setUserDetails() {
        viewModel.name.value = "vigneshwaran"
        viewModel.mobileNumber.value = "8667636458"
        viewModel.emailId.value = "vigneshwaran@gmail.com"
        binding.cppSignIn.setCountryForPhoneCode(+91)
    }

    override fun onPause() {
        // Save date picker dialog flag
        if (!picker.isVisible) {
            viewModel.hideDatePickerDialogFlag()
        }
        super.onPause()
        if (picker.isVisible) {
            // Date picker dialog state loss
            picker.dismissAllowingStateLoss()
        }
    }

}