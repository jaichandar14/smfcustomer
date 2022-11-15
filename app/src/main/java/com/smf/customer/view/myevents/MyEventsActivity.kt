package com.smf.customer.view.myevents

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smf.customer.R
import com.smf.customer.app.base.BaseActivity
import com.smf.customer.app.base.MyApplication
import com.smf.customer.app.constant.AppConstant
import com.smf.customer.di.sharedpreference.SharedPrefConstant
import com.smf.customer.di.sharedpreference.SharedPrefsHelper
import com.smf.customer.utility.MyToast
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
    var lastOrientation = 0

    @Inject
    lateinit var sharedPrefsHelper: SharedPrefsHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mInitialize()
        viewModel.lastOrientation.value = resources.configuration.orientation
        // currentOrientation()
        viewModel.showLoading.value = true
        if (savedInstanceState == null) {
            viewModel.lastOrientation.observe(this, Observer { it ->
                lastOrientation = it
                if (it == 1) {
                    mEventOverviewRecycler(3)
                } else {
                    mEventOverviewRecycler(5)
                }
            })
        } else {
            viewModel.lastOrientation.observe(this, Observer { it ->
                if (it == 1) {
                    mEventOverviewRecycler(3)
                    viewModel.showLoading.value = false
                } else {
                    mEventOverviewRecycler(4)
                    viewModel.showLoading.value = false
                }
            })

        }
        // 3275 My Event Api call

    }


    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        //lastOrientation?.let { outState.putInt("Rotated", it) }
        viewModel.eventClickedPos?.value?.let { outState.putInt("postion", it) }
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
    private fun onNextClick() {
        if (viewModel.eventClickedPos?.value == null) {
            viewModel.onClicked.value = false
        } else if (viewModel.eventClickedPos?.value != null) {
            viewModel.onClicked.value = true

        }
    }

    // 3285 on Back press click
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    // 3275 Recycler view for showing event type
    private fun mEventOverviewRecycler(i: Int) {
        mEventOverViewRecyclerView = mDataBinding.myEventRecyclerView
        mAdapterEvent = MyEventListAdaptor()
        mEventOverViewRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val layoutManager = GridLayoutManager(this, i)
        mEventOverViewRecyclerView.layoutManager = layoutManager
        mEventOverViewRecyclerView.adapter = mAdapterEvent
        mAdapterEvent.setOnClickListener(this)
        viewModel.setOnClickListener(this)
        viewModel.getEventType(sharedPrefsHelper[SharedPrefConstant.ACCESS_TOKEN, ""])
    }

    // 3275 on click recyclerview green tick
    override fun onclick(position: Int?) {
        viewModel.eventClickedPos?.value = position
        mAdapterEvent.refreshItems(eventTypeList, viewModel.eventClickedPos?.value)
        viewModel.onClicked.value = true

        if (viewModel.eventClickedPos?.value != null) {
            mDataBinding.appCompatButton.setOnClickListener {
                val intent = Intent(this, EventDetailsActivity::class.java)
                intent.putExtra(
                    AppConstant.TITLE,
                    eventTypeList[viewModel.eventClickedPos?.value!!].title
                )
                intent.putExtra(
                    AppConstant.TEMPLATE_ID,
                    eventTypeList[viewModel.eventClickedPos?.value!!].numberText
                )
                startActivity(intent)
            }
        } else {
            MyToast.show(this, getString(R.string.select_any_event), Toast.LENGTH_LONG)
        }
    }

    override fun getMyevetList(listMyEvents: ArrayList<EventStatusDTO>) {
        viewModel.showLoading.value = false
        eventTypeList = listMyEvents
        viewModel.eventTypeList.value = listMyEvents
        if (viewModel.eventClickedPos?.value != null) {
            mDataBinding.appCompatButton.setOnClickListener {
                val intent = Intent(this, EventDetailsActivity::class.java)
                intent.putExtra(
                    AppConstant.TITLE,
                    eventTypeList[viewModel.eventClickedPos?.value!!].title
                )
                intent.putExtra(
                    AppConstant.TEMPLATE_ID,
                    eventTypeList[viewModel.eventClickedPos?.value!!].numberText
                )
                startActivity(intent)
            }
        } else {
            mDataBinding.appCompatButton.setOnClickListener {
                MyToast.show(this, getString(R.string.select_any_event), Toast.LENGTH_LONG)
            }
        }
        mAdapterEvent.refreshItems(listMyEvents, viewModel.eventClickedPos?.value)
    }
}



