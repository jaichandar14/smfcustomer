package com.smf.customer.app.base

import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsControllerCompat
import com.smf.customer.R

abstract class AppActivity : AppCompatActivity() {

    open fun setStatusBarColor() {
        window.statusBarColor = getColor(R.color.theme_color)
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = false
    }
}