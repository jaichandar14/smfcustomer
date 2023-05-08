package com.smf.customer.view.vieweventdetails

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.smf.customer.R
import com.smf.customer.app.base.BaseActivity
import com.smf.customer.app.base.MyApplication
import com.smf.customer.databinding.ActivityViewEventDetailsBinding

class ViewEventDetailsActivity : BaseActivity<ViewEventDetailsViewModel>() {
    lateinit var binding: ActivityViewEventDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this@ViewEventDetailsActivity,
            R.layout.activity_view_event_details
        )
        viewModel = ViewModelProvider(this)[ViewEventDetailsViewModel::class.java]
        binding.viewEventDetailsViewModel = viewModel
        binding.lifecycleOwner = this@ViewEventDetailsActivity
        MyApplication.applicationComponent?.inject(this)

        init()
    }

    private fun init() {
// TODO Need to next task API implementation
    }
}