package com.smf.customer.view.dashboard.fragment.serviceFragment.servicedetailsdashboard

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView
import com.smf.customer.R
import com.smf.customer.app.base.BaseFragment
import com.smf.customer.app.base.MyApplication
import com.smf.customer.app.constant.AppConstant
import com.smf.customer.data.model.response.DataBidding
import com.smf.customer.data.model.response.ServiceProviderBiddingResponseDto
import com.smf.customer.databinding.FragmentServiceDetailDashboardBinding
import com.smf.customer.di.sharedpreference.SharedPrefConstant
import com.smf.customer.di.sharedpreference.SharedPrefsHelper
import com.smf.customer.dialog.DialogConstant
import com.smf.customer.dialog.TwoButtonDialogFragment
import com.smf.customer.view.dashboard.DashBoardActivity
import com.smf.customer.view.dashboard.fragment.serviceFragment.eventListDashBoard.adaptor.StatusDetailsAdaptor
import com.smf.customer.view.dashboard.fragment.serviceFragment.servicedetailsdashboard.adaapter.ServiceDetailsAdapter
import com.smf.customer.view.dashboard.fragment.serviceFragment.servicedetailsdashboard.adaapter.ServiceProvidersListAdapter
import com.smf.customer.view.dashboard.fragment.serviceFragment.servicedetailsdashboard.adaapter.SlotAdapter
import com.smf.customer.view.dashboard.fragment.serviceFragment.sharedviewmodel.EventsDashBoardViewModel
import com.smf.customer.view.dashboard.model.EventStatusDTO
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject


class ServiceDetailDashboardFragment(private var serviceDescriptionId: String?) :
    BaseFragment<EventsDashBoardViewModel>(),
    EventsDashBoardViewModel.OnServiceDetailsClickListener,
    ServiceProvidersListAdapter.OnServiceClickListener {
    private lateinit var mDataBinding: FragmentServiceDetailDashboardBinding
    private lateinit var mBiddingDetailsRecyclerView: RecyclerView
    private lateinit var mAdapterBiddingServiceProvidersDetails: ServiceProvidersListAdapter
    private lateinit var mStatusDetailsRecyclerView: RecyclerView
    private lateinit var mAdapterStatusDetails: StatusDetailsAdaptor
    private lateinit var mServiceDetailsRecyclerView: RecyclerView
    private lateinit var mAdapterServiceDetails: ServiceDetailsAdapter
    private lateinit var mSlotRecyclerView: RecyclerView
    private lateinit var mAdapterSlot: SlotAdapter
    private val formatter: DateTimeFormatter =
        DateTimeFormatter.ofPattern("MM/dd/yyyy", Locale.ENGLISH)
    val childDataData1 = java.util.ArrayList<ServiceProviderBiddingResponseDto>()
    private var twoButtonDialog: DialogFragment? = null

    @Inject
    lateinit var sharedPrefsHelper: SharedPrefsHelper
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        mDataBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_service_detail_dashboard, container, false
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
        // 3460 Service Provider list recycler view
        mBiddingFlowRecycler()
        // 3460 Status flow recycler view
        mStatusFlowRecycler()
        // 3460 Bidding details recycler view
        mServiceRecyclerView()
        // 3460 Time slots recycler view
        mSlotRecyclerView()
        // 3460 pull down swipe refresh
        onSwipeRefresh()
        mAdapterStatusDetails.refreshItems(viewModel.setStatusFlowDetails(viewModel.stepThree))
        serviceDescriptionId?.toLong()?.let {
            viewModel.getBiddingResponse(
                sharedPrefsHelper[SharedPrefConstant.EVENT_ID, 0], it
            )
        }
       // viewModel.getBiddingResponse(1352, 101138)
    }

    private fun onSwipeRefresh() {
        mDataBinding.swipeRefresh.setOnRefreshListener {
            // When ever we refresh the activity clearing the expandable list view data
            childDataData1.clear()
            // Reload current fragment
            onReload()
        }
    }

    private fun onReload() {
        // Reload current fragment
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            requireActivity().supportFragmentManager.beginTransaction().detach(this).commitNow();
            requireActivity().supportFragmentManager.beginTransaction().attach(this).commitNow();
        } else {
            requireActivity().supportFragmentManager.beginTransaction().detach(this).attach(this)
                .commit();
        }
    }

    // 3438 Intializing bidding recycler
    private fun mBiddingFlowRecycler() {
        mBiddingDetailsRecyclerView = mDataBinding.exRecycle
        mAdapterBiddingServiceProvidersDetails = ServiceProvidersListAdapter()
        mBiddingDetailsRecyclerView.layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        mBiddingDetailsRecyclerView.adapter = mAdapterBiddingServiceProvidersDetails
        mAdapterBiddingServiceProvidersDetails.setOnClickListener(this)
    }

    private fun setEventServiceDetails() {
        val date = LocalDate.parse(
            sharedPrefsHelper[SharedPrefConstant.EVENT_DATE, ""], formatter
        )
        val formatter1 = DateTimeFormatter.ofPattern("dd MMM yyyy")
        val formattedDate = date.format(formatter1)
        mDataBinding.statusLayout.eventDateTxt.text = formattedDate
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
        val layoutManager = GridLayoutManager(requireContext(), 3)
        mSlotRecyclerView.layoutManager = layoutManager
        mSlotRecyclerView.adapter = mAdapterSlot
    }


    override fun getBiddingResponse(response: DataBidding) {
        setEventServiceDetails()
        val date = LocalDate.parse(
            response.bidRequestedDate, formatter
        )
        val formatter1 = DateTimeFormatter.ofPattern("dd MMM yyyy")
        val formattedDate = date.format(formatter1)

        val bidCutOffDate = LocalDate.parse(
            response.biddingCutOffDate, formatter
        )
        val monthFormatter = DateTimeFormatter.ofPattern("MMM")
        val formattedMonth = bidCutOffDate.format(monthFormatter)
        mDataBinding.apply {
            cutoffMonthText.text = formattedMonth
            progressDateNumber.text = bidCutOffDate.dayOfMonth.toString()
            cutoffMonthText1.text = formattedMonth
            progressDateNumber1.text = bidCutOffDate.dayOfMonth.toString()

        }
        if (tag == AppConstant.ON_SERVICE) {
            mDataBinding.apply {
                dashBoardStatus = true
                timeLeftTxt.text =
                    response.timeLeft.toString() + " " + getString(R.string.days)
            }
        } else {
            mDataBinding.dashBoardStatus = false
        }
        // 3494 this is for cut off date required response
        mAdapterBiddingServiceProvidersDetails.refreshItems(
            viewModel.parentData,
            viewModel.isExpandable,
            settingServiceProviderChildListUI(response)
        )
        // 3494 this is for expandable list of service providers list
        mAdapterServiceDetails.refreshItems(settingServiceAdapterUi(response, formattedDate))
        val preferredSlots = ArrayList<String?>().apply {
            addAll(response.preferredTimeSlots)
        }
        // 3494 this is for preferred time slot list
        mAdapterSlot.refreshItems(preferredSlots)
    }

    private fun settingServiceProviderChildListUI(response: DataBidding): ArrayList<ServiceProviderBiddingResponseDto> {
        return childDataData1.apply { addAll(response.serviceProviderBiddingResponseDtos) }
    }

    private fun settingServiceAdapterUi(
        response: DataBidding, formattedDate: String?
    ): ArrayList<EventStatusDTO> {
        var event = java.util.ArrayList<EventStatusDTO>().apply {
            add(EventStatusDTO(formattedDate, getString(R.string.cut_off_date)))
            add(
                EventStatusDTO(
                    "${response.bidRequestedCount.toString()} ${getString(R.string.bidders)}",
                    getString(R.string.request_posted)
                )
            )
            add(
                EventStatusDTO(
                    "${response.biddingResponseCount.toString()} ${getString(R.string.bidders)}",
                    getString(R.string.responses_received)
                )
            )
            }
        return event
    }

    // 3466 On Click service provider accept button
    override fun onClickSubmitBtn(myEvents: ServiceProviderBiddingResponseDto) {
        Log.d(TAG, "onClickSubmitBtn: $myEvents")
        if (twoButtonDialog == null) {
            twoButtonDialog = TwoButtonDialogFragment.newInstance(
                getString(R.string.you_are_choosing) + " " + myEvents.serviceProviderName + "\n" + getString(
                    R.string.bidding_amount
                ), myEvents.currencyType + myEvents.bidValue, this, false
            )
        }
        if (twoButtonDialog?.isVisible != true) {
            twoButtonDialog?.show(
                requireActivity().supportFragmentManager, DialogConstant.AMOUNT_HIGHLIGHTED_DIALOG
            )
        }
    }

    override fun onNegativeClick(dialogFragment: DialogFragment) {
        super.onNegativeClick(dialogFragment)
        // Dismiss dialog
        dismissEstimationDialog()
    }

    private fun dismissEstimationDialog() {
        twoButtonDialog?.let {
            if (it.isVisible) {
                it.dismiss()
            }
        }
    }

    override fun onPositiveClick(dialogFragment: DialogFragment) {
        super.onPositiveClick(dialogFragment)
        dialogFragment.apply {
            Intent(requireActivity(), DashBoardActivity::class.java).apply {
                this.putExtra(AppConstant.ON_EVENT, AppConstant.QUOTE_ACCEPTED_SERVICE)
                startActivity(this)
                dialogFragment.dismiss()
            }
        }
    }


}