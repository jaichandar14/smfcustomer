package com.smf.customer.view.dashboard

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.smf.customer.R
import com.smf.customer.app.base.BaseActivity
import com.smf.customer.app.base.MyApplication
import com.smf.customer.app.constant.AppConstant
import com.smf.customer.databinding.ActivityDashBoardBinding
import com.smf.customer.view.dashboard.fragment.MainDashBoardFragment

// 3262
class DashBoardActivity : BaseActivity<DashBoardViewModel>() {

    private lateinit var mDataBinding: ActivityDashBoardBinding
    var fragment = Fragment()
    var lastOrientation: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mInitialize()
        if (savedInstanceState == null) {
            lastOrientation = 1
            mainFragment()
        } else {
            Log.d(TAG, "onCreate: else statement")
        }
    }
    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

    private fun mainFragment() {
        val frg = MainDashBoardFragment() //create the fragment instance for the middle fragment
        val manager: FragmentManager =
            supportFragmentManager //create an instance of fragment manager
        val transaction: FragmentTransaction =
            manager.beginTransaction() //create an instance of Fragment-transaction
        transaction.replace(R.id.main_dashboard_ui, frg, "Frag_Bottom_tag")
        transaction.commit()
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putInt("Rotated", lastOrientation)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        lastOrientation = savedInstanceState.getInt(AppConstant.ROTATED)
    }

    private fun mInitialize() {
        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_dash_board)
        viewModel = ViewModelProvider(this)[DashBoardViewModel::class.java]
        mDataBinding.dashdoardviewmodel = viewModel
        mDataBinding.lifecycleOwner = this
        MyApplication.applicationComponent.inject(this)

    }

    override fun onDestroy() {
        super.onDestroy()
    }
}