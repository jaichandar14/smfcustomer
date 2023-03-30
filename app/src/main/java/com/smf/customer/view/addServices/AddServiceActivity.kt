package com.smf.customer.view.addServices

import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smf.customer.R
import com.smf.customer.app.base.BaseActivity
import com.smf.customer.app.base.MyApplication
import com.smf.customer.databinding.ActivityAddServiceBinding
import com.smf.customer.di.sharedpreference.SharedPrefsHelper
import com.smf.customer.view.addServices.adapter.AddServiceAdapter
import javax.inject.Inject

class AddServiceActivity : BaseActivity<AddServiceViewModel>(),
    AddServiceAdapter.ServiceClickListener {

    lateinit var binding: ActivityAddServiceBinding
    lateinit var serviceRecycler: RecyclerView
    lateinit var addServiceAdapter: AddServiceAdapter

    @Inject
    lateinit var sharedPrefsHelper: SharedPrefsHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            DataBindingUtil.setContentView(this@AddServiceActivity, R.layout.activity_add_service)
        viewModel = ViewModelProvider(this)[AddServiceViewModel::class.java]
        binding.addServiceViewModel = viewModel
        binding.lifecycleOwner = this@AddServiceActivity
        MyApplication.applicationComponent?.inject(this)

        init()
    }

    private fun init() {
        // Initialize variables
        setInitialDetails()
        // Initialize recycler view
        serviceRecycler = binding.rcList
        // Set service recyclerView Data
        setServiceRecycler()
        // save and cancel button Listeners
        buttonListeners()
    }

    private fun setServiceRecycler() {
        addServiceAdapter = AddServiceAdapter(this@AddServiceActivity)
        serviceRecycler.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        serviceRecycler.adapter = addServiceAdapter
        addServiceAdapter.setOnClickListener(this)
        // API call for services list
        viewModel.getAddServices(187)
        // Observer for update services to UI
        viewModel.servicesList.observe(this, Observer {
            addServiceAdapter.updateServicesList(
                it,
                viewModel.selectedServicePositionMap,
                viewModel.preSelectedServices
            )
        })
    }

    private fun buttonListeners() {
        binding.cancelBtn.setOnClickListener {
            finish()
        }
        binding.saveBtn.setOnClickListener {

        }
    }

    override fun onServiceClicked(listPosition: Int, status: Boolean) {
        Log.d(TAG, "onServiceClicked: $listPosition $status")
        // Update and remove time slots based on user selection
        if (status) {
            // Update selected time slots
            viewModel.selectedServicePositionMap[listPosition] = true
        } else {
            viewModel.selectedServicePositionMap.remove(listPosition)
        }
    }

    private fun setInitialDetails() {
        // TODO Next task
        // list of services in previous page
//        viewModel.selectedServicePositionMap[1] = true
//        viewModel.selectedServicePositionMap[5] = true
        viewModel.preSelectedServices.add("De Papel fixed")
        viewModel.preSelectedServices.add("Venue")
        // id - get api call
    }

}