package com.smf.customer.view.dashboard

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.smf.customer.R
import com.smf.customer.app.base.BaseActivity
import com.smf.customer.app.base.MyApplication
import com.smf.customer.app.constant.AppConstant
import com.smf.customer.databinding.ActivityDashBoardBinding
import com.smf.customer.utility.OnBackPressedFragment
import com.smf.customer.view.dashboard.fragment.MainDashBoardFragment
import com.smf.customer.view.dashboard.fragment.serviceFragment.eventListDashBoard.EventsDashBoardFragment
import com.smf.customer.view.dashboard.fragment.serviceFragment.servicedetailsdashboard.ServiceDetailDashboardFragment

// 3262
class DashBoardActivity : BaseActivity<DashBoardViewModel>() {

    private lateinit var mDataBinding: ActivityDashBoardBinding
    private var doubleBackToExitPressedOnce = false
    var lastOrientation: Int = 0
    var frag: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mInitialize()
    }

    // 3452 on back press
    override fun onBackPressed() {
        if (OnBackPressedFragment.tag==getString(R.string.eventDashboard)){
             mainFragment()
            OnBackPressedFragment.tag=null
       }else{
            if (doubleBackToExitPressedOnce) {
                finishAffinity()
                return
            }
            this.doubleBackToExitPressedOnce = true
            // 3452  Need to implement future
            viewModel.showToastMessage( getString(R.string.back_message))
            Handler(Looper.getMainLooper()).postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)
       }
    }


    override fun onStart() {
        super.onStart()

        val intent = intent
        var fragIntent = intent.getStringExtra(AppConstant.ON_EVENT)
        if (fragIntent == AppConstant.ON_EVENT) {
            eventListFragment()
        } else if (fragIntent == AppConstant.ON_SERVICE) {
            serviceDetailsFragment()
        }else{
            mainFragment()
        }


    }

    override fun onPause() {
        val manager: FragmentManager =
            supportFragmentManager //create an instance of fragment manager
        var transaction =
            manager.beginTransaction()
        frag?.let { transaction.remove(it) }?.commit()
        super.onPause()
    }


    fun mainFragment() {
        Log.d(TAG, "mainFragment:  called")
        frag = MainDashBoardFragment() //create the fragment instance for the middle fragment
        val manager: FragmentManager =
            supportFragmentManager //create an instance of fragment manager
        var transaction =
            manager.beginTransaction() //create an instance of Fragment-transaction
        transaction.replace(
            R.id.main_dashboard_ui,
            frag as MainDashBoardFragment,
            "Frag_Bottom_tag"
        )
        transaction.addToBackStack(null)
        transaction.commit()
    }

    fun eventListFragment() {
        Log.d(TAG, "mainFragment:  called")
        //frag = MainDashBoardFragment() //create the fragment instance for the middle fragment
        frag = EventsDashBoardFragment()
        val manager: FragmentManager =
            supportFragmentManager //create an instance of fragment manager
        var transaction =
            manager.beginTransaction() //create an instance of Fragment-transaction
        transaction.replace(
            R.id.main_dashboard_ui,
            frag as EventsDashBoardFragment,
            "Frag_Bottom_tag"
        )
        transaction.addToBackStack(null)
        transaction.commit()
    }

    // 3438 service dashboard call
    private fun serviceDetailsFragment() {
        Log.d(TAG, "mainFragment:  called")
        //frag = MainDashBoardFragment() //create the fragment instance for the middle fragment
        frag = ServiceDetailDashboardFragment()
        val manager: FragmentManager =
            supportFragmentManager //create an instance of fragment manager
        var transaction =
            manager.beginTransaction() //create an instance of Fragment-transaction
        transaction.replace(
            R.id.main_dashboard_ui,
            frag as ServiceDetailDashboardFragment,
            "Frag_Bottom_tag"
        )
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun mInitialize() {
        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_dash_board)
        viewModel = ViewModelProvider(this)[DashBoardViewModel::class.java]
        mDataBinding.dashdoardviewmodel = viewModel
        mDataBinding.lifecycleOwner = this
        MyApplication.applicationComponent?.inject(this)
    }


}