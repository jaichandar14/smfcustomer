package com.smf.customer.view.provideservicedetails

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.smf.customer.R
import com.smf.customer.app.base.BaseActivity
import com.smf.customer.app.base.MyApplication
import com.smf.customer.app.constant.AppConstant
import com.smf.customer.data.model.dto.QuestionListItem
import com.smf.customer.data.model.response.BudgetCalcInfoDTO
import com.smf.customer.data.model.response.EventQuestionsResponseDTO
import com.smf.customer.databinding.ActivityProvideServiceDetailsBinding
import com.smf.customer.di.sharedpreference.SharedPrefConstant
import com.smf.customer.di.sharedpreference.SharedPrefsHelper
import com.smf.customer.dialog.DialogConstant
import com.smf.customer.dialog.TwoButtonDialogFragment
import com.smf.customer.listener.DialogTwoButtonListener
import com.smf.customer.view.dashboard.DashBoardActivity
import com.smf.customer.view.provideservicedetails.adapter.TimeSlotsAdapter
import com.smf.customer.view.questions.QuestionsActivity
import java.math.BigDecimal
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class ProvideServiceDetailsActivity : BaseActivity<ProvideServiceViewModel>(),
    ProvideServiceViewModel.CallBackInterface, TimeSlotsAdapter.TimeSlotIconClickListener,
    DialogTwoButtonListener {
    lateinit var binding: ActivityProvideServiceDetailsBinding
    private val picker: MaterialDatePicker<Long> by lazy {
        // Makes only dates from today forward selectable.
        val constraintsBuilder =
            CalendarConstraints.Builder().setValidator(DateValidatorPointForward.now())
        MaterialDatePicker.Builder.datePicker().setTitleText(getString(R.string.service_date))
            .setCalendarConstraints(constraintsBuilder.build()).build()
    }
    private lateinit var timeSlotRecycler: RecyclerView
    private lateinit var mileDistance: Spinner
    private lateinit var timeSlotsAdapter: TimeSlotsAdapter
    private var twoButtonDialog: DialogFragment? = null

    @Inject
    lateinit var sharedPrefsHelper: SharedPrefsHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_provide_service_details)
        viewModel = ViewModelProvider(this)[ProvideServiceViewModel::class.java]
        binding.provideServiceViewModel = viewModel
        binding.lifecycleOwner = this@ProvideServiceDetailsActivity
        MyApplication.applicationComponent?.inject(this)
        viewModel.setCallBackInterface(this)

        init()
    }

    private fun init() {
        // Initialize recycler view
        timeSlotRecycler = binding.slotsRecyclerView
        // Initialize mileDistance spinner view
        mileDistance = binding.mileDistance
        // Initialize UI value
        setInitialValues()
        // Set timeSlots recyclerView Data
        setTimeSlotsRecycler()
        // Event date edittext Listener
        dateOnClickListeners()
        // Save and cancel Listener
        clickListeners()
        // estimatedBudget edittext focus Listener
        estimatedBudgetFocusListener()
        // Initialize Mile Distance
        initializeMileDistance()
        // Error details observer
        liveDataObservers()
    }

    private fun setTimeSlotsRecycler() {
        timeSlotsAdapter = TimeSlotsAdapter()
        timeSlotRecycler.layoutManager = GridLayoutManager(this, 2)
        timeSlotRecycler.adapter = timeSlotsAdapter
        timeSlotsAdapter.setOnClickListener(this)
    }

    private fun dateOnClickListeners() {
        picker.addOnPositiveButtonClickListener {
            val formatter = DateTimeFormatter.ofPattern(AppConstant.DATE_FORMAT)
            val date = Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate()
                .format(formatter)
            viewModel.serviceDate.value = date
            // Get Questions API call
            viewModel.updateQuestions(true)
            // Get slots based on selected date
            viewModel.updateServiceDate(true)
        }
        binding.eventDate.setOnClickListener {
            showDatePickerDialog()
        }
        binding.calendarImage.setOnClickListener {
            showDatePickerDialog()
        }
    }

    private fun showDatePickerDialog() {
        if (binding.estimatedBudget.isFocused &&
            viewModel.estimatedBudget.value.isNullOrEmpty().not()
        ) {
            binding.estimatedBudget.clearFocus()
            return
        } else {
            if (!picker.isVisible) {
                // show picker using this
                picker.show(supportFragmentManager, AppConstant.MATERIAL_DATE_PICKER)
            }
        }
    }

    private fun clickListeners() {
        binding.cancelBtn.setOnClickListener {
            // Delete specific service details from shared preference
            viewModel.removeServiceSharedPreference()
            finish()
        }

        binding.saveBtn.setOnClickListener {
            if (binding.estimatedBudget.isFocused &&
                viewModel.estimatedBudget.value.isNullOrEmpty().not()
            ) {
                binding.estimatedBudget.clearFocus()
                return@setOnClickListener
            } else {
                viewModel.onClickSaveBtn()
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initializeMileDistance() {
        val arrayAdapterMile = ArrayAdapter(
            applicationContext, R.layout.spinner_text_view, viewModel.mileList
        )
        mileDistance.adapter = arrayAdapterMile

        mileDistance.setOnTouchListener { _, event ->
            var returnValue = false
            if (event.action == MotionEvent.ACTION_UP) {
                if (binding.estimatedBudget.isFocused &&
                    viewModel.estimatedBudget.value.isNullOrEmpty().not()
                ) {
                    binding.estimatedBudget.clearFocus()
                    returnValue = true
                }
            }
            returnValue
        }
    }

    override fun onSaveClick() {
        Intent(this, DashBoardActivity::class.java).apply {
            this.putExtra(AppConstant.ON_EVENT, AppConstant.ON_EVENT)
            startActivity(this)
        }
    }

    override fun onClickQuestionsBtn(from: String) {
        if (binding.estimatedBudget.isFocused &&
            viewModel.estimatedBudget.value.isNullOrEmpty().not()
        ) {
            binding.estimatedBudget.clearFocus()
            return
        } else {
            val intent = Intent(this, QuestionsActivity::class.java)
            intent.putExtra(AppConstant.FROM_ACTIVITY, this::class.java.simpleName)
            intent.putExtra(AppConstant.QUESTION_LIST_ITEM, viewModel.questionListItem)
            intent.putIntegerArrayListExtra(
                AppConstant.QUESTION_NUMBER_LIST, viewModel.questionNumberList
            )
            intent.putExtra(AppConstant.SELECTED_ANSWER_MAP, viewModel.eventSelectedAnswerMap)
            intent.putExtra(
                AppConstant.QUESTION_BTN_TEXT,
                if (from != AppConstant.EDIT_BUTTON) viewModel.questionBtnText.value else ""
            )
            finish()
            startActivity(intent)
        }
    }

    private fun estimatedBudgetFocusListener() {
        binding.estimatedBudget.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus.not()) {
                viewModel.setDoBudgetAPICall(true)
            }
        }
    }

    private fun liveDataObservers() {
        // Observer for service date lead period verification
        viewModel.updateServiceDate.observe(this, androidx.lifecycle.Observer {
            if (it) {
                // Lead period verification
                viewModel.updateLeadPeriodVerification(viewModel.serviceDate.value!!)
                // Get time slots based on selected date
                viewModel.getServiceSlots()
                // Update submit button background color
                submitBtnColorVisibility()
                // To avoid observer observe data multiple times
                viewModel.updateServiceDate(false)
            }
        })

        // Observer for update service time slots to UI
        viewModel.timeSlotList.observe(this, androidx.lifecycle.Observer {
            Log.d(TAG, "setTimeSlotsRecycler: $it")
            // Show error and slots visibility based on timeslots
            if (it.isNullOrEmpty()) {
                val message = getString(R.string.no_service_is_available_)
                viewModel.setServiceDateErrorText(message)
                viewModel.showServiceDateError()
                viewModel.hideTimeSlot()
            } else {
                viewModel.showTimeSlot()
                timeSlotsAdapter.setTimeSlotList(it, viewModel.selectedSlotsList)
                // Verify questions API call required
                if (viewModel.updateQuestions.value == true) {
                    // Get Service Questions
                    viewModel.getServiceDetailQuestions()
                }
            }
        })

        // Observer for budget value
        viewModel.estimatedBudget.observe(this, androidx.lifecycle.Observer {
            if (it != null) {
                // Refresh error message visibility
                if (it.isNotEmpty() && viewModel.amountErrorVisibility.value == true) {
                    viewModel.hideAmountErrorText()
                }
                // Update submit button background color
                submitBtnColorVisibility()
            }
        })

        // Observer for execute BudgetCalcInfo api call
        viewModel.doBudgetAPICall.observe(this, androidx.lifecycle.Observer {
            if (it) {
                viewModel.estimatedBudget.value?.let { it1 ->
                    if (it1.isNotEmpty()) {
                        viewModel.getBudgetCalcInfo(it1)
                    }
                }
                // Reset livedata value
                viewModel.setDoBudgetAPICall(false)
            }
        })

        // Observe for get and update budget value
        viewModel.budgetCalcInfo.observe(this, androidx.lifecycle.Observer {
            if (it != null) {
                updateBudget(it)
                // Reset livedata value
                viewModel.setNullToBudgetCalcInfo()
                // Update submit button background color
                submitBtnColorVisibility()
            }
        })

        viewModel.zipCode.observe(this, Observer {
            // Auto refresh ZipCode error
            if (it.isNullOrEmpty().not() && viewModel.zipCodeErrorVisibility.value == true) {
                viewModel.hideZipCodeError()
            }
            // Update submit button background color
            submitBtnColorVisibility()
        })

        viewModel.eventQuestionsResponseDTO.observe(
            this,
            androidx.lifecycle.Observer { eventQuestionsResponseDTO ->
                if (eventQuestionsResponseDTO != null) {
                    viewModel.questionListItem.clear()
                    if (eventQuestionsResponseDTO.data.questionnaireDtos.isEmpty()) {
                        viewModel.hideStartQuestionsBtn()
                    } else {
                        viewModel.showStartQuestionsBtn()
                        viewModel.eventSelectedAnswerMap.clear()
                        editButtonVisibility()
                        // Update Questions
                        updateQuestionsList(eventQuestionsResponseDTO)
                        // Update Question Number
                        updateQuestionNumberList()
                    }
                    // Update submit button background color
                    submitBtnColorVisibility()
                }
            })
    }

    override fun onSlotClicked(listPosition: Int, status: Boolean) {
        // Update and remove time slots based on user selection
        if (status) {
            // Update selected time slots
            viewModel.selectedSlotsList.add(viewModel.timeSlotList.value!![listPosition])
        } else {
            viewModel.selectedSlotsList.remove(viewModel.timeSlotList.value!![listPosition])
        }
        // Manage timeslot error visibility
        if (viewModel.selectedSlotsList.isNotEmpty() &&
            viewModel.timeSlotErrorVisibility.value == true
        ) {
            viewModel.hideTimeSlotError()
        }
        // Verify slot clicked while estimatedBudget focused
        if (binding.estimatedBudget.isFocused) {
            binding.estimatedBudget.clearFocus()
        }
        // Update submit button background color
        submitBtnColorVisibility()
    }

    private fun updateBudget(budgetCalcInfoDTO: BudgetCalcInfoDTO) {
        // Verify remaining budget
        if (verifyRemainingBudget(budgetCalcInfoDTO)) {
            updateBudgetToUI(budgetCalcInfoDTO)
        }
    }

    private fun verifyRemainingBudget(budgetCalcInfoDTO: BudgetCalcInfoDTO): Boolean {
        return if (budgetCalcInfoDTO.data.remainingBudget.toString().startsWith("-").not()) {
            true
        } else {
            showEstimationDialog(budgetCalcInfoDTO)
            Log.d(TAG, "verifyRemainingBudget: else called")
            false
        }
    }

    private fun updateBudgetToUI(budgetCalcInfoDTO: BudgetCalcInfoDTO) {
        // show total and remaining amount to UI
        viewModel.showRemainingAmountLayout()

        val totalBudget = getExactAmountToUI(budgetCalcInfoDTO.data.estimatedEventBudget)
        val remainingBudget = getExactAmountToUI(budgetCalcInfoDTO.data.remainingBudget)

        if (budgetCalcInfoDTO.data.currencyType == AppConstant.NULL) {
            viewModel.setTotalAmount(budgetCalcInfoDTO.data.currencyType + "  $totalBudget")
            viewModel.setRemainingAmount(budgetCalcInfoDTO.data.currencyType + "  $remainingBudget")
        } else {
            viewModel.setTotalAmount("${getString(R.string.dollar)}  " + totalBudget)
            viewModel.setRemainingAmount("${getString(R.string.dollar)}  " + remainingBudget)
        }
    }

    // Get exact value to show UI
    private fun getExactAmountToUI(amount: BigDecimal): String {
        return if (amount.toString().contains("E")) {
            amount.longValueExact().toString()
        } else {
            amount.toString()
        }
    }

    private fun showEstimationDialog(budgetCalcInfoDTO: BudgetCalcInfoDTO) {
        var remainingBudgetAmount = budgetCalcInfoDTO.data.remainingBudget.toString()
        if (remainingBudgetAmount.startsWith("-")) {
            remainingBudgetAmount =
                remainingBudgetAmount.substring(1, remainingBudgetAmount.length - 1)
        }
        // For update totalAmount while serviceAmount exceeds the totalAmount
        viewModel.updateTotalAmount =
            (budgetCalcInfoDTO.data.estimatedEventBudget + remainingBudgetAmount.toBigDecimal())
        val message =
            "${getString(R.string.service_Budget_Exceeds_)} $remainingBudgetAmount ${
                getString(R.string.would_you_like_to_update_)
            } ${viewModel.updateTotalAmount}"

        if (twoButtonDialog == null) {
            twoButtonDialog = TwoButtonDialogFragment.newInstance(
                message,
                getString(R.string.do_you_want_to_Update_),
                this@ProvideServiceDetailsActivity,
                false
            )
        }
        if (twoButtonDialog?.isVisible != true) {
            twoButtonDialog?.show(supportFragmentManager, DialogConstant.ESTIMATION_DIALOG)
        }
    }

    override fun onNegativeClick(dialogFragment: DialogFragment) {
        super.onNegativeClick(dialogFragment)
        Log.d(TAG, "onNegativeClick: called")
        // Dismiss dialog
        dismissEstimationDialog()
        // Update error value
        viewModel.showAmountErrorText(getString(R.string.please_enter_the_valid_))
        viewModel.setNullToEstimatedBudget()
        // Hide total and remaining amount to UI
        viewModel.hideRemainingAmountLayout()
    }

    override fun onPositiveClick(dialogFragment: DialogFragment) {
        super.onPositiveClick(dialogFragment)
        Log.d(TAG, "onPositiveClick: called")
        dismissEstimationDialog()
        viewModel.putBudgetCalcInfo()
    }

    private fun dismissEstimationDialog() {
        twoButtonDialog?.let {
            if (it.isVisible) {
                it.dismiss()
            }
        }
    }

    private fun updateQuestionsList(eventQuestionsResponseDTO: EventQuestionsResponseDTO) {
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
    }

    private fun setInitialValues() {
        if (intent.extras?.getString(AppConstant.SERVICE_QUESTIONS) != AppConstant.SERVICE_QUESTIONS) {
            sharedPrefsHelper.put(
                SharedPrefConstant.EVENT_SERVICE_ID,
                intent.getStringExtra(AppConstant.EVENT_SERVICE_ID) ?: "0"
            )
            sharedPrefsHelper.put(
                SharedPrefConstant.SERVICE_CATEGORY_ID,
                intent.getStringExtra(AppConstant.SERVICE_CATEGORY_ID) ?: "0"
            )
            sharedPrefsHelper.put(
                SharedPrefConstant.EVENT_SERVICE_DESCRIPTION_ID,
                intent.getStringExtra(AppConstant.EVENT_SERVICE_DESCRIPTION_ID) ?: "0"
            )
            sharedPrefsHelper.put(
                SharedPrefConstant.LEAD_PERIOD,
                intent.getStringExtra(AppConstant.LEAD_PERIOD) ?: "0"
            )
            viewModel.serviceDate.value = sharedPrefsHelper[SharedPrefConstant.EVENT_DATE, ""]
            viewModel.zipCode.value = sharedPrefsHelper[SharedPrefConstant.ZIPCODE, ""]
            // Get Initial Questions API call
            viewModel.updateQuestions(true)
            // Get Initial time slots based on event date
            viewModel.updateServiceDate(true)
        } else {
            updateEnteredValues()
            // Avoid getting initial Questions API call
            viewModel.updateQuestions(false)
            // Get Initial time slots based on event date
            viewModel.updateServiceDate(false)
        }
    }

    private fun updateEnteredValues() {
        viewModel.serviceDate.value = sharedPrefsHelper[SharedPrefConstant.SERVICE_DATE, ""]
        // Lead period verification
        viewModel.updateLeadPeriodVerification(viewModel.serviceDate.value!!)
        viewModel.timeSlotList.value =
            sharedPrefsHelper.getArrayList(SharedPrefConstant.TIME_SLOT_LIST) as ArrayList<String>
        viewModel.selectedSlotsList =
            sharedPrefsHelper.getArrayList(SharedPrefConstant.SELECTED_TIME_SLOTS) as ArrayList<String>
        viewModel.zipCode.value = sharedPrefsHelper[SharedPrefConstant.SERVICE_ZIPCODE, ""]
        viewModel.estimatedBudget.value = sharedPrefsHelper[SharedPrefConstant.ESTIMATED_BUDGET, ""]
        viewModel.totalAmount.value = sharedPrefsHelper[SharedPrefConstant.TOTAL_AMOUNT, ""]
        viewModel.remainingAmount.value = sharedPrefsHelper[SharedPrefConstant.REMAINING_AMOUNT, ""]
        // show total and remaining amount to UI
        if (viewModel.estimatedBudget.value.isNullOrEmpty()) {
            viewModel.hideRemainingAmountLayout()
        } else {
            viewModel.showRemainingAmountLayout()
        }
        viewModel.milePosition.value = sharedPrefsHelper[SharedPrefConstant.SERVICE_MILES, 0]
        // Question data
        viewModel.questionListItem =
            sharedPrefsHelper.getQuestionsList(SharedPrefConstant.QUESTION_LIST_ITEM)
        viewModel.questionNumberList =
            sharedPrefsHelper.getArrayIntList(SharedPrefConstant.QUESTION_NUMBER_LIST)
        // Update selected questions answers
        viewModel.eventSelectedAnswerMap =
            sharedPrefsHelper.getHashMap(SharedPrefConstant.SERVICE_SELECTED_ANSWER_MAP) as HashMap<Int, ArrayList<String>>
        // Update questions button visibility
        if (viewModel.questionListItem.isNotEmpty()) {
            viewModel.showStartQuestionsBtn()
            editButtonVisibility()
        }
    }

    private fun editButtonVisibility() {
        // Update questions button text
        if (viewModel.eventSelectedAnswerMap.isNotEmpty()) {
            viewModel.questionBtnText.value =
                getString(R.string.view_order) + " {${viewModel.eventSelectedAnswerMap.keys.size}}"
            viewModel.editImageVisibility.value = true
        } else {
            viewModel.questionBtnText.value = getString(R.string.start_questions)
            viewModel.editImageVisibility.value = false
        }
    }

    private fun submitBtnColorVisibility() {
        // Verify all user details are entered
        if (viewModel.userDetailsValidation(AppConstant.ON_VERIFY) &&
            viewModel.verifyMandatoryQuesAnswered(
                viewModel.questionListItem, viewModel.eventSelectedAnswerMap
            )
        ) {
            binding.saveBtn.background = getDrawable(R.drawable.custom_button_corner_ok)
            binding.saveBtn.setTextColor(ContextCompat.getColor(this, R.color.white))
        } else {
            binding.saveBtn.background = getDrawable(R.drawable.custom_button_corner_ok_fade)
            binding.saveBtn.setTextColor(ContextCompat.getColor(this, R.color.gray_text))
        }
    }

}