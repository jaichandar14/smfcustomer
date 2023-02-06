package com.smf.customer.view.eventDetails

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.datepicker.MaterialDatePicker
import com.smf.customer.R
import com.smf.customer.app.base.BaseActivity
import com.smf.customer.app.base.MyApplication
import com.smf.customer.app.constant.AppConstant
import com.smf.customer.data.model.dto.QuestionListItem
import com.smf.customer.data.model.response.EventQuestionsResponseDTO
import com.smf.customer.databinding.EventDetailsBinding
import com.smf.customer.di.sharedpreference.SharedPrefConstant
import com.smf.customer.di.sharedpreference.SharedPrefsHelper
import com.smf.customer.utility.DatePicker
import com.smf.customer.utility.Util
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
    var token = ""

    @Inject
    lateinit var sharedPrefsHelper: SharedPrefsHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@EventDetailsActivity, R.layout.event_details)
        viewModel = ViewModelProvider(this)[EventDetailsViewModel::class.java]
        binding.eventDetailsViewModel = viewModel
        binding.lifecycleOwner = this@EventDetailsActivity
        MyApplication.applicationComponent.inject(this)
        currentCountryName = binding.cppSignIn.defaultCountryName
        viewModel.setCallBackInterface(this)

        init()
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        // Verify date picker dialog isVisible
        if (viewModel.isDatePickerDialogVisible()) {
            showDatePickerDialog()
        }
        // Check screen rotating
        if (viewModel.screenRotationStatus.value == false) {
            // Event Questions Api call
            viewModel.templateId?.let { viewModel.getEventDetailsQuestions(token, it) }
            // Update viewOrderQuestionNumber when activity not rotation
            viewModel.viewOrderQuestionNumber = 0
        } else {
            // Set start button visibility when rotating screen
            if (viewModel.questionListItem.isEmpty()) {
                startQuestionsButtonVisibility(false)
            }
        }
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
        if (eventQuestionsResponseDTO.data.questionnaireDtos.isEmpty()) {
            viewModel.questionListItem.clear()
            startQuestionsButtonVisibility(false)
        } else {
            startQuestionsButtonVisibility(true)
            // Update Questions
            updateQuestionsList(eventQuestionsResponseDTO)
            // Update Question Number
            updateQuestionNumberList()
        }
    }

    private fun updateQuestionsList(eventQuestionsResponseDTO: EventQuestionsResponseDTO) {
        viewModel.questionListItem.clear()
        eventQuestionsResponseDTO.data.questionnaireDtos.forEach {
            viewModel.questionListItem.add(
                QuestionListItem(
                    it.questionMetadata.question,
                    it.questionMetadata.choices as ArrayList<String>,
                    it.questionMetadata.questionType,
                    it.questionMetadata.isMandatory
                )
            )
        }
    }

    private fun updateQuestionNumberList() {
        viewModel.questionNumberList.clear()
        for (i in 0 until viewModel.questionListItem.size) {
            viewModel.questionNumberList.add(i)
        }
        Log.d(
            TAG,
            "updateListData: ${viewModel.questionListItem.size} ${viewModel.questionNumberList}"
        )
    }

    override fun onClickNext() {
        val intent = Intent(this, DashBoardActivity::class.java)
        intent.putExtra(AppConstant.ON_EVENT, getString(R.string.event_dt))
        startActivity(intent)
    }

    override fun onClickQuestionsBtn() {
        // Go to questions page
        navigateQuestionsPage()
    }

    private fun navigateQuestionsPage() {
        val intent = Intent(this, QuestionsActivity::class.java)
        intent.putExtra(AppConstant.FROM_ACTIVITY, this::class.java.simpleName)
        intent.putExtra(AppConstant.QUESTION_LIST_ITEM, viewModel.questionListItem)
        intent.putIntegerArrayListExtra(
            AppConstant.QUESTION_NUMBER_LIST, viewModel.questionNumberList
        )
        intent.putExtra(AppConstant.SELECTED_ANSWER_MAP, viewModel.eventSelectedAnswerMap)
        finish()
        startActivity(intent)
    }

    private fun initializeCurrencyType() {
        val arrayAdapterCurrency = ArrayAdapter(
            applicationContext, R.layout.spinner_text_view, viewModel.currencyTypeList
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
            viewModel.totalBudgetError.value = false
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
        Log.d(TAG, "called onDestroy: called")
    }

    private fun startQuestionsButtonVisibility(status: Boolean) {
        if (status) {
            binding.startQusBtn.visibility = View.VISIBLE
            binding.provideEventDetailsText.visibility = View.VISIBLE
            binding.hostDetailsSeparator.visibility = View.VISIBLE
        } else {
            binding.startQusBtn.visibility = View.GONE
            binding.provideEventDetailsText.visibility = View.GONE
            binding.hostDetailsSeparator.visibility = View.GONE
        }
    }

    private fun setEditTextFilter() {
//        binding.eventName.filters = arrayOf(Util.filterTextWithSpace())
        binding.city.filters = arrayOf(Util.filterText())
    }

    // Setting user details to the UI
    private fun setUserDetails() {
        if (intent.extras?.get(AppConstant.EVENT_DASH_BOARD) == AppConstant.EVENT_DASH_BOARD ||
            intent.extras?.get(AppConstant.EVENT_QUESTIONS) == AppConstant.EVENT_QUESTIONS
        ) {
            // Set sharedPref details
            updateOldValues()
            // Set Initial details
            setInitialUserDetails()
        } else {
            // Update eventTitle and TemplateId
            updateSharedPrefValues()
            // Set Initial details
            setInitialUserDetails()
        }
    }

    private fun updateSharedPrefValues() {
        sharedPrefsHelper.put(
            SharedPrefConstant.EVENT_TITLE,
            intent.getStringExtra(AppConstant.TITLE) + " " + getString(R.string.event_Details)
        )
        intent.getStringExtra(AppConstant.TEMPLATE_ID)?.let {
            Log.d(TAG, "updateSharedPrefValues: $it")
            sharedPrefsHelper.put(SharedPrefConstant.TEMPLATE_ID, it.toInt())
        }
    }

    private fun setInitialUserDetails() {
        binding.pageTitleText.text = sharedPrefsHelper[SharedPrefConstant.EVENT_TITLE, ""]
        viewModel.templateId = sharedPrefsHelper[SharedPrefConstant.TEMPLATE_ID, 0]
        token = sharedPrefsHelper[SharedPrefConstant.ACCESS_TOKEN, ""]
        viewModel.name.value =
            sharedPrefsHelper[SharedPrefConstant.FIRST_NAME, ""] + " " + sharedPrefsHelper[SharedPrefConstant.LAST_NAME, ""]
        viewModel.emailId.value = sharedPrefsHelper[SharedPrefConstant.EMAIL_ID, ""]
        val countryCode = sharedPrefsHelper[SharedPrefConstant.COUNTRY_CODE, ""]
        val mobileNumber = sharedPrefsHelper[SharedPrefConstant.MOBILE_NUMBER, ""]
        viewModel.mobileNumber.value = mobileNumber.substringAfter(countryCode)
        binding.cppSignIn.setCountryForPhoneCode(sharedPrefsHelper[SharedPrefConstant.COUNTRY_CODE, 0])
    }

    private fun updateOldValues() {
        viewModel.eventName.value = sharedPrefsHelper[SharedPrefConstant.EVENT_NAME, ""]
        viewModel.eventDate.value = sharedPrefsHelper[SharedPrefConstant.EVENT_DATE, ""]
        viewModel.noOfAttendees.value = sharedPrefsHelper[SharedPrefConstant.NO_OF_ATTENDEES, ""]
        viewModel.currencyPosition.value = sharedPrefsHelper[SharedPrefConstant.CURRENCY_TYPE, 0]
        viewModel.totalBudget.value = sharedPrefsHelper[SharedPrefConstant.BUDGET, ""]
        viewModel.iKnowVenue2.value = sharedPrefsHelper[SharedPrefConstant.VENUE, false] == false
        viewModel.address1.value = sharedPrefsHelper[SharedPrefConstant.ADDRESS_1, ""]
        viewModel.address2.value = sharedPrefsHelper[SharedPrefConstant.ADDRESS_2, ""]
        viewModel.selectedCountryPosition = sharedPrefsHelper[SharedPrefConstant.COUNTRY, 0]
        viewModel.selectedStatePosition = sharedPrefsHelper[SharedPrefConstant.STATE, 0]
        viewModel.city.value = sharedPrefsHelper[SharedPrefConstant.CITY, ""]
        viewModel.zipCode.value = sharedPrefsHelper[SharedPrefConstant.ZIPCODE, ""]
        // Update selected questions answers
        viewModel.eventSelectedAnswerMap =
            intent.getSerializableExtra(AppConstant.SELECTED_ANSWER_MAP) as HashMap<Int, ArrayList<String>>
        // Update questions button text
        if (viewModel.eventSelectedAnswerMap.isNotEmpty()) {
            viewModel.questionBtnText.value =
                getString(R.string.view_order) + " {${viewModel.eventSelectedAnswerMap.keys.size}}"
        } else {
            viewModel.questionBtnText.value = getString(R.string.start_questions)
        }
    }

}