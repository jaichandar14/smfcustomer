package com.smf.customer.view.dashboard.fragment


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
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
import com.smf.customer.dialog.DialogConstant
import com.smf.customer.view.dashboard.adaptor.EventOverView
import com.smf.customer.view.dashboard.adaptor.ServicesStatus
import com.smf.customer.view.dashboard.adaptor.Upcomingevent
import com.smf.customer.view.dashboard.model.EventStatusDTO
import com.smf.customer.view.dashboard.responsedto.EventStatusData
import com.smf.customer.view.dashboard.responsedto.MyEventData
import com.smf.customer.view.myevents.MyEventsActivity
import javax.inject.Inject


// 3262
class MainDashBoardFragment() : BaseFragment<MainDashBoardViewModel>(),
    MainDashBoardViewModel.OnServiceClickListener {
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
    var count = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return mInitialize(inflater, container).root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(AppConstant.ROTATED, lastOrientation)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 3285 Method to initialize the Di
        //  mInitialize()
        Log.d(TAG, "onViewCreated: MainFragment")
        // 3285 Method for setting Recycler view
        mRecyclerViewIntializer()
        // 3285 Initialize the call back listeners
        viewModel.setOnClickListener(this)
        // 3285 Method to get he screen orientation
        currentOrientation()
        // 3285 Method for Screen Rotation Validation
        onScreenRotation()
        mDataBinding.myEventIcon.setOnClickListener {
            val intent = Intent(requireContext().applicationContext, MyEventsActivity::class.java)
            startActivity(intent)
        }
        if (isAvailable == true) {
            viewModel.getEventCount(
                sharedPrefsHelper[SharedPrefConstant.USER_ID, ""]
            )
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        viewModel.screenRotationValue.value = true
    }

    fun onScreenRotation() {
        if (viewModel.screenRotationValue.value == false) {
            // 3285 Method for get api call
            dashBoardGetApiCall()
            viewModel.listMyEvents.observe(requireActivity(), Observer {
                mDataBinding.myEventsCountsTx.text =
                    it.approvedEventsCount.toString() + " " + getString(R.string.active_counts)
            })
        } else {
            viewModel.listMyEvents.observe(requireActivity(), Observer {
                Log.d(TAG, "doNetworkOperation OnScreenRotation api : called ")
                mEventStatusRecycler(it)
                mDataBinding.myEventsCountsTx.text =
                    it.approvedEventsCount.toString() + " " + getString(R.string.active_counts)
            })
            viewModel.eventStatusData.observe(requireActivity(), Observer {
                mAdapterEvent.refreshItems(it.eventDtos)
            })
        }
    }


    fun dashBoardGetApiCall() {
        Log.d(TAG, "dashBoardGetApiCall: call")
        viewModel.getEventCount(
            sharedPrefsHelper[SharedPrefConstant.USER_ID, ""]
        )
        mEventStatusList.add(AppConstant.APPROVED)
        Log.d(TAG, "doNetworkOperation Dashboard api : called ")
        viewModel.getEventStatus(
            sharedPrefsHelper[SharedPrefConstant.USER_ID, ""], mEventStatusList
        )
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
                count += 1
                if (count == 1) {
                    onTabClick(tab, listMyEvents)
                    count = 0
                }

            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {
                onTabClick(tab, listMyEvents)
            }
        })
    }

    // 3285 On Tab view clicked
    fun onTabClick(tab: TabLayout.Tab, listMyEvents: MyEventData) {
        Log.d(TAG, "onTabClick: $tab")
        val position = tab.position
        val mEventStatusList = ArrayList<String>()
        Log.d(TAG, ": $position")
        onClickTabItems(position, listMyEvents)
    }

    fun onClickTabItems(position: Int, listMyEvents: MyEventData): String {
        return when (position) {
            0 -> {
                if (::mDataBinding.isInitialized) {
                    mEventStatusList.clear()
                    mEventStatusList.add(AppConstant.APPROVED)
                    getEventStatusApi(mEventStatusList)
                    mDataBinding.myEventsCountsTx.text =
                        listMyEvents.approvedEventsCount.toString() + " " + getString(
                            R.string.active_counts
                        )
                    AppConstant.PENDING_ADMIN_APPROVAL

                } else {
                    AppConstant.APPROVED
                }
            }
            1 -> {
                if (::mDataBinding.isInitialized) {
                    mEventStatusList.clear()
                    mEventStatusList.add(AppConstant.UNDER_REVIEW)
                    mEventStatusList.add(AppConstant.PENDING_ADMIN_APPROVAL)
                    getEventStatusApi(mEventStatusList)
                    mDataBinding.myEventsCountsTx.text =
                        listMyEvents.pendingEventsCount.toString() + " " + getString(
                            R.string.pending_counts
                        )
                    AppConstant.PENDING_ADMIN_APPROVAL
                } else {
                    AppConstant.PENDING_ADMIN_APPROVAL
                }
            }
            2 -> {  if (::mDataBinding.isInitialized) {
                mEventStatusList.clear()
                mEventStatusList.add(AppConstant.NEW)
                mEventStatusList.add(AppConstant.REVOKED)
                getEventStatusApi(mEventStatusList)
                mDataBinding.myEventsCountsTx.text =
                    listMyEvents.newEventsCount.plus(listMyEvents.revokedEventsCount)
                        .toString() + " " + getString(
                        R.string.draft_counts
                    )
                AppConstant.NEW
            } else{
                AppConstant.NEW
            }
            }
            3 -> {
                if (::mDataBinding.isInitialized) {
                    mEventStatusList.clear()
                    mEventStatusList.add(AppConstant.REJECTED)
                    getEventStatusApi(mEventStatusList)
                    mDataBinding.myEventsCountsTx.text =
                        listMyEvents.rejectedEventsCount.toString() + " " + getString(
                            R.string.reject_counts
                        )
                    AppConstant.REJECTED
                }else{
                    AppConstant.REJECTED
                }
            }
            4 -> {if (::mDataBinding.isInitialized) {
                mEventStatusList.clear()
                mEventStatusList.add(AppConstant.CLOSED)
                getEventStatusApi(mEventStatusList)
                mDataBinding.myEventsCountsTx.text =
                    listMyEvents.closedEventsCount.toString() + " " + getString(
                        R.string.closed_counts
                    )
                AppConstant.CLOSED
            }else{
                AppConstant.CLOSED
            }
            }
            else -> ""
        }

    }

    private fun getEventStatusApi(mEventStatusList: ArrayList<String>) {
        viewModel.getEventStatus(
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
                }
                1 -> {
                    var tvTab1Title = tab1.customView!!.findViewById<View>(R.id.title_text)
                    var tvTab1Value = tab1.customView!!.findViewById<View>(R.id.number_text)
                    (tvTab1Value as TextView).text = listMyEvents.pendingEventsCount.toString()
                    (tvTab1Title as TextView).text = AppConstant.PENDING

                }
                2 -> {
                    var tvTab1Title = tab1.customView!!.findViewById<View>(R.id.title_text)
                    var tvTab1Value = tab1.customView!!.findViewById<View>(R.id.number_text)
                    (tvTab1Value as TextView).text =
                        listMyEvents.newEventsCount.plus(listMyEvents.revokedEventsCount).toString()
                    (tvTab1Title as TextView).text = AppConstant.DRAFT

                }
                3 -> {
                    var tvTab1Title = tab1.customView!!.findViewById<View>(R.id.title_text)
                    var tvTab1Value = tab1.customView!!.findViewById<View>(R.id.number_text)
                    (tvTab1Value as TextView).text = listMyEvents.rejectedEventsCount.toString()
                    (tvTab1Title as TextView).text = AppConstant.REJECT

                }
                4 -> {
                    var tvTab1Title = tab1.customView!!.findViewById<View>(R.id.title_text)
                    var tvTab1Value = tab1.customView!!.findViewById<View>(R.id.number_text)
                    (tvTab1Value as TextView).text = listMyEvents.closedEventsCount.toString()
                    (tvTab1Title as TextView).text = AppConstant.CLOSED_TXT

                }
            }
        }
        viewModel.showLoading.value = false
    }


    private fun mInitialize(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMainDashBoardBinding {
        mDataBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_main_dash_board, container, false)
        viewModel = ViewModelProvider(this)[MainDashBoardViewModel::class.java]
        mDataBinding.mainDashboardViewModel = viewModel
        mDataBinding.lifecycleOwner = this
        MyApplication.applicationComponent?.inject(this)
        viewModel.noEventVisible.value = false
        return mDataBinding
    }

    fun mEventOverviewRecycler(i: Int) {
        mEventOverViewRecyclerView = mDataBinding.myEventsRecyclerview
        mAdapterEvent = EventOverView()
        mEventOverViewRecyclerView.layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        val layoutManager = GridLayoutManager(requireActivity(), i)
        mEventOverViewRecyclerView.layoutManager = layoutManager
        mEventOverViewRecyclerView.adapter = mAdapterEvent
    }

    fun mServiceStatusRecycler() {
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
        viewModel.listMyEvents.value = listMyEvents
        Log.d(TAG, "doNetworkOperation getMyevetList : called ")
        mEventStatusRecycler(listMyEvents)

    }

    override fun getEventStatus(response: EventStatusData) {
        var cutOrientation = resources.configuration.orientation
        if (response.eventDtos == null) {
            viewModel.noEventVisible.value = true
            if (cutOrientation == 1) {
                mEventOverviewRecycler(3)
            } else {
                mEventOverviewRecycler(5)
            }
        } else {
            viewModel.noEventVisible.value = false
            viewModel.eventStatusData.value = response
            mAdapterEvent.refreshItems(response.eventDtos)
        }
//        viewModel.getEventCount(
//            sharedPrefsHelper[SharedPrefConstant.ACCESS_TOKEN, ""],
//            sharedPrefsHelper[SharedPrefConstant.USER_ID, ""]
//        )


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