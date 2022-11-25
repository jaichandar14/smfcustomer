package com.smf.customer.view.dashboard.fragment.serviceFragment.eventListDashBoard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smf.customer.R
import com.smf.customer.app.base.BaseFragment
import com.smf.customer.app.base.MyApplication
import com.smf.customer.databinding.FragmentEventsDashBoardBinding
import com.smf.customer.databinding.MyEventStatusDashboardBinding
import com.smf.customer.di.sharedpreference.SharedPrefsHelper
import com.smf.customer.view.dashboard.adaptor.Upcomingevent
import com.smf.customer.view.dashboard.fragment.serviceFragment.eventListDashBoard.adaptor.EventDetailsAdaptor
import com.smf.customer.view.dashboard.model.EventStatusDTO
import javax.inject.Inject

class EventsDashBoardFragment : BaseFragment<EventsDashBoardViewModel>() {

    @Inject
    lateinit var sharedPrefsHelper: SharedPrefsHelper
    private lateinit var mDataBinding: FragmentEventsDashBoardBinding
    private lateinit var mEventDetailsRecyclerView: RecyclerView
    private lateinit var mAdapterEventDetails: EventDetailsAdaptor

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mDataBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_events_dash_board, container, false)
        viewModel = ViewModelProvider(this)[EventsDashBoardViewModel::class.java]
        mDataBinding.eventsDashboardViewModel = viewModel
        mDataBinding.lifecycleOwner = this
        MyApplication.applicationComponent.inject(this)
        return mDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.onPending()
        mRecyclerViewIntializer()
 mDataBinding.statusLayout.myEventIcon.setOnClickListener {
            Log.d(TAG, "onViewCreated: ${mDataBinding.statusLayout.horizontalScroll.width}")
            Log.d(TAG, "onViewCreated: ${mDataBinding.statusLayout.horizontalScroll.measuredWidth/2}")

      mDataBinding.statusLayout.horizontalScroll.smoothScrollTo((mDataBinding.statusLayout.horizontalScroll.width/3).toInt(),0)
        }

    }
    private fun mRecyclerViewIntializer() {
        mEventDetailsRecycler()
        mAdapterEventDetails.refreshItems(getServiceCountList())

    }
    private fun mEventDetailsRecycler() {
        mEventDetailsRecyclerView = mDataBinding.eventDetailsRecyclerview
        mAdapterEventDetails = EventDetailsAdaptor()
        mEventDetailsRecyclerView.layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        mEventDetailsRecyclerView.adapter = mAdapterEventDetails
    }
    // Static data for demo need to implement in future
    private fun getServiceCountList(): ArrayList<EventStatusDTO> {
        val list = ArrayList<EventStatusDTO>()
        list.add(EventStatusDTO("4", "Active"))
        list.add(EventStatusDTO("3", "Pending"))
        list.add(EventStatusDTO("2", "Draft"))
        list.add(EventStatusDTO("1", "Inactive"))
        list.add(EventStatusDTO("0", "Rejected"))
        list.add(EventStatusDTO("4", "Active"))
        list.add(EventStatusDTO("3", "Pending"))
        list.add(EventStatusDTO("2", "Draft"))
        list.add(EventStatusDTO("1", "Inactive"))
        list.add(EventStatusDTO("0", "Rejected"))
        list.add(EventStatusDTO("4", "Active"))
        list.add(EventStatusDTO("3", "Pending"))
        list.add(EventStatusDTO("2", "Draft"))
        list.add(EventStatusDTO("1", "Inactive"))
        return list
    }
}