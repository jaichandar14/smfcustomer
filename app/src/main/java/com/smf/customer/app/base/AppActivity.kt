package com.smf.customer.app.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsControllerCompat
import com.smf.customer.R
import com.smf.customer.utility.CrashlyticsLogger

abstract class AppActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // To find which activity get exception and activity in which order it opened
        crashlyticsClassNameLog()
    }

    open fun setStatusBarColor() {
        window.statusBarColor = getColor(R.color.theme_color)
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = false
    }

    private fun crashlyticsClassNameLog() {
        CrashlyticsLogger.log(this::class.java.simpleName)
    }
}