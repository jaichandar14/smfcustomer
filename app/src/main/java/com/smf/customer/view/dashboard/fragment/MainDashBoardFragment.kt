package com.smf.customer.view.dashboard.fragment


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.smf.customer.R
import com.smf.customer.app.base.BaseFragment
import com.smf.customer.app.base.MyApplication
import com.smf.customer.app.constant.AppConstant
import com.smf.customer.databinding.FragmentMainDashBoardBinding
import com.smf.customer.di.sharedpreference.SharedPrefConstant
import com.smf.customer.di.sharedpreference.SharedPrefsHelper
import com.smf.customer.view.dashboard.DashBoardViewModel
import com.smf.customer.view.dashboard.adaptor.EventOverView
import com.smf.customer.view.dashboard.adaptor.ServicesStatus
import com.smf.customer.view.dashboard.adaptor.Upcomingevent
import com.smf.customer.view.dashboard.model.EventStatusDTO
import com.smf.customer.view.dashboard.responsedto.EventStatusData
import com.smf.customer.view.dashboard.responsedto.MyEventData
import com.smf.customer.view.myevents.MyEventsActivity
import javax.inject.Inject


// 3262
class MainDashBoardFragment : BaseFragment<DashBoardViewModel>(),
    DashBoardViewModel.OnServiceClickListener {
    private lateinit var mAdapterEvent: EventOverView
    private lateinit var mAdapterService: ServicesStatus
    private lateinit var mAdapterUpcoming: Upcomingevent
    private lateinit var mEventOverViewRecyclerView: RecyclerView
    private lateinit var mServicesStatusRecyclerView: RecyclerView
    private lateinit var mUpcomingEventsRecyclerView: RecyclerView
    private lateinit var mDataBinding: FragmentMainDashBoardBinding
    private var mEventStatusList = ArrayList<String>()
    private var lastOrientation = 0

    @Inject
    lateinit var sharedPrefsHelper: SharedPrefsHelper

    private var eventStatus: Any = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return inflater.inflate(R.layout.fragment_main_dash_board, container, false)
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(AppConstant.ROTATED, lastOrientation)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 3285 Method to initialize the Di
        mInitialize()
        // 3285 Method for setting Recycler view
        mRecyclerViewIntializer()
        // 3285 Initialize the call back listeners
        viewModel.setOnClickListener(this)
        // 3285 Method to get he screen orientation
        currentOrientation()
        // 3285 Method for get api call
        dashBoardGetApiCall()

        mDataBinding.myEventIcon.setOnClickListener {
            val intent = Intent(requireContext().applicationContext, MyEventsActivity::class.java)
            startActivity(intent)
        }

    }

    private fun dashBoardGetApiCall() {
        viewModel.getEventCount(
            sharedPrefsHelper[SharedPrefConstant.ACCESS_TOKEN, ""],
            sharedPrefsHelper[SharedPrefConstant.USER_ID, ""]
        )
        mEventStatusList.add(AppConstant.APPROVED)
        viewModel.getEventStatus(
            sharedPrefsHelper[SharedPrefConstant.ACCESS_TOKEN, ""],
            sharedPrefsHelper[SharedPrefConstant.USER_ID, ""], mEventStatusList
        )
        viewModel.showLoading.value = true
    }

    private fun currentOrientation() {
        var cutOrientation = resources.configuration.orientation
        Log.d(TAG, "currentOrientation: $cutOrientation")
        if (lastOrientation != cutOrientation) {
            onScreenOrientation(cutOrientation)
            lastOrientation = cutOrientation
        } else {
            onScreenOrientation(cutOrientation)
        }
    }

    private fun onScreenOrientation(cutOrientation: Int) {
        if (cutOrientation == 1) {
            mEventOverviewRecycler(3)
        } else {
            mEventOverviewRecycler(5)
        }

    }

    private fun mRecyclerViewIntializer() {
        mServiceStatusRecycler()
        mUpcomingEventRecycler()
        mAdapterService.refreshItems(getMyEvents())
        mAdapterUpcoming.refreshItems(getServiceCountList())
    }

    // 3285 Method to Set Tab layout
    private fun mEventStatusRecycler(listMyEvents: MyEventData) {
        val tabLayout = mDataBinding.eventStatusTab
        setTab(tabLayout, listMyEvents)
        tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val position = tab.position
                val mEventStatusList = ArrayList<String>()
                Log.d(TAG, "onTabSelected: $position")
                when (position) {
                    0 -> {
                        mEventStatusList.clear()
                        mEventStatusList.add(AppConstant.APPROVED)
                        getEventStatusApi(mEventStatusList)
                    }
                    1 -> {
                        mEventStatusList.clear()
                        mEventStatusList.add(AppConstant.UNDER_REVIEW)
                        mEventStatusList.add(AppConstant.PENDING_ADMIN_APPROVAL)
                        getEventStatusApi(mEventStatusList)
                    }
                    2 -> {
                        mEventStatusList.clear()
                        mEventStatusList.add(AppConstant.NEW)
                        mEventStatusList.add(AppConstant.REVOKED)
                        getEventStatusApi(mEventStatusList)
                    }
                    3 -> {
                        mEventStatusList.clear()
                        mEventStatusList.add(AppConstant.REJECTED)
                        getEventStatusApi(mEventStatusList)
                    }
                    4 -> {
                        mEventStatusList.clear()
                        mEventStatusList.add(AppConstant.CLOSED)
                        getEventStatusApi(mEventStatusList)
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    private fun getEventStatusApi(mEventStatusList: ArrayList<String>) {
        viewModel.getEventStatus(
            sharedPrefsHelper[SharedPrefConstant.ACCESS_TOKEN, ""],
            sharedPrefsHelper[SharedPrefConstant.USER_ID, ""], mEventStatusList
        )
    }

    private fun setTab(tabLayout: TabLayout, listMyEvents: MyEventData) {
        for (i in 0 until 5) {
            val tab1: TabLayout.Tab? = tabLayout.getTabAt(i)
            when (tab1?.position) {
                0 -> {
                    var tvTab1Title = tab1.customView!!.findViewById<View>(R.id.title_text)
                    var tvTab1Value = tab1.customView!!.findViewById<View>(R.id.number_text)
                    (tvTab1Value as TextView).text = listMyEvents.approvedEventsCount.toString()
                    (tvTab1Title as TextView).text = AppConstant.ACTIVE
                    mDataBinding.myEventsCountsTx.text =
                        listMyEvents.approvedEventsCount.toString() +" "+ getString(
                            R.string.active_counts
                        )
                }
                1 -> {
                    var tvTab1Title = tab1.customView!!.findViewById<View>(R.id.title_text)
                    var tvTab1Value = tab1.customView!!.findViewById<View>(R.id.number_text)
                    (tvTab1Value as TextView).text = listMyEvents.pendingEventsCount.toString()
                    (tvTab1Title as TextView).text = AppConstant.PENDING
                    mDataBinding.myEventsCountsTx.text =
                        listMyEvents.pendingEventsCount.toString() +" "+ getString(
                            R.string.pending_counts
                        )
                }
                2 -> {
                    var tvTab1Title = tab1.customView!!.findViewById<View>(R.id.title_text)
                    var tvTab1Value = tab1.customView!!.findViewById<View>(R.id.number_text)
                    (tvTab1Value as TextView).text =
                        listMyEvents.newEventsCount.plus(listMyEvents.revokedEventsCount).toString()
                    (tvTab1Title as TextView).text = AppConstant.DRAFT
                    mDataBinding.myEventsCountsTx.text =
                        listMyEvents.newEventsCount.plus(listMyEvents.revokedEventsCount).toString() +" "+ getString(
                            R.string.draft_counts
                        )
                }
                3 -> {
                    var tvTab1Title = tab1.customView!!.findViewById<View>(R.id.title_text)
                    var tvTab1Value = tab1.customView!!.findViewById<View>(R.id.number_text)
                    (tvTab1Value as TextView).text = listMyEvents.rejectedEventsCount.toString()
                    (tvTab1Title as TextView).text = AppConstant.REJECT
                    mDataBinding.myEventsCountsTx.text =
                        listMyEvents.rejectedEventsCount.toString() +" "+ getString(
                            R.string.reject_counts
                        )
                }
                4 -> {
                    var tvTab1Title = tab1.customView!!.findViewById<View>(R.id.title_text)
                    var tvTab1Value = tab1.customView!!.findViewById<View>(R.id.number_text)
                    (tvTab1Value as TextView).text = listMyEvents.closedEventsCount.toString()
                    (tvTab1Title as TextView).text = AppConstant.CLOSED_TXT
                    mDataBinding.myEventsCountsTx.text =
                        listMyEvents.closedEventsCount.toString() +" "+ getString(
                            R.string.closed_counts
                        )
                }
            }
        }
        viewModel.showLoading.value = false
    }


    private fun mInitialize() {
        mDataBinding =
            DataBindingUtil.setContentView(requireActivity(), R.layout.fragment_main_dash_board)
        viewModel = ViewModelProvider(this)[DashBoardViewModel::class.java]
        mDataBinding.mainDashboardViewModel = viewModel
        mDataBinding.lifecycleOwner = this
        MyApplication.applicationComponent.inject(this)

    }

    private fun mEventOverviewRecycler(i: Int) {
        mEventOverViewRecyclerView = mDataBinding.myEventsRecyclerview
        mAdapterEvent = EventOverView()
        mEventOverViewRecyclerView.layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        val layoutManager = GridLayoutManager(requireActivity(), i)
        mEventOverViewRecyclerView.layoutManager = layoutManager
        mEventOverViewRecyclerView.adapter = mAdapterEvent
    }

    private fun mServiceStatusRecycler() {
        mServicesStatusRecyclerView = mDataBinding.serviceStatusRecylerview
        mAdapterService = ServicesStatus()
        mServicesStatusRecyclerView.layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        mServicesStatusRecyclerView.adapter = mAdapterService
    }

    private fun mUpcomingEventRecycler() {
        mUpcomingEventsRecyclerView = mDataBinding.upcomingEventRecyclerview
        mAdapterUpcoming = Upcomingevent()
        mUpcomingEventsRecyclerView.layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        mUpcomingEventsRecyclerView.adapter = mAdapterUpcoming
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

    // Static data for demo need to implement in future
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

    override fun getMyevetList(listMyEvents: MyEventData) {
        eventStatus = listMyEvents
        mEventStatusRecycler(listMyEvents)
        mDataBinding.myEventsCountsTx.text =
            listMyEvents.approvedEventsCount.toString() + getString(R.string.active_counts)
    }

    override fun getEventStatus(response: EventStatusData) {
        if (response.eventDtos == null) {
            mEventOverviewRecycler(3)
        } else {
            mAdapterEvent.refreshItems(response.eventDtos)
        }

    }
}