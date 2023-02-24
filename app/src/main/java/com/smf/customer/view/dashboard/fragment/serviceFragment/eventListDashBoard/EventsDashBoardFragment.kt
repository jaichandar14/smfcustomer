package com.smf.customer.view.dashboard.fragment.serviceFragment.eventListDashBoard

import android.content.Intent
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
import com.smf.customer.app.constant.AppConstant
import com.smf.customer.data.model.dto.DialogListItem
import com.smf.customer.databinding.FragmentEventsDashBoardBinding
import com.smf.customer.di.sharedpreference.SharedPrefsHelper
import com.smf.customer.dialog.MultipleSelectionListDialog
import com.smf.customer.view.dashboard.fragment.serviceFragment.eventListDashBoard.adaptor.EventDetailsAdaptor
import com.smf.customer.view.dashboard.model.EventStatusDTO
import com.smf.customer.view.eventDetails.EventDetailsActivity
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
        MyApplication.applicationComponent?.inject(this)
        return mDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.onPending()
        mRecyclerViewIntializer()
        mDataBinding.statusLayout.myEventIcon.setOnClickListener {
            Log.d(TAG, "onViewCreated: ${mDataBinding.statusLayout.horizontalScroll.width}")
            Log.d(
                TAG,
                "onViewCreated: ${mDataBinding.statusLayout.horizontalScroll.measuredWidth / 2}"
            )

            mDataBinding.statusLayout.horizontalScroll.smoothScrollTo(
                (mDataBinding.statusLayout.horizontalScroll.width / 3).toInt(),
                0
            )
        }
        mDataBinding.addServiceIcon.setOnClickListener {
            val list =
                ArrayList<DialogListItem>()
            list.add(DialogListItem("1", "Venue"))
            list.add(DialogListItem("1", "Venue"))
            list.add(DialogListItem("1", "Venue"))
            list.add(DialogListItem("1", "Venue"))

            MultipleSelectionListDialog.newInstance(
                getString(R.string.service_list),
                list, this, true, R.string.save_services,
                R.string.cancel
            )
                .show(requireActivity().supportFragmentManager, "MultipleSelectionListDialog")


        }
        onClickViewDetails()
    }

    private fun onClickViewDetails() {
        mDataBinding.statusLayout.eventViewDetails.setOnClickListener {
//            val intent =
//                Intent(requireContext().applicationContext, EventDetailsActivity::class.java)
//            intent.putExtra(AppConstant.EVENT_DASH_BOARD, AppConstant.EVENT_DASH_BOARD)
//            startActivity(intent)
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