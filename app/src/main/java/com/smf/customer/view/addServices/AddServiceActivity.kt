package com.smf.customer.view.addServices

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smf.customer.R
import com.smf.customer.app.base.BaseActivity
import com.smf.customer.app.base.MyApplication
import com.smf.customer.app.constant.AppConstant
import com.smf.customer.databinding.ActivityAddServiceBinding
import com.smf.customer.di.sharedpreference.SharedPrefsHelper
import com.smf.customer.view.addServices.adapter.AddServiceAdapter
import com.smf.customer.view.dashboard.DashBoardActivity
import javax.inject.Inject

class AddServiceActivity : BaseActivity<AddServiceViewModel>(),
    AddServiceViewModel.CallBackInterface, AddServiceAdapter.ServiceClickListener {

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
        viewModel.setCallBackInterface(this)

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
        viewModel.getAddServices()
        // Observer for update services to UI
        viewModel.servicesList.observe(this, Observer {
            addServiceAdapter.updateServicesList(
                it,
                viewModel.preSelectedServices,
                viewModel.selectedServices
            )
        })
    }

    private fun buttonListeners() {
        binding.cancelBtn.setOnClickListener {
            finish()
        }
        binding.saveBtn.setOnClickListener {
            viewModel.postAddServices()
        }
    }

    override fun onClickSaveService() {
        Intent(this, DashBoardActivity::class.java).apply {
            putExtra(AppConstant.ON_EVENT, AppConstant.ON_ADD_SERVICE)
            startActivity(this)
        }
    }

    override fun onServiceClicked(listPosition: Int, status: Boolean) {
        // Update and remove time slots based on user selection
        if (status) {
            viewModel.servicesList.value?.get(listPosition)?.serviceName?.let {
                viewModel.selectedServices.add(it)
            }
        } else {
            viewModel.servicesList.value?.get(listPosition)?.serviceName?.let {
                viewModel.selectedServices.remove(it)
            }
        }
        // Update Adapter value
        addServiceAdapter.updateServicesList(
            viewModel.servicesList.value!!,
            viewModel.preSelectedServices,
            viewModel.selectedServices
        )
    }

    private fun setInitialDetails() {
        viewModel.eventId = intent.getIntExtra(AppConstant.EVENT_ID, 0)
        viewModel.eventTemplateId = intent.getIntExtra(AppConstant.TEMPLATE_ID, 0)
        viewModel.preSelectedServices =
            intent.getStringArrayListExtra(AppConstant.SERVICE_NAME_LIST) as ArrayList<String>
    }

}