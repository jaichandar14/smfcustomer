package com.smf.customer.view.provideservicedetails

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.datepicker.MaterialDatePicker
import com.smf.customer.R
import com.smf.customer.app.base.BaseActivity
import com.smf.customer.app.base.MyApplication
import com.smf.customer.app.constant.AppConstant
import com.smf.customer.data.model.response.BudgetCalcInfoDTO
import com.smf.customer.databinding.ActivityProvideServiceDetailsBinding
import com.smf.customer.di.sharedpreference.SharedPrefConstant
import com.smf.customer.di.sharedpreference.SharedPrefsHelper
import com.smf.customer.utility.DatePicker
import com.smf.customer.view.provideservicedetails.adapter.TimeSlotsAdapter
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Period
import java.time.ZoneId
import java.util.*
import javax.inject.Inject

class ProvideServiceDetailsActivity : BaseActivity<ProvideServiceViewModel>(),
    ProvideServiceViewModel.CallBackInterface, TimeSlotsAdapter.TimeSlotIconClickListener {
    lateinit var binding: ActivityProvideServiceDetailsBinding
    private var picker: MaterialDatePicker<Long> = DatePicker.newInstance
    lateinit var timeSlotRecycler: RecyclerView
    lateinit var mileDistance: Spinner
    lateinit var timeSlotsAdapter: TimeSlotsAdapter
    var estimatedBudget: String = ""

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

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        // Verify date picker dialog isVisible
        if (viewModel.isDatePickerDialogVisible()) {
            showDatePickerDialog()
        }
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
        // Start question button listener
        onClickQuestionsBtn()
        // Zipcode click listener
        onClickZipCode()
        // Initialize Mile Distance
        initializeMileDistance()
        // Error details observer
        errorDetailsObserver()
    }

    private fun onClickQuestionsBtn() {
        binding.startQusBtn.setOnClickListener {
//            navigateQuestionsPage()
        }
        binding.editImage.setOnClickListener {

        }
    }

    private fun navigateQuestionsPage() {
//        val intent = Intent(this, QuestionsActivity::class.java)
//        intent.putExtra("questionListItem", questionListItem)
//        intent.putIntegerArrayListExtra("questionNumberList", questionNumberList)
//        startActivity(intent)
    }

    private fun setTimeSlotsRecycler() {
        timeSlotsAdapter = TimeSlotsAdapter()
        timeSlotRecycler.layoutManager = GridLayoutManager(this, 2)
        timeSlotRecycler.adapter = timeSlotsAdapter
        timeSlotsAdapter.setOnClickListener(this)
        // API call for service time slots
        viewModel.getServiceSlots()
        // Observer for update service time slots to UI
        viewModel.timeSlotList.observe(this, androidx.lifecycle.Observer {
            Log.d(TAG, "setTimeSlotsRecycler: $it")
            timeSlotsAdapter.setTimeSlotList(it, viewModel.selectedSlotsPositionMap)
        })
    }

    private fun dateOnClickListeners() {
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

    private fun onClickZipCode() {
        binding.zipCode.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                // Check and Update budget amount to UI
                updateBudget(AppConstant.ZIPCODE)
            }
        }
    }

    private fun showDatePickerDialog() {
        // Check and Update budget amount to UI
        updateBudget(AppConstant.MATERIAL_DATE_PICKER)
        viewModel.showDatePickerDialogFlag()
        picker.addOnPositiveButtonClickListener {
            verifySelectedDate(it)
        }
        // show picker using this
        picker.show(supportFragmentManager, AppConstant.MATERIAL_DATE_PICKER)
    }

    private fun verifySelectedDate(it: Long) {
        val myFormat = AppConstant.DATE_FORMAT
        val sdf = SimpleDateFormat(myFormat)
        val date = Date(it)
        val formattedDate = sdf.format(date) // date selected by the user
        viewModel.eventDate.value = formattedDate
        val serviceDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
        viewModel.eventDateErrorVisibility.value =
            Period.between(LocalDate.now(), serviceDate).days <= 3
    }

    private fun initializeMileDistance() {
        val arrayAdapterMile = ArrayAdapter(
            applicationContext, R.layout.spinner_text_view, viewModel.mileList
        )
        mileDistance.adapter = arrayAdapterMile
        // Mile Distance spinner listener
        mileDistance.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {
                // Check and Update budget amount to UI
                updateBudget(AppConstant.MILE_SPINNER)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    override fun onSaveClick() {
//       TODO Next - Move to next screen
    }

    override fun onCancelClick() {
//    TODO Next - Move to previous screen
    }

    private fun errorDetailsObserver() {
        viewModel.estimatedBudget.observe(this, androidx.lifecycle.Observer {
            if (viewModel.screenRotationStatus.value == false) {
                viewModel.amountErrorVisibility.value = false
            }
        })
        viewModel.zipCode.observe(this, androidx.lifecycle.Observer {
            if (viewModel.screenRotationStatus.value == false) {
                viewModel.zipCodeErrorVisibility.value = false
            }
        })
    }

    override fun onResume() {
        super.onResume()
        // For Error visibility after screen rotation
        viewModel.setScreenRotationValueFalse()
    }

    override fun onSlotClicked(listPosition: Int, status: Boolean) {
        Log.d(TAG, "onSlotClicked: called $status")
        // Update selected time slots
        viewModel.selectedSlotsPositionMap[listPosition] = status
        // Check and Update budget amount to UI
        updateBudget(AppConstant.TIME_SLOT)
    }

    // Load budget value when estimatedBudget edittext value changed
    private fun updateBudget(from: String) {
        // Verify user entered same budget
        if (estimatedBudget == binding.estimatedBudget.text.toString().trim()) {
            return
        }
        // Update user budget
        estimatedBudget = binding.estimatedBudget.text.toString().trim()
        // From others(Not EditText)
        if (binding.estimatedBudget.isFocused &&
            binding.estimatedBudget.text.toString().isNotEmpty()
        ) {
            viewModel.getBudgetCalcInfo(estimatedBudget.toInt())
        }
        // From zipCode(EditText)
        if (binding.zipCode.isFocused && from == AppConstant.ZIPCODE) {
            viewModel.getBudgetCalcInfo(estimatedBudget.toInt())
        }
        // Observe budget value
        viewModel.budgetCalcInfo.observe(this, androidx.lifecycle.Observer {
            if (it != null) {
                updateBudgetToUI(it)
            }
        })
    }

    private fun updateBudgetToUI(budgetCalcInfoDTO: BudgetCalcInfoDTO) {
        Log.d(
            TAG,
            "onSuccess: responseDTO ${budgetCalcInfoDTO.data.currencyType}"
        )
        // show total and remaining amount to UI
        viewModel.remainingAmountVisibility.value = true
        if (budgetCalcInfoDTO.data.currencyType == AppConstant.NULL) {
            binding.totalAmount.text = "${budgetCalcInfoDTO.data.currencyType}" +
                    "  ${budgetCalcInfoDTO.data.estimatedEventBudget}"
            binding.remainingAmount.text = "${budgetCalcInfoDTO.data.currencyType}" +
                    "  ${budgetCalcInfoDTO.data.remainingBudget}"
        } else {
            binding.totalAmount.text = "${getString(R.string.dollar)}  " +
                    "${budgetCalcInfoDTO.data.estimatedEventBudget}"
            binding.remainingAmount.text = "${getString(R.string.dollar)}  " +
                    "${budgetCalcInfoDTO.data.remainingBudget}"
        }
    }

    private fun setInitialValues() {
        viewModel.eventDate.value = sharedPrefsHelper[SharedPrefConstant.EVENT_DATE, ""]
        viewModel.zipCode.value = sharedPrefsHelper[SharedPrefConstant.ZIPCODE, ""]
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
        viewModel.setScreenRotationValueTrue()
    }

}