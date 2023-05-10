package com.smf.customer.view.viewservicedetails

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smf.customer.R
import com.smf.customer.app.base.BaseActivity
import com.smf.customer.app.base.MyApplication
import com.smf.customer.databinding.ActivityViewServiceDetailsBinding
import com.smf.customer.view.vieweventdetails.adaptor.QuesAndAnsAdaptor

class ViewServiceDetailsActivity : BaseActivity<ViewServiceDetailsViewModel>() {
    lateinit var binding: ActivityViewServiceDetailsBinding
    lateinit var quesAndAnsRecycler: RecyclerView
    lateinit var quesAndAnsAdaptor: QuesAndAnsAdaptor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this@ViewServiceDetailsActivity,
            R.layout.activity_view_service_details
        )
        viewModel = ViewModelProvider(this)[ViewServiceDetailsViewModel::class.java]
        binding.viewServiceDetailsViewModel = viewModel
        binding.lifecycleOwner = this@ViewServiceDetailsActivity
        MyApplication.applicationComponent?.inject(this)

        init()
    }

    private fun init() {
        // Initialize variables
        setInitialDetails()
        // Initialize recycler view
        setQuesAndAnsRecycler()
    }

    private fun setQuesAndAnsRecycler() {
        quesAndAnsRecycler = binding.quesRecyclerview
        quesAndAnsAdaptor = QuesAndAnsAdaptor(this@ViewServiceDetailsActivity)
        quesAndAnsRecycler.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        quesAndAnsRecycler.adapter = quesAndAnsAdaptor
//        // Update question and answers
//        quesAndAnsAdaptor.updateQuesAndAnsList(
//            questions,
//            answers
//        )
    }

    private fun setInitialDetails() {
//        TODO next task
    }

}