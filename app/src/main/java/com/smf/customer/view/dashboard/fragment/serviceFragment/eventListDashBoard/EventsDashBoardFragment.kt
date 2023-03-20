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
import com.smf.customer.data.model.response.GetEventServiceDataDto
import com.smf.customer.databinding.FragmentEventsDashBoardBinding
import com.smf.customer.di.sharedpreference.SharedPrefConstant
import com.smf.customer.di.sharedpreference.SharedPrefsHelper
import com.smf.customer.view.dashboard.fragment.serviceFragment.eventListDashBoard.adaptor.EventDetailsAdaptor
import com.smf.customer.view.dashboard.model.EventServiceInfoDTO
import com.smf.customer.view.eventDetails.EventDetailsActivity
import com.smf.customer.view.provideservicedetails.ProvideServiceDetailsActivity
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject

class EventsDashBoardFragment : BaseFragment<EventsDashBoardViewModel>(),
    EventsDashBoardViewModel.OnServiceClickListener,
    EventDetailsAdaptor.OnServiceClickListener {

    @Inject
    lateinit var sharedPrefsHelper: SharedPrefsHelper
    private lateinit var mDataBinding: FragmentEventsDashBoardBinding
    private lateinit var mEventDetailsRecyclerView: RecyclerView
    private lateinit var mAdapterEventDetails: EventDetailsAdaptor
    private var eventServiceDetails = ArrayList<EventServiceInfoDTO>()
    private val formatter: DateTimeFormatter =
        DateTimeFormatter.ofPattern("MM/dd/yyyy", Locale.ENGLISH)

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
        // 3420 Initialize the call back listeners
        viewModel.setOnClickListener(this)
        // 3426 Initialize data in Ui
        setEventServiceDetails()
        mDataBinding.addServiceIcon.setOnClickListener {
            val intent = Intent(requireActivity(), ProvideServiceDetailsActivity::class.java)
            startActivity(intent)
//            val list =
//                ArrayList<DialogListItem>()
//            list.add(DialogListItem("1", "Venue"))
//            list.add(DialogListItem("1", "Venue"))
//            list.add(DialogListItem("1", "Venue"))
//            list.add(DialogListItem("1", "Venue"))
//
//            MultipleSelectionListDialog.newInstance(
//                getString(R.string.service_list),
//                list, this, true, R.string.save_services,
//                R.string.cancel
//            )
//                .show(requireActivity().supportFragmentManager, "MultipleSelectionListDialog")
        }
        onClickViewDetails()
        // 3426 getEventServiceInfo api call
        viewModel.getEventServiceInfo(sharedPrefsHelper[SharedPrefConstant.EVENT_ID, 1218])
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

    private fun onClickViewDetails() {
        mDataBinding.statusLayout.eventViewDetails.setOnClickListener {
            val intent =
                Intent(requireContext().applicationContext, EventDetailsActivity::class.java)
            intent.putExtra(AppConstant.EVENT_DASH_BOARD, AppConstant.EVENT_DASH_BOARD)
            startActivity(intent)
        }
    }

    private fun mRecyclerViewIntializer() {
        mEventDetailsRecycler()
        mAdapterEventDetails.refreshItems(
            getServiceList()
        )
    }

    private fun mEventDetailsRecycler() {
        mEventDetailsRecyclerView = mDataBinding.eventDetailsRecyclerview
        mAdapterEventDetails = EventDetailsAdaptor()
        mEventDetailsRecyclerView.layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        mEventDetailsRecyclerView.adapter = mAdapterEventDetails
    }

    override fun getEventServiceInfo(listMyEvents: GetEventServiceDataDto) {
        Log.d(TAG, "getEventServiceInfo: $listMyEvents")
        listMyEvents.eventServiceDtos.forEach {
            if (!it.serviceDate.isNullOrEmpty()) {
                val date = LocalDate.parse(it.serviceDate, formatter)
                val formatter1 = DateTimeFormatter.ofPattern("dd MMM")
                val formattedDate = date.format(formatter1)
                eventServiceDetails.add(
                    EventServiceInfoDTO(
                        it.serviceName,
                        it.biddingCutOffDate,
                        formattedDate,
                        it.eventServiceId,
                        it.serviceCategoryId,
                        it.leadPeriod
                    )
                )
            } else {
                eventServiceDetails.add(
                    EventServiceInfoDTO(
                        it.serviceName,
                        it.biddingCutOffDate,
                        getString(R.string.na),
                        it.eventServiceId,
                        it.serviceCategoryId,
                        it.leadPeriod
                    )
                )
            }
        }
        mAdapterEventDetails.refreshItems(getServiceList())
    }

    // 3426 service list
    private fun getServiceList(): ArrayList<EventServiceInfoDTO> {
        return eventServiceDetails
    }

    // 3426 service event details on click provide details button
    override fun onClickProvideDetails(listMyEvents: EventServiceInfoDTO) {
//        val intent = Intent(requireActivity(), ProvideServiceDetailsActivity::class.java)
//        startActivity(intent)
    }
}