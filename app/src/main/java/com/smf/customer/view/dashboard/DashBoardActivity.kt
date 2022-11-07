package com.smf.customer.view.dashboard

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.smf.customer.R
import com.smf.customer.app.base.BaseActivity
import com.smf.customer.app.base.MyApplication
import com.smf.customer.databinding.ActivityDashBoardBinding
import com.smf.customer.view.dashboard.fragment.MainDashBoardFragment

// 3262
class DashBoardActivity : BaseActivity<DashBoardViewModel>() {

    private lateinit var mDataBinding: ActivityDashBoardBinding
    var fragment = Fragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mInitialize()
        //mMainDashboardUI()
    }

    private fun mInitialize() {
        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_dash_board)
        viewModel = ViewModelProvider(this)[DashBoardViewModel::class.java]
        mDataBinding.dashdoardviewmodel = viewModel
        mDataBinding.lifecycleOwner = this
        MyApplication.applicationComponent.inject(this)
        val frg = MainDashBoardFragment() //create the fragment instance for the middle fragment
        val manager: FragmentManager =
            supportFragmentManager //create an instance of fragment manager
        val transaction: FragmentTransaction =
            manager.beginTransaction() //create an instance of Fragment-transaction
        transaction.add(R.id.main_dashboard_ui, frg, "Frag_Bottom_tag")
        transaction.commit()

//        fragment = MainDashBoardFragment()
//        loadFragment(fragment)
    }

    private fun loadFragment(fragment: Fragment) {
        // load fragment
        val transaction = supportFragmentManager.beginTransaction()
        //frame_container is your layout name in xml file
        transaction.replace(com.smf.customer.R.id.main_dashboard_ui, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }


}