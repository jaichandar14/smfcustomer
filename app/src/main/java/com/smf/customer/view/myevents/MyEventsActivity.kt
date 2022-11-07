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
import com.smf.customer.view.dashboard.model.EventStatusDTO
import com.smf.customer.view.eventDetails.EventDetailsActivity
import com.smf.customer.view.myevents.Adaptor.MyEventListAdaptor

// 3262
class MyEventsActivity : BaseActivity<MyEventsViewModel>(),
    MyEventListAdaptor.OnServiceClickListener {
    private lateinit var mAdapterEvent: MyEventListAdaptor
    private lateinit var mDataBinding: com.smf.customer.databinding.ActivityMyEventsBinding
    private lateinit var mEventOverViewRecyclerView: RecyclerView
    var position: Int? = null

    //    var zeroCount=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_events)
        mInitialize()
        mEventOverviewRecycler()
        mAdapterEvent.refreshItems(getMyEvents(), position)

        mDataBinding.appCompatButton.setOnClickListener {
            val intent = Intent(this, EventDetailsActivity::class.java)
            intent.putExtra("template", 186)
            startActivity(intent)
        }
    }

    private fun mInitialize() {
        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_my_events)
        viewModel = ViewModelProvider(this)[MyEventsViewModel::class.java]
        mDataBinding.myEventViewModel = viewModel
        mDataBinding.lifecycleOwner = this
        MyApplication.applicationComponent.inject(this)

    }

    private fun mEventOverviewRecycler() {
        mEventOverViewRecyclerView = mDataBinding.myEventRecyclerView
        mAdapterEvent = MyEventListAdaptor()
        mEventOverViewRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val layoutManager = GridLayoutManager(this, 3)

        mEventOverViewRecyclerView.layoutManager = layoutManager
        mEventOverViewRecyclerView.adapter = mAdapterEvent

        mAdapterEvent.setOnClickListener(this)
    }

    private fun getMyEvents(): ArrayList<EventStatusDTO> {
        val list = ArrayList<EventStatusDTO>()
        list.add(EventStatusDTO("4", "Saaras Birthday"))
        list.add(EventStatusDTO("3", "jai Pending"))
        list.add(EventStatusDTO("2", "Vicky Draft"))
        list.add(EventStatusDTO("1", "WEdding new"))
        list.add(EventStatusDTO("0", "Rejected first"))
        list.add(EventStatusDTO("4", "Saaras Birthday"))
        list.add(EventStatusDTO("3", "jai Pending"))
        list.add(EventStatusDTO("2", "Vicky Draft"))
        list.add(EventStatusDTO("1", "WEdding new"))
        list.add(EventStatusDTO("0", "Rejected first"))

        return list
    }

    override fun onclick(position: Int?) {
        Log.d(TAG, "onclick: $position")
        mAdapterEvent.refreshItems(getMyEvents(), position)
    }
}