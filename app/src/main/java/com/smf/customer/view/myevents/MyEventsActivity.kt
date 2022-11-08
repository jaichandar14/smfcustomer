package com.smf.customer.view.myevents

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smf.customer.R
import com.smf.customer.app.base.BaseActivity
import com.smf.customer.app.base.MyApplication
import com.smf.customer.di.sharedpreference.SharedPrefConstant
import com.smf.customer.di.sharedpreference.SharedPrefsHelper
import com.smf.customer.view.dashboard.model.EventStatusDTO
import com.smf.customer.view.eventDetails.EventDetailsActivity
import com.smf.customer.view.myevents.Adaptor.MyEventListAdaptor
import javax.inject.Inject

// 3262
class MyEventsActivity : BaseActivity<MyEventsViewModel>(),
    MyEventListAdaptor.OnServiceClickListener, MyEventsViewModel.OnServiceClickListener {
    private lateinit var mAdapterEvent: MyEventListAdaptor
    private lateinit var mDataBinding: com.smf.customer.databinding.ActivityMyEventsBinding
    private lateinit var mEventOverViewRecyclerView: RecyclerView
    var position: Int? = null
    private var eventTypeList = ArrayList<EventStatusDTO>()

    @Inject
    lateinit var sharedPrefsHelper: SharedPrefsHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mInitialize()
        mEventOverviewRecycler()
        viewModel.showLoading.value = true
        // 3275 My Event Api call
        viewModel.getEventType(sharedPrefsHelper[SharedPrefConstant.ACCESS_TOKEN, ""])
    }

    // 3275
    private fun mInitialize() {
        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_my_events)
        viewModel = ViewModelProvider(this)[MyEventsViewModel::class.java]
        mDataBinding.myEventViewModel = viewModel
        mDataBinding.lifecycleOwner = this
        MyApplication.applicationComponent.inject(this)
        onNextClick()
    }

    // 3275 onNextButtonClick
    private fun onNextClick(){
        if (viewModel.eventClickedPos?.value ==null) {
            viewModel.onClicked.value = false
        }else if (viewModel.eventClickedPos?.value !=null) {
            viewModel.onClicked.value = true

        }
    }

    // 3275 on Back press click
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    // 3275 Recycler view for showing event type
    private fun mEventOverviewRecycler() {
        mEventOverViewRecyclerView = mDataBinding.myEventRecyclerView
        mAdapterEvent = MyEventListAdaptor()
        mEventOverViewRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val layoutManager = GridLayoutManager(this, 3)
        mEventOverViewRecyclerView.layoutManager = layoutManager
        mEventOverViewRecyclerView.adapter = mAdapterEvent
        mAdapterEvent.setOnClickListener(this)
        viewModel.setOnClickListener(this)
    }

    // 3275 on click recyclerview green tick
    override fun onclick(position: Int?) {
        Log.d(TAG, "onclick: $position")
        viewModel.eventClickedPos?.value = position
        mAdapterEvent.refreshItems(eventTypeList, viewModel.eventClickedPos?.value)
        viewModel.onClicked.value = true

        if(viewModel.eventClickedPos?.value != null){
            mDataBinding.appCompatButton.setOnClickListener {
                val intent = Intent(this, EventDetailsActivity::class.java)
                intent.putExtra(eventTypeList[viewModel.eventClickedPos?.value!!].title, eventTypeList[viewModel.eventClickedPos?.value!!].numberText)
                startActivity(intent)
            }
        }else{
            viewModel.showToastMessage("Select any Event to proceed")
        }
    }

    override fun getMyevetList(listMyEvents: ArrayList<EventStatusDTO>) {
        viewModel.showLoading.value = false
        eventTypeList = listMyEvents
        mAdapterEvent.refreshItems(listMyEvents, viewModel.eventClickedPos?.value)
    }
}