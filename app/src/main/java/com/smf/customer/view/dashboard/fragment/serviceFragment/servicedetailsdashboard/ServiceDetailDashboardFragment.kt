package com.smf.customer.view.dashboard.fragment.serviceFragment.servicedetailsdashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView
import com.smf.customer.R
import com.smf.customer.app.base.BaseFragment
import com.smf.customer.app.base.MyApplication
import com.smf.customer.databinding.FragmentServiceDetailDashboardBinding
import com.smf.customer.di.sharedpreference.SharedPrefsHelper
import com.smf.customer.view.dashboard.fragment.serviceFragment.eventListDashBoard.adaptor.StatusDetailsAdaptor
import com.smf.customer.view.dashboard.fragment.serviceFragment.servicedetailsdashboard.adaapter.ServiceDashboardAdapter
import com.smf.customer.view.dashboard.fragment.serviceFragment.servicedetailsdashboard.expandablelist.ChildData
import com.smf.customer.view.dashboard.fragment.serviceFragment.servicedetailsdashboard.expandablelist.ParentData
import com.smf.customer.view.dashboard.fragment.serviceFragment.sharedviewmodel.EventsDashBoardViewModel
import javax.inject.Inject


class ServiceDetailDashboardFragment :  BaseFragment<EventsDashBoardViewModel>() {
    private lateinit var mDataBinding: FragmentServiceDetailDashboardBinding
    private lateinit var mBiddingDetailsRecyclerView: RecyclerView
    private lateinit var mAdapterBiddingDetails: ServiceDashboardAdapter
    private lateinit var mStatusDetailsRecyclerView: RecyclerView
    private lateinit var mAdapterStatusDetails: StatusDetailsAdaptor
    @Inject
    lateinit var sharedPrefsHelper: SharedPrefsHelper
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mDataBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_service_detail_dashboard, container, false)
        viewModel = ViewModelProvider(this)[EventsDashBoardViewModel::class.java]
        mDataBinding.eventsDashboardViewModel = viewModel
        mDataBinding.lifecycleOwner = this
        MyApplication.applicationComponent?.inject(this)
        return mDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBiddingFlowRecycler()
        mStatusFlowRecycler()
        mAdapterStatusDetails.refreshItems(viewModel.setStatusFlowDetails(viewModel.stepTwo))

    }
    // 3438 Intializing bidding recycler
    private fun mBiddingFlowRecycler(){
        mBiddingDetailsRecyclerView = mDataBinding.exRecycle
        mAdapterBiddingDetails = ServiceDashboardAdapter(requireContext(),viewModel.setBiddingDetails())
        mBiddingDetailsRecyclerView.layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        mBiddingDetailsRecyclerView.adapter = mAdapterBiddingDetails
    }

    // 3438 Intializing status recycler
    private fun mStatusFlowRecycler() {
        mStatusDetailsRecyclerView = mDataBinding.statusLayout.stepperListview
        val layoutManager =
            LinearLayoutManager(requireContext(), OrientationHelper.HORIZONTAL, false)
        mStatusDetailsRecyclerView.layoutManager = layoutManager
        mAdapterStatusDetails = StatusDetailsAdaptor()
        mStatusDetailsRecyclerView.adapter = mAdapterStatusDetails
    }


}