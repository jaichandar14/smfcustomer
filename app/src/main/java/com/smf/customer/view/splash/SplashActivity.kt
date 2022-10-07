package com.smf.customer.view.splash

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.smf.customer.R
import com.smf.customer.app.base.AppActivity
import com.smf.customer.app.base.MyApplication
import com.smf.customer.app.constant.AppConstant
import com.smf.customer.di.DaggerAppComponent
import com.smf.customer.di.sharedpreference.SharedPrefsHelper
import dagger.android.AndroidInjection
import javax.inject.Inject

class SplashActivity : AppCompatActivity() {

    companion object {
        fun starter(activity: AppActivity, logout: Boolean = false) {
            val intent = Intent(activity, SplashActivity::class.java)
            intent.putExtra(AppConstant.EXTRA, logout)
            activity.startActivity(intent)
            /*TODO if (activity !is DashboardActivity) {
                 activity.finishAffinity()
             }*/
        }
    }

    @Inject
    lateinit var sharedPrefsHelper: SharedPrefsHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        MyApplication.applicationComponent.inject(this)
        if (::sharedPrefsHelper.isInitialized){
            Log.d("TAG", "onCreate: yes")
        }else{
            Log.d("TAG", "onCreate: no")
        }
    }
}