package com.smf.customer.view.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.smf.customer.R
import com.smf.customer.app.base.AppActivity
import com.smf.customer.app.constant.AppConstant

class SplashActivity : AppCompatActivity() {

    companion object {
        fun starter(activity: AppActivity, logout: Boolean = false) {
            var intent = Intent(activity, SplashActivity::class.java)
            intent.putExtra(AppConstant.EXTRA, logout)
            activity.startActivity(intent)
           /*TODO if (activity !is DashboardActivity) {
                activity.finishAffinity()
            }*/
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}