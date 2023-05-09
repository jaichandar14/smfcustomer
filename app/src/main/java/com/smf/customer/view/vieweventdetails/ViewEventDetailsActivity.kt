package com.smf.customer.view.vieweventdetails

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smf.customer.R
import com.smf.customer.app.base.BaseActivity
import com.smf.customer.app.base.MyApplication
import com.smf.customer.app.constant.AppConstant
import com.smf.customer.data.model.dto.ViewEventDetails
import com.smf.customer.databinding.ActivityViewEventDetailsBinding
import com.smf.customer.utility.Util
import com.smf.customer.utility.Util.Companion.parcelable
import com.smf.customer.view.vieweventdetails.adaptor.QuesAndAnsAdaptor

class ViewEventDetailsActivity : BaseActivity<ViewEventDetailsViewModel>() {
    lateinit var binding: ActivityViewEventDetailsBinding
    lateinit var quesAndAnsRecycler: RecyclerView
    lateinit var quesAndAnsAdaptor: QuesAndAnsAdaptor

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
        // Initialize variables
        setInitialDetails()
        // Initialize recycler view
        setQuesAndAnsRecycler()
        // Display user details
        setDetailsToUI()
    }

    private fun setQuesAndAnsRecycler() {
        quesAndAnsRecycler = binding.quesRecyclerview
        quesAndAnsAdaptor = QuesAndAnsAdaptor(this@ViewEventDetailsActivity)
        quesAndAnsRecycler.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        quesAndAnsRecycler.adapter = quesAndAnsAdaptor
        // Update question and answers
        viewModel.viewEventDetails?.eventQuestionMetaDataDto?.let { questionnaireWrapperDto ->
            quesAndAnsAdaptor.updateQuesAndAnsList(
                viewModel.getQuesListItem(questionnaireWrapperDto),
                viewModel.getAnswers(questionnaireWrapperDto)
            )
        }
    }

    private fun setDetailsToUI() {
        // Event Icon and Title
        setTitleAndIcon()
        // Event Details
        setEventDetails()
        // Venue Details
        setVenueDetails()
        // Host Details
        setHostDetails()
        // Questions Details
        setQuestionDetails()
    }

    private fun setTitleAndIcon() {
        binding.eventTitle.text = viewModel.viewEventDetails?.eventTitle
        if (viewModel.viewEventDetails?.categoryIcon.isNullOrEmpty()) {
            binding.eventImage.setImageDrawable(getDrawable(R.drawable.custom_button_corner_ok_fade))
        } else {
            Util.setIcon(
                this,
                viewModel.viewEventDetails?.categoryIcon ?: "",
                R.drawable.custom_button_corner_ok_fade,
                binding.eventImage
            )
        }
    }

    private fun setEventDetails() {
        binding.eventNameValue.text = viewModel.viewEventDetails?.eventName
        binding.dateValue.text = viewModel.viewEventDetails?.eventDate
        binding.attendeesValue.text = viewModel.viewEventDetails?.noOfAttendees
    }

    private fun setVenueDetails() {
        binding.address1Value.text = viewModel.viewEventDetails?.address1
        binding.address2Value.text = viewModel.viewEventDetails?.address2
        binding.stateValue.text = viewModel.viewEventDetails?.state
        binding.cityValue.text = viewModel.viewEventDetails?.city
        binding.zipcodeValue.text = viewModel.viewEventDetails?.zipCode
        // Set Visibility
        if (viewModel.viewEventDetails?.address1.isNullOrEmpty()) {
            binding.address1Layout.visibility = View.GONE
            binding.address2Layout.visibility = View.GONE
            binding.stateLayout.visibility = View.GONE
            binding.cityLayout.visibility = View.GONE
            binding.zipcodeLayout.visibility = View.VISIBLE
        } else {
            binding.address1Layout.visibility = View.VISIBLE
            binding.address2Layout.visibility = View.VISIBLE
            binding.stateLayout.visibility = View.VISIBLE
            binding.cityLayout.visibility = View.VISIBLE
            binding.zipcodeLayout.visibility = View.VISIBLE
        }
    }

    private fun setHostDetails() {
        binding.hostNameValue.text = viewModel.viewEventDetails?.name
        binding.hostEmailValue.text = viewModel.viewEventDetails?.emailId
        binding.hostMobileValue.text = viewModel.viewEventDetails?.mobileNumberWithCountryCode
    }

    private fun setQuestionDetails() {
        // Questions recyclerview visibility
        if (viewModel.viewEventDetails?.eventQuestionMetaDataDto?.questionnaireDtos.isNullOrEmpty()) {
            binding.noQuestionsText.visibility = View.VISIBLE
            binding.quesRecyclerview.visibility = View.GONE
        } else {
            binding.noQuestionsText.visibility = View.GONE
            binding.quesRecyclerview.visibility = View.VISIBLE
        }
    }

    private fun setInitialDetails() {
        viewModel.viewEventDetails = intent.parcelable<ViewEventDetails>(AppConstant.EVENT_DATA)
    }
}