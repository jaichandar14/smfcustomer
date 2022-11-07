package com.smf.customer.view.dashboard.fragment


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smf.customer.R
import com.smf.customer.app.base.BaseFragment
import com.smf.customer.app.base.MyApplication
import com.smf.customer.databinding.FragmentMainDashBoardBinding
import com.smf.customer.view.dashboard.DashBoardViewModel
import com.smf.customer.view.dashboard.adaptor.EventOverView
import com.smf.customer.view.dashboard.adaptor.ServicesStatus
import com.smf.customer.view.dashboard.adaptor.Upcomingevent
import com.smf.customer.view.dashboard.model.EventStatusDTO
import com.smf.customer.view.myevents.MyEventsActivity

// 3262
class MainDashBoardFragment : BaseFragment<DashBoardViewModel>(), View.OnClickListener {
    private lateinit var mAdapterEvent: EventOverView
    private lateinit var mAdapterService: ServicesStatus
    private lateinit var mAdapterUpcoming: Upcomingevent
    private lateinit var mEventOverViewRecyclerView: RecyclerView
    private lateinit var mServicesStatusRecyclerView: RecyclerView
    private lateinit var mUpcomingEventsRecyclerView: RecyclerView
    private lateinit var mDataBinding: FragmentMainDashBoardBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return inflater.inflate(R.layout.fragment_main_dash_board, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mInitialize()
        mRecyclerViewIntializer()

        mDataBinding.myEventIcon.setOnClickListener {
            val intent = Intent(requireContext().applicationContext, MyEventsActivity::class.java)
            startActivity(intent)

        }
    }

    private fun mRecyclerViewIntializer() {
        mEventStatusRecycler()
        mEventOverviewRecycler()
        mServiceStatusRecycler()
        mUpcomingEventRecycler()
        //   mAdapter.refreshItems(getServiceCountList())
        mAdapterEvent.refreshItems(getMyEvents())
        mAdapterService.refreshItems(getMyEvents())
        mAdapterUpcoming.refreshItems(getServiceCountList())
    }

    private fun mEventStatusRecycler() {
        //    val tabLayout = mDataBinding.eventStatusTabView as TabLayout
//        // Add five tabs.  Three have icons and two have text titles
//
//        tabLayout.getTabAt(0)?.setCustomView(R.layout.custom_tab_layout_dashboard)
//        val tabOne = LayoutInflater.from(requireContext()).inflate(com.smf.customer.R.layout.custom_tab_layout_dashboard, null)
//
//        tabLayout.getTabAt(0)!!.customView = tabOne
//        tabLayout.getTabAt(1)!!.customView = tabOne
//        tabLayout.getTabAt(2)!!.customView = tabOne
//        tabLayout.getTabAt(3)!!.customView = tabOne
    }


    private fun mInitialize() {
        mDataBinding =
            DataBindingUtil.setContentView(requireActivity(), R.layout.fragment_main_dash_board)
        viewModel = ViewModelProvider(this)[DashBoardViewModel::class.java]
        mDataBinding.mainDashboardViewModel = viewModel
        mDataBinding.lifecycleOwner = this
        MyApplication.applicationComponent.inject(this)

    }

    private fun mEventOverviewRecycler() {
        mEventOverViewRecyclerView = mDataBinding.myEventsRecyclerview
        mAdapterEvent = EventOverView()
        mEventOverViewRecyclerView.layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        val layoutManager = GridLayoutManager(requireActivity(), 3)

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

    override fun onClick(v: View?) {
//        if (view!!.id === R.id.layout1) {
//            select.animate().x(0).setDuration(100)
//            item1.setTextColor(Color.WHITE)
//            item2.setTextColor(def)
//            item3.setTextColor(def)
//        } else if (view!!.id === R.id.item2) {
//            item1.setTextColor(def)
//            item2.setTextColor(Color.WHITE)
//            item3.setTextColor(def)
//            val size: Int = item2.getWidth()
//            select.animate().x(size).setDuration(100)
//        } else if (view!!.id === R.id.item3) {
//            item1.setTextColor(def)
//            item3.setTextColor(Color.WHITE)
//            item2.setTextColor(def)
//            val size: Int = item2.getWidth() * 2
//            select.animate().x(size).setDuration(100)
//        }
    }
}