package com.smf.customer.view.eventDetails

import android.os.Bundle
import android.util.Log
import android.view.View
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
import com.smf.customer.data.model.response.EventQuestionsResponseDTO
import com.smf.customer.databinding.EventDetailsBinding
import com.smf.customer.di.sharedpreference.SharedPrefConstant
import com.smf.customer.di.sharedpreference.SharedPrefsHelper
import com.smf.customer.dialog.EventQuestionsCallback
import com.smf.customer.dialog.EventQuestionsDialog
import com.smf.customer.listener.DialogThreeButtonListener
import com.smf.customer.utility.DatePicker
import com.smf.customer.utility.Util
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class EventDetailsActivity : BaseActivity<EventDetailsViewModel>(), DialogThreeButtonListener,
    EventDetailsViewModel.CallBackInterface, EventQuestionsCallback {
    lateinit var binding: EventDetailsBinding
    private var picker: MaterialDatePicker<Long> = DatePicker.newInstance
    var currentCountryName = ""
    var token = ""

    @Inject
    lateinit var sharedPrefsHelper: SharedPrefsHelper

    companion object {
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
        viewModel.setCallBackInterface(this)
        init()

//        viewModel.screenRotationStatus.value = true
//        var local = (getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager).simCountryIso
//        val locale = Locale.getDefault()
//        locale.country
//        Log.d(TAG, "onCreate: $local")
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        // Verify event question dialog isVisible
        if (viewModel.isEventQuestionDialogVisible()) {
            createDialog()
        }
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
            if (questionListItem.isEmpty()) {
                startQuestionsButtonVisibility(false)
            }
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
        // Restrict number inside editText
        setEditTextFilter()
        // Setting User Details
        setUserDetails()
        // Error details observer
        errorDetailsObserver()
        // Event date edittext Listener
        eventDateListener()
        // Start question button listener
        onClickQuestionsBtn()
    }

    override fun updateQuestions(eventQuestionsResponseDTO: EventQuestionsResponseDTO) {
        if (eventQuestionsResponseDTO.data.questionnaireDtos.isEmpty()) {
            questionListItem.clear()
            startQuestionsButtonVisibility(false)
        } else {
            startQuestionsButtonVisibility(true)
            questionListItem.clear()
            eventQuestionsResponseDTO.data.questionnaireDtos.forEach {
                questionListItem.add(
                    QuestionListItem(
                        it.questionMetadata.question,
                        it.questionMetadata.choices as ArrayList<String>,
                        it.questionMetadata.questionType
                    )
                )
            }
        }
    }

    override fun onClickNext() {
//        TODO next grooming tasks
//        startActivity(Intent(this, DashBoardActivity::class.java))
    }

    private fun onClickQuestionsBtn() {
        binding.startQusBtn.setOnClickListener {
            createDialog()
        }
        binding.editImage.setOnClickListener {
            showEventQuestionDialog(
                AppConstant.EDIT_BUTTON,
                AppConstant.EVENT_QUESTIONS_DIALOG,
                viewModel.questionNumber
            )
        }
    }

    private fun createDialog() {
        if (binding.startQusBtn.text.toString().contains(getString(R.string.View_order))) {
            showEventQuestionDialog(
                binding.startQusBtn.text.toString(),
                AppConstant.VIEW_QUESTIONS_DIALOG, viewModel.viewOrderQuestionNumber
            )
        } else {
            showEventQuestionDialog(
                binding.startQusBtn.text.toString(),
                AppConstant.EVENT_QUESTIONS_DIALOG, viewModel.questionNumber
            )
        }
    }

    private fun showEventQuestionDialog(
        questionBtnStatus: String,
        tag: String,
        questionNumber: Int
    ) {
        Log.d(TAG, "setData: questionNumber ${viewModel.questionNumber}")
        // set dialog flag true
        viewModel.showEventQuestionDialogFlag()
        EventQuestionsDialog.newInstance(
            questionListItem,
            questionBtnStatus,
            viewModel.selectedAnswerPositionMap, questionNumber,
            this, this
        ).show(supportFragmentManager, tag)
    }

    override fun updateSelectedAnswer(questionNumber: Int, position: Int) {
        // Update Selected answer viewModel selectedAnswerPositionMap
        viewModel.selectedAnswerPositionMap[questionNumber] = position
    }

    override fun dialogStatus(status: String, questionNumber: Int) {
        if (status == AppConstant.SUBMIT) {
            viewModel.questionStatus = AppConstant.SUBMIT
            viewModel.questionBtnText.value =
                "${getString(R.string.View_order)} (${questionListItem.size})"
            viewModel.editImageVisibility.value = true
        } else {
            viewModel.questionStatus = AppConstant.CANCEL
            viewModel.questionBtnText.value = getString(R.string.Start_questions)
            viewModel.editImageVisibility.value = false
        }
        viewModel.questionNumber = questionNumber
    }

    override fun updateQusNumberOnScreenRotation(
        questionNumber: Int,
        dialogFragment: DialogFragment
    ) {
        when {
            dialogFragment.tag.equals(AppConstant.EVENT_QUESTIONS_DIALOG) -> {
                viewModel.questionNumber = questionNumber
            }
            dialogFragment.tag.equals(AppConstant.VIEW_QUESTIONS_DIALOG) -> {
                viewModel.viewOrderQuestionNumber = questionNumber
            }
        }
    }

    override fun onCancelClick(dialogFragment: DialogFragment) {
        // Update Flag status
        viewModel.hideEventQuestionDialogFlag()
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
        val arrayAdapterStatesSpinner = ArrayAdapter(
            applicationContext, R.layout.spinner_text_view, stateListForSpinner
        )
        binding.state.adapter = arrayAdapterStatesSpinner
    }

    private fun getCountryAndStateList() {
        val obj = JSONObject(
            Util.getCountriesAndStatesList(
                this, "countriesAndStateList.json"
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

    // Setting Initial User Details to the UI
    private fun setUserDetails() {
        binding.pageTitleText.text =
            intent.getStringExtra(AppConstant.TITLE) + " " + getString(R.string.Event_Details)
        token = sharedPrefsHelper[SharedPrefConstant.ACCESS_TOKEN, ""]
        viewModel.templateId = intent.getStringExtra(AppConstant.TEMPLATE_ID)?.toInt()
        viewModel.name.value =
            sharedPrefsHelper[SharedPrefConstant.FIRST_NAME, ""] + " " +
                    sharedPrefsHelper[SharedPrefConstant.LAST_NAME, ""]
        viewModel.emailId.value = sharedPrefsHelper[SharedPrefConstant.EMAIL_ID, ""]
        val countryCode = sharedPrefsHelper[SharedPrefConstant.COUNTRY_CODE, ""]
        val mobileNumber = sharedPrefsHelper[SharedPrefConstant.MOBILE_NUMBER, ""]
        viewModel.mobileNumber.value = mobileNumber.substringAfter(countryCode)
        binding.cppSignIn.setCountryForPhoneCode(sharedPrefsHelper[SharedPrefConstant.COUNTRY_CODE, 0])
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
            // Update question answer status
            viewModel.questionStatus = AppConstant.SUBMIT
            binding.startQusBtn.visibility = View.GONE
            binding.provideEventDetailsText.visibility = View.GONE
            binding.hostDetailsSeparator.visibility = View.GONE
        }
    }

    private fun setEditTextFilter() {
        binding.eventName.filters = arrayOf(Util.filterText())
        binding.city.filters = arrayOf(Util.filterText())
    }

}