package com.smf.customer.view.eventDetails

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.datepicker.MaterialDatePicker
import com.smf.customer.R
import com.smf.customer.app.base.BaseActivity
import com.smf.customer.app.base.MyApplication
import com.smf.customer.app.constant.AppConstant
import com.smf.customer.data.model.dto.EventDetailsDTO
import com.smf.customer.data.model.response.EventQuestionsResponseDTO
import com.smf.customer.databinding.EventDetailsBinding
import com.smf.customer.di.sharedpreference.SharedPrefConstant
import com.smf.customer.di.sharedpreference.SharedPrefsHelper
import com.smf.customer.utility.DatePicker
import com.smf.customer.utility.Util
import com.smf.customer.utility.Util.Companion.parcelable
import com.smf.customer.view.dashboard.DashBoardActivity
import com.smf.customer.view.questions.QuestionsActivity
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class EventDetailsActivity : BaseActivity<EventDetailsViewModel>(),
    EventDetailsViewModel.CallBackInterface {
    lateinit var binding: EventDetailsBinding
    private var picker: MaterialDatePicker<Long> = DatePicker.newInstance
    var currentCountryName = ""

    @Inject
    lateinit var sharedPrefsHelper: SharedPrefsHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@EventDetailsActivity, R.layout.event_details)
        viewModel = ViewModelProvider(this)[EventDetailsViewModel::class.java]
        binding.eventDetailsViewModel = viewModel
        binding.lifecycleOwner = this@EventDetailsActivity
        MyApplication.applicationComponent?.inject(this)
        currentCountryName = binding.cppSignIn.defaultCountryName
        viewModel.setCallBackInterface(this)

        init()
    }

    private fun init() {
        // Setting user details on UI
        setUserDetails()
        // Get country and state list
        getCountryAndStateList()
        // Initialize country and state list
        initializeCountryAndStateSpinner()
        // Venue details observer
        venueDetailsObserver()
        // Restrict number inside editText
        setEditTextFilter()
        // Error details observer
        errorDetailsObserver()
        // Event date edittext Listener
        eventDateListener()
        // Initialize currency type
        initializeCurrencyType()
    }

    override fun updateQuestions(eventQuestionsResponseDTO: EventQuestionsResponseDTO) {
        // Update eventQuestionsResponse data to view model variable
        viewModel.eventQuestionsResponseDTO = eventQuestionsResponseDTO
        viewModel.questionListItem.clear()
        viewModel.questionNumberList.clear()
        if (viewModel.eventQuestionsResponseDTO?.data?.questionnaireDtos?.isEmpty() == true) {
            viewModel.hideStartQuestionsBtn()
        } else {
            viewModel.showStartQuestionsBtn()
            viewModel.editButtonVisibility()
            // Update Questions
            viewModel.questionListItem = viewModel.getQuesListItem(eventQuestionsResponseDTO.data)
            // Update Question Number
            viewModel.questionNumberList =
                viewModel.getQuestionNumberList(viewModel.questionListItem.size)
        }
    }

    override fun onClickNext() {
        val intent = Intent(this, DashBoardActivity::class.java).apply {
            this.putExtra(AppConstant.ON_EVENT, AppConstant.ON_EVENT)
            this.putExtra(AppConstant.EVENT_ID, viewModel.eventId)
            this.putExtra(AppConstant.EVENT_NAME, viewModel.eventName.value)
            this.putExtra(AppConstant.EVENT_DATE, viewModel.eventDate.value)
        }
        finish()
        startActivity(intent)
    }

    override fun onClickQuestionsBtn(from: String) {
        val intent = Intent(this, QuestionsActivity::class.java).apply {
            this.putExtra(AppConstant.FROM_ACTIVITY, AppConstant.EVENT_DETAILS_ACTIVITY)
            this.putExtra(
                AppConstant.EVENT_DATA, viewModel.getUserEnteredValuesForQuesPage()
            )
            this.putExtra(
                AppConstant.QUESTION_LIST_ITEM,
                viewModel.questionListItem
            )
            this.putIntegerArrayListExtra(
                AppConstant.QUESTION_NUMBER_LIST, viewModel.questionNumberList
            )
            this.putExtra(
                AppConstant.SELECTED_ANSWER_MAP,
                viewModel.eventSelectedAnswerMap
            )
            this.putExtra(
                AppConstant.QUESTION_BTN_TEXT,
                if (from != AppConstant.EDIT_BUTTON) viewModel.questionBtnText.value else ""
            )
        }
        finish()
        startActivity(intent)
    }

    private fun initializeCurrencyType() {
        val arrayAdapterCurrency = ArrayAdapter(
            applicationContext, R.layout.currency_spinner_text_view, viewModel.currencyTypeList
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
        // Update Selected country Position
        binding.country.setSelection(viewModel.selectedCountryPosition)
        binding.country.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                // Update text color based on country selection
                if (viewModel.countryList[position] == getString(R.string.select_your_country)) {
                    (parent?.getChildAt(0) as TextView).setTextColor(getColor(R.color.gray_text))
                } else {
                    (parent?.getChildAt(0) as TextView).setTextColor(getColor(R.color.black))
                }
                // Update selected country position
                viewModel.selectedCountryPosition = position
                // Update country error visibility
                viewModel.countryPositionError.value = false
                // Update state based on selected country
                updateStateBaseOnCountry(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun updateStateBaseOnCountry(countryPosition: Int) {
        val stateListForSpinner = ArrayList<String>()
        viewModel.stateList[countryPosition].forEach {
            stateListForSpinner.add(it)
        }
        val arrayAdapterStatesSpinner = ArrayAdapter(
            applicationContext, R.layout.spinner_text_view, stateListForSpinner
        )
        binding.state.adapter = arrayAdapterStatesSpinner
        Log.d(TAG, "updateStateBaseOnCountry: state ${viewModel.selectedStatePosition}")
        // Update Selected state Position
        binding.state.setSelection(viewModel.selectedStatePosition)
        binding.state.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                // Update text color based on state selection
                if (viewModel.stateList[countryPosition][position] == getString(R.string.select_your_state)) {
                    (parent?.getChildAt(0) as TextView).setTextColor(getColor(R.color.gray_text))
                } else {
                    (parent?.getChildAt(0) as TextView).setTextColor(getColor(R.color.black))
                }
                // Update selected state position
                viewModel.selectedStatePosition = position
                // Update state error visibility
                viewModel.statePositionError.value = false
                Log.d(TAG, "updateStateBaseOnCountry: onItem ${viewModel.selectedStatePosition}")
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun getCountryAndStateList() {
        val obj = JSONObject(
            Util.getCountriesAndStatesList(
                this, "countriesAndStateList.json"
            )
        )
        val jsonArray = obj.getJSONArray(AppConstant.COUNTRIES)
        viewModel.countryList.add(getString(R.string.select_your_country))
        viewModel.stateList.add(listOf(getString(R.string.select_your_state)))
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
            if (it.isNullOrEmpty().not()) {
                viewModel.totalBudgetError.value = !Util.amountValidation(it)
            }
        })
        viewModel.address1.observe(this, Observer {
            viewModel.address1Error.value = false
        })
        viewModel.address2.observe(this, Observer {
            viewModel.address2Error.value = false
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

    override fun onDestroy() {
        super.onDestroy()
        // Update screen rotation status
        viewModel.screenRotationStatus.value = true
    }

    private fun setEditTextFilter() {
        binding.city.filters = arrayOf(Util.filterText())
    }

    // Setting user details to the UI
    private fun setUserDetails() {
        if (intent.extras?.getString(AppConstant.EVENT_QUESTIONS) == AppConstant.EVENT_QUESTIONS) {
            // Get and update user details before entered
            intent.parcelable<EventDetailsDTO>(AppConstant.EVENT_DATA)?.let { eventDetailsDTO ->
                eventDetailsDTO.apply {
                    eventTitle = viewModel.setEventTitle(eventDetailsDTO.eventTitle)
                }
                viewModel.updateDetailsToUI(eventDetailsDTO)
            }
        } else if (intent.extras?.getString(AppConstant.EVENT_DASH_BOARD) == AppConstant.EVENT_DASH_BOARD) {
//            TODO Need to get and pass dynamic event Id
            // Get api call for modify event details
            viewModel.getEventInfo(sharedPrefsHelper[SharedPrefConstant.EVENT_ID, 0])
        } else {
            val eventDetailsDTO = EventDetailsDTO(
                eventTitle = viewModel.setEventTitle(
                    intent.getStringExtra(AppConstant.TITLE).toString()
                ),
                templateId = intent.getStringExtra(AppConstant.TEMPLATE_ID)?.toInt() ?: 0,
                eventId = 0,
                name = sharedPrefsHelper[SharedPrefConstant.FIRST_NAME, ""] + " " + sharedPrefsHelper[SharedPrefConstant.LAST_NAME, ""],
                emailId = sharedPrefsHelper[SharedPrefConstant.EMAIL_ID, ""],
                mobileNumberWithCountryCode = sharedPrefsHelper[SharedPrefConstant.MOBILE_NUMBER_WITH_COUNTRY_CODE, ""]
            )
            viewModel.updateDetailsToUI(eventDetailsDTO)
            // Event Questions Api call
            viewModel.templateId.let { viewModel.getEventDetailsQuestions(it) }
        }
    }

    override fun updateCountryCode(code: Int) {
        binding.cppSignIn.setCountryForPhoneCode(code)
    }

    override fun updateCountryAndState(selectedCountryPosition: Int, selectedStatePosition: Int) {
        binding.country.setSelection(selectedCountryPosition)
        binding.state.setSelection(selectedStatePosition)
    }

}