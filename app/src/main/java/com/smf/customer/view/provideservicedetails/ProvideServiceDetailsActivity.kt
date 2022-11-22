package com.smf.customer.view.provideservicedetails

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.datepicker.MaterialDatePicker
import com.smf.customer.R
import com.smf.customer.app.base.BaseActivity
import com.smf.customer.app.base.MyApplication
import com.smf.customer.app.constant.AppConstant
import com.smf.customer.databinding.ActivityProvideServiceDetailsBinding
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
    lateinit var timeSlotsAdapter: TimeSlotsAdapter

    @Inject
    lateinit var sharedPrefsHelper: SharedPrefsHelper

    companion object {
        private val timeSlotList = ArrayList<String>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_provide_service_details)
        viewModel = ViewModelProvider(this)[ProvideServiceViewModel::class.java]
        binding.provideServiceViewModel = viewModel
        binding.lifecycleOwner = this@ProvideServiceDetailsActivity
        MyApplication.applicationComponent.inject(this)
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
        // Set timeSlots recyclerView Data
        setTimeSlotsRecycler()
        // Event date edittext Listener
        dateOnClickListeners()
        // Initialize Mile Distance
        initializeMileDistance()
        // Error details observer
        errorDetailsObserver()
    }

    private fun setTimeSlotsRecycler() {
        timeSlotsAdapter = TimeSlotsAdapter()
        timeSlotRecycler.layoutManager = GridLayoutManager(this, 2)
        timeSlotRecycler.adapter = timeSlotsAdapter
        timeSlotsAdapter.setOnClickListener(this)
        timeSlotList.clear()
        timeSlotList.add("12 - 3 am")
        timeSlotList.add("3 - 6 am")
        timeSlotList.add("6 - 9 am")
        timeSlotList.add("9 - 12 pm")
        timeSlotList.add("12 - 3 pm")
        timeSlotList.add("3 - 9 pm")
        timeSlotList.add("9 - 12 am")
        timeSlotsAdapter.setTimeSlotList(timeSlotList, viewModel.selectedSlotsPositionMap)
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

    private fun showDatePickerDialog() {
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
        binding.mileDistance.adapter = arrayAdapterMile
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

    override fun onSlotClicked(listPosition: Int, status: Boolean) {
        Log.d(TAG, "onSlotClicked: called $status")
        // Update selected time slots
        viewModel.selectedSlotsPositionMap[listPosition] = status
    }

}