package com.smf.customer.view.myevents

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
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
import com.smf.customer.dialog.DialogConstant
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
    lateinit var title: String
    lateinit var eventNo: String

    @Inject
    lateinit var sharedPrefsHelper: SharedPrefsHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mInitialize()
        viewModel.lastOrientation.value = resources.configuration.orientation
        // currentOrientation()
        //2974  viewModel.showLoading.value = true
        if (savedInstanceState == null) {
            viewModel.lastOrientation.observe(this, Observer { it ->
                lastOrientation = it
                if (it == 1) {
                    mEventOverviewRecycler(3)
                } else {
                    mEventOverviewRecycler(5)
                }
                viewModel.getEventType(sharedPrefsHelper[SharedPrefConstant.ACCESS_TOKEN, ""])
            })
        } else {


            viewModel.lastOrientation.observe(this, Observer { it ->
                if (it == 1) {
                    mEventOverviewRecycler(3)
                    viewModel.eventTypeList.observe(this, Observer {
                        mAdapterEvent.refreshItems(it, viewModel.eventClickedPos?.value)
                    })
                } else {
                    mEventOverviewRecycler(5)
                    viewModel.eventTypeList.observe(this, Observer {
                        mAdapterEvent.refreshItems(it, viewModel.eventClickedPos?.value)
                    })
                }
                //viewModel.showLoading.value = false
            })

        }
        viewModel.clickedEventTitle.observe(this, Observer {
            title = it.toString()

        })
        viewModel.clickedEventNo.observe(this, Observer {
            eventNo = it.toString()

        })

        // 3275 My Event Api call

    }

    override fun onStart() {
        super.onStart()
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
        MyApplication.applicationComponent?.inject(this)
        onNextClick()
    }

    // 3275 onNextButtonClick
    private fun onNextClick() {
        if (viewModel.eventClickedPos?.value != null) {
            viewModel.onClicked.value = true
            mDataBinding.appCompatButton.setOnClickListener {
                val intent = Intent(this, EventDetailsActivity::class.java)
                intent.putExtra(
                    AppConstant.TITLE,
                    title
                )
                intent.putExtra(
                    AppConstant.TEMPLATE_ID,
                    eventNo
                )
                startActivity(intent)
            }
        } else {
            viewModel.onClicked.value = false
            mDataBinding.appCompatButton.setOnClickListener {
                MyToast.show(this, getString(R.string.select_any_event), Toast.LENGTH_LONG)
            }
        }
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
    }

    // 3275 on click recyclerview green tick
    override fun onclick(position: Int?) {
        viewModel.eventClickedPos?.value = position
        viewModel.eventTypeList.value?.let {
            mAdapterEvent.refreshItems(
                it,
                viewModel.eventClickedPos?.value
            )
        }
        onNextClick()
        Log.d(
            TAG,
            "onclick clciekd pos: ${position?.let { viewModel.eventTypeList.value?.get(it)?.title }}"
        )
        viewModel.onClicked.value = true
        viewModel.clickedEventTitle.value =
            position?.let { viewModel.eventTypeList.value?.get(it)?.title }
        viewModel.clickedEventNo.value =
            position?.let { viewModel.eventTypeList.value?.get(it)?.numberText }
    }

    override fun getMyevetList(listMyEvents: ArrayList<EventStatusDTO>) {
        viewModel.showLoading.value = false
        eventTypeList = listMyEvents
        viewModel.eventTypeList.value = listMyEvents
        mAdapterEvent.refreshItems(listMyEvents, viewModel.eventClickedPos?.value)
    }

    override fun onPositiveClick(dialogFragment: DialogFragment) {
        super.onPositiveClick(dialogFragment)
        when {
            dialogFragment.tag.equals(DialogConstant.INTERNET_DIALOG) -> {
                dialogFragment.dismiss()
                viewModel.hideRetryDialogFlag()
            }
        }
    }


}



