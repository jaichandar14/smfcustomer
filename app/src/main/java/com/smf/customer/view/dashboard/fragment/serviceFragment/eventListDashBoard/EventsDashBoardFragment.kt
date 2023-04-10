package com.smf.customer.view.dashboard.fragment.serviceFragment.eventListDashBoard


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView
import com.smf.customer.R
import com.smf.customer.app.base.BaseFragment
import com.smf.customer.app.base.MyApplication
import com.smf.customer.app.constant.AppConstant
import com.smf.customer.app.listener.DialogOneButtonListener
import com.smf.customer.data.model.response.EventServiceDtos
import com.smf.customer.data.model.response.GetEventServiceDataDto
import com.smf.customer.databinding.FragmentEventsDashBoardBinding
import com.smf.customer.di.sharedpreference.SharedPrefConstant
import com.smf.customer.di.sharedpreference.SharedPrefsHelper
import com.smf.customer.dialog.OneButtonDialogFragment
import com.smf.customer.utility.OnBackPressedFragment
import com.smf.customer.view.addServices.AddServiceActivity
import com.smf.customer.view.dashboard.DashBoardActivity
import com.smf.customer.view.dashboard.fragment.serviceFragment.eventListDashBoard.adaptor.EventDetailsAdaptor
import com.smf.customer.view.dashboard.fragment.serviceFragment.eventListDashBoard.adaptor.StatusDetailsAdaptor
import com.smf.customer.view.dashboard.fragment.serviceFragment.sharedviewmodel.EventsDashBoardViewModel
import com.smf.customer.view.dashboard.model.EventServiceInfoDTO
import com.smf.customer.view.dashboard.model.EventVisibility
import com.smf.customer.view.eventDetails.EventDetailsActivity
import com.smf.customer.view.provideservicedetails.ProvideServiceDetailsActivity
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject


class EventsDashBoardFragment : BaseFragment<EventsDashBoardViewModel>(),
    EventsDashBoardViewModel.OnServiceClickListener,
    EventDetailsAdaptor.OnServiceClickListener, DialogOneButtonListener{


    @Inject
    lateinit var sharedPrefsHelper: SharedPrefsHelper
    private lateinit var mDataBinding: FragmentEventsDashBoardBinding
    private lateinit var mEventDetailsRecyclerView: RecyclerView
    private lateinit var mStatusDetailsRecyclerView: RecyclerView
    private lateinit var mAdapterEventDetails: EventDetailsAdaptor
    private lateinit var mAdapterStatusDetails: StatusDetailsAdaptor
    private var eventServiceDetails = ArrayList<EventServiceInfoDTO>()
    private val formatter: DateTimeFormatter =
        DateTimeFormatter.ofPattern("MM/dd/yyyy", Locale.ENGLISH)
    private var provideDetailsCount: Int? = null
    private var servicesName = ArrayList<String>()

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
        mRecyclerViewIntializer()
        // 3420 Initialize the call back listeners
        viewModel.setOnClickListener(this)
        // 3429 Initialize the call back listeners
        mAdapterEventDetails.setOnClickListener(this)
        // 3426 Initialize data in Ui
        setEventServiceDetails()
        mDataBinding.addServiceIcon.setOnClickListener {
            Intent(requireActivity(), AddServiceActivity::class.java).apply {
                putStringArrayListExtra(AppConstant.SERVICE_NAME_LIST, servicesName)
                putExtra(
                    AppConstant.EVENT_ID,
                    sharedPrefsHelper[SharedPrefConstant.EVENT_ID, 0]
                )
                putExtra(
                    AppConstant.TEMPLATE_ID,
                    sharedPrefsHelper[SharedPrefConstant.TEMPLATE_ID, 0]
                )
                startActivity(this)
            }
        }
        onClickViewDetails()
        // 3426 getEventServiceInfo api call
        viewModel.getEventServiceInfo(sharedPrefsHelper[SharedPrefConstant.EVENT_ID, 1218])
        // 3439 onSubmit btn click for put api cal
        onSubmitBtnClick()
        OnBackPressedFragment.tag=getString(R.string.eventDashboard)
    }

    private fun onSubmitBtnClick() {
        mDataBinding.saveBtn.setOnClickListener {
            if (provideDetailsCount != 0) {
                OneButtonDialogFragment.newInstance(
                    "message",
                    getString(R.string.the_service) +
                            " $servicesName " +
                            getString(R.string.before_provide_details),
                    "ok",
                    this,
                    false
                ).show(requireActivity().supportFragmentManager, "")

            } else {
                viewModel.sendForApproval(sharedPrefsHelper[SharedPrefConstant.EVENT_ID, 1218])
            }

        }
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
        mStatusFlowRecycler()
    }

    private fun mEventDetailsRecycler() {
        mEventDetailsRecyclerView = mDataBinding.eventDetailsRecyclerview
        mAdapterEventDetails = EventDetailsAdaptor()
        mEventDetailsRecyclerView.layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        mEventDetailsRecyclerView.adapter = mAdapterEventDetails
    }

    private fun mStatusFlowRecycler() {
        mStatusDetailsRecyclerView = mDataBinding.statusLayout.stepperListview
        val layoutManager =
            LinearLayoutManager(requireContext(), OrientationHelper.HORIZONTAL, false)
        mStatusDetailsRecyclerView.layoutManager = layoutManager
        mAdapterStatusDetails = StatusDetailsAdaptor()
        mStatusDetailsRecyclerView.adapter = mAdapterStatusDetails
    }


    override fun getEventServiceInfo(listMyEvents: GetEventServiceDataDto) {
        Log.d(TAG, "getEventServiceInfo: $listMyEvents")
        setEventStatus(listMyEvents.eventStatus, listMyEvents.eventTrackStatus)
        provideDetailsCount = listMyEvents.eventServiceCountsDto.provideOrderDetails
        listMyEvents.eventServiceDtos.forEach {
            if (!it.serviceDate.isNullOrEmpty()) {
                val date = LocalDate.parse(it.serviceDate, formatter)
                val formatter1 = DateTimeFormatter.ofPattern("dd MMM")
                val formattedDate = date.format(formatter1)
                addEventServiceDetails(it, formattedDate)
            } else {
                addEventServiceDetails(it, getString(R.string.na))
            }
        }
        mAdapterEventDetails.refreshItems(getServiceList())
        // Update selected service name
        eventServiceDetails.forEach {
            it.serviceName?.let { it1 -> servicesName.add(it1) }
        }
    }

    override fun sendForApproval() {
        viewModel.sendEventTrackStatus(
            sharedPrefsHelper[SharedPrefConstant.EVENT_ID, 0], getString(
                R.string.admin_approve
            )
        )
    }

    override fun sendForTrackStatus() {

        if (provideDetailsCount == 0) {
            OneButtonDialogFragment.newInstance(
                "message",
                getString(R.string.admin_approval_ifo),
                "ok",
                this,
                false
            ).show(requireActivity().supportFragmentManager, "")
        }
    }

    private fun setEventStatus(eventStatus: String, eventTrackStatus: String) {
        if (eventStatus == "NEW" || eventTrackStatus == "Add/Remove Services" || eventTrackStatus == "Order Details") {
            mAdapterStatusDetails.refreshItems(viewModel.setStatusFlowDetails(viewModel.stepOne))
            mDataBinding.visibilityBtn = EventVisibility(submit = true, addService = true)
        } else if (eventStatus == "PENDING ADMIN APPROVAL" && eventTrackStatus == "Admin Approval") {
            mAdapterStatusDetails.refreshItems(viewModel.setStatusFlowDetails(viewModel.stepTwo))
            mDataBinding.visibilityBtn = EventVisibility(submit = false, addService = false)
        } else if (eventStatus == AppConstant.APPROVED) {
            mAdapterStatusDetails.refreshItems(viewModel.setStatusFlowDetails(viewModel.stepTwo))
            mDataBinding.visibilityBtn = EventVisibility(submit = false, addService = false)
        }

    }

    private fun addEventServiceDetails(it: EventServiceDtos, formattedDate: String) {
        eventServiceDetails.add(
            EventServiceInfoDTO(
                it.serviceName,
                it.biddingCutOffDate,
                formattedDate,
                it.eventServiceId,
                it.serviceCategoryId,
                it.leadPeriod,
                it.eventServiceDescriptionId,
                it.eventServiceStatus,
                sharedPrefsHelper[SharedPrefConstant.EVENT_NAME, ""],
            )
        )
    }

    // 3426 service list
    private fun getServiceList(): ArrayList<EventServiceInfoDTO> {
        return eventServiceDetails
    }

    // 3426 service event details on click provide details button
    override fun onClickProvideDetails(listMyEvents: EventServiceInfoDTO) {
        Intent(requireActivity(), ProvideServiceDetailsActivity::class.java).apply {
            this.putExtra(
                AppConstant.EVENT_ID,
                sharedPrefsHelper[SharedPrefConstant.EVENT_ID, ""]
            )
            this.putExtra(AppConstant.SERVICE_CATEGORY_ID, listMyEvents.serviceCategoryId)
            this.putExtra(AppConstant.EVENT_SERVICE_ID, listMyEvents.eventServiceId)
            this.putExtra(
                AppConstant.EVENT_SERVICE_DESCRIPTION_ID,
                listMyEvents.eventServiceDescriptionId
            )
            this.putExtra(AppConstant.LEAD_PERIOD, listMyEvents.leadPeriod)
            startActivity(this)
        }
    }

    override fun onPositiveClick(dialogFragment: DialogFragment) {
        super.onPositiveClick(dialogFragment)
        Intent(requireActivity(), DashBoardActivity::class.java).apply {
            this.putExtra(AppConstant.ON_EVENT, AppConstant.ON_EVENT)
            startActivity(this)
        }
    }
}