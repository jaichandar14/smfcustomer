package com.smf.customer.view.dashboard.fragment.serviceFragment.servicedetailsdashboard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView
import com.smf.customer.R
import com.smf.customer.app.base.BaseFragment
import com.smf.customer.app.base.MyApplication
import com.smf.customer.data.model.response.DataBidding
import com.smf.customer.databinding.FragmentServiceDetailDashboardBinding
import com.smf.customer.di.sharedpreference.SharedPrefConstant
import com.smf.customer.di.sharedpreference.SharedPrefsHelper
import com.smf.customer.view.dashboard.fragment.serviceFragment.eventListDashBoard.adaptor.StatusDetailsAdaptor
import com.smf.customer.view.dashboard.fragment.serviceFragment.servicedetailsdashboard.adaapter.ServiceDashboardAdapter
import com.smf.customer.view.dashboard.fragment.serviceFragment.servicedetailsdashboard.adaapter.ServiceDetailsAdapter
import com.smf.customer.view.dashboard.fragment.serviceFragment.servicedetailsdashboard.adaapter.SlotAdapter
import com.smf.customer.view.dashboard.fragment.serviceFragment.sharedviewmodel.EventsDashBoardViewModel
import com.smf.customer.view.dashboard.model.EventStatusDTO
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


class ServiceDetailDashboardFragment(var serviceDescriptionId: String?) :
    BaseFragment<EventsDashBoardViewModel>(),
    EventsDashBoardViewModel.OnServiceDetailsClickListener {
    private lateinit var mDataBinding: FragmentServiceDetailDashboardBinding
    private lateinit var mBiddingDetailsRecyclerView: RecyclerView
    private lateinit var mAdapterBiddingDetails: ServiceDashboardAdapter
    private lateinit var mStatusDetailsRecyclerView: RecyclerView
    private lateinit var mAdapterStatusDetails: StatusDetailsAdaptor
    private lateinit var mServiceDetailsRecyclerView: RecyclerView
    private lateinit var mAdapterServiceDetails: ServiceDetailsAdapter
    private lateinit var mSlotRecyclerView: RecyclerView
    private lateinit var mAdapterSlot: SlotAdapter
    private val formatter: DateTimeFormatter =
        DateTimeFormatter.ofPattern("MM/dd/yyyy", Locale.ENGLISH)


    @Inject
    lateinit var sharedPrefsHelper: SharedPrefsHelper
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mDataBinding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_service_detail_dashboard,
                container,
                false
            )
        viewModel = ViewModelProvider(this)[EventsDashBoardViewModel::class.java]
        mDataBinding.eventsDashboardViewModel = viewModel
        mDataBinding.lifecycleOwner = this
        MyApplication.applicationComponent?.inject(this)
        return mDataBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Initialize the call back listeners
        viewModel.setOnServiceClickListener(this)
        mBiddingFlowRecycler()
        mStatusFlowRecycler()
        mServiceRecyclerView()
        mSlotRecyclerView()
        setEventServiceDetails()
        mAdapterStatusDetails.refreshItems(viewModel.setStatusFlowDetails(viewModel.stepThree))
        Log.d(TAG, "onViewCreated service Dashboard: $serviceDescriptionId")
        serviceDescriptionId?.toLong()?.let {
            viewModel.getBiddingResponse(
                sharedPrefsHelper[SharedPrefConstant.EVENT_ID, 0], it
            )
        }
    }

    // 3438 Intializing bidding recycler
    private fun mBiddingFlowRecycler() {
        mBiddingDetailsRecyclerView = mDataBinding.exRecycle
        mAdapterBiddingDetails =
            ServiceDashboardAdapter(requireContext(), viewModel.setBiddingDetails())
        mBiddingDetailsRecyclerView.layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        mBiddingDetailsRecyclerView.adapter = mAdapterBiddingDetails
    }
    private fun setEventServiceDetails() {
        val date = LocalDate.parse(
            sharedPrefsHelper[SharedPrefConstant.EVENT_DATE, ""], formatter
        )
        val formatter1 = DateTimeFormatter.ofPattern("dd MMM yyyy")
        val formattedDate = date.format(formatter1)
        mDataBinding.statusLayout.eventDateTxt.text =
            formattedDate
        mDataBinding.statusLayout.titleTxt.text =
            sharedPrefsHelper[SharedPrefConstant.EVENT_NAME, ""]
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

    private fun mServiceRecyclerView() {
        mServiceDetailsRecyclerView = mDataBinding.serviceDetailsRecycler
        mAdapterServiceDetails = ServiceDetailsAdapter()
        mServiceDetailsRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        val layoutManager = GridLayoutManager(requireContext(), 3)
        mServiceDetailsRecyclerView.layoutManager = layoutManager
        mServiceDetailsRecyclerView.adapter = mAdapterServiceDetails
    }

    private fun mSlotRecyclerView() {
        mSlotRecyclerView = mDataBinding.slotsRecyclerView
        mAdapterSlot = SlotAdapter()
        mSlotRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        val layoutManager = GridLayoutManager(requireContext(), 4)
        mSlotRecyclerView.layoutManager = layoutManager
        mSlotRecyclerView.adapter = mAdapterSlot
    }


    override fun getBiddingResponse(response: DataBidding) {
        Log.d(TAG, "getBiddingResponse: get the log")
        val formatter1 = DateTimeFormatter.ofPattern("dd MMM yyyy")
        val formattedDate = response.bidRequestedDate?.format(formatter1)
        var event = ArrayList<EventStatusDTO>()
        event.add(EventStatusDTO(formattedDate, getString(R.string.cut_off_date)))
        event.add(
            EventStatusDTO(
                "${response.bidRequestedCount.toString()} ${getString(R.string.bidders)}",
                getString(R.string.request_posted)
            )
        )
        event.add(
            EventStatusDTO(
                "${response.biddingResponseCount.toString()} ${getString(R.string.bidders)}",
                getString(R.string.responses_received)
            )
        )
        mAdapterServiceDetails.refreshItems(event)
        mDataBinding.timeLeftTxt.text =
            response.timeLeft.toString() + " ${getString(R.string.days)}"
        var preferredSlots = ArrayList<String>()
        preferredSlots.addAll(response.preferredTimeSlots)
        mAdapterSlot.refreshItems(preferredSlots)
    }


}