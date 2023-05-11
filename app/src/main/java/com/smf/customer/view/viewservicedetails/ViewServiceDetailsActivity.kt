package com.smf.customer.view.viewservicedetails

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smf.customer.R
import com.smf.customer.app.base.BaseActivity
import com.smf.customer.app.base.MyApplication
import com.smf.customer.app.constant.AppConstant
import com.smf.customer.data.model.response.GetServiceDetailsDTO
import com.smf.customer.databinding.ActivityViewServiceDetailsBinding
import com.smf.customer.utility.Util
import com.smf.customer.view.vieweventdetails.adaptor.QuesAndAnsAdaptor

class ViewServiceDetailsActivity : BaseActivity<ViewServiceDetailsViewModel>(),
    ViewServiceDetailsViewModel.CallBackInterface {
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
        // Get Service Description for view service details
        viewModel.getServiceDescription(viewModel.eventServiceDescriptionId)
    }

    private fun setQuesAndAnsRecycler() {
        quesAndAnsRecycler = binding.quesRecyclerview
        quesAndAnsAdaptor = QuesAndAnsAdaptor(this@ViewServiceDetailsActivity)
        quesAndAnsRecycler.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        quesAndAnsRecycler.adapter = quesAndAnsAdaptor
        viewModel.setCallBackInterface(this)
    }

    override fun updateServiceDetails(getServiceDetailsDTO: GetServiceDetailsDTO) {
        viewModel.showProgress()
        binding.serviceDateValue.text =
            getServiceDetailsDTO.data.eventServiceDescriptionDto.eventServiceDateDto.serviceDate
        binding.bidCutoffValue.text =
            getServiceDetailsDTO.data.eventServiceDescriptionDto.eventServiceDateDto.biddingCutOffDate as String
        binding.estimatedBudgetValue.text =
            getServiceDetailsDTO.data.eventServiceDescriptionDto.eventServiceBudgetDto.estimatedBudget
        binding.serviceRadiusValue.text =
            getServiceDetailsDTO.data.eventServiceDescriptionDto.eventServiceVenueDto.redius
        binding.preferredSlotsValue.text =
            getServiceDetailsDTO.data.eventServiceDescriptionDto.eventServiceDateDto.preferredSlots.joinToString()
        // Update question and answers to adaptor
        setQuestionDetails(getServiceDetailsDTO)
        viewModel.hideProgress()
    }

    private fun setQuestionDetails(getServiceDetailsDTO: GetServiceDetailsDTO) {
        // Questions recyclerview visibility
        if (getServiceDetailsDTO.data.questionnaireWrapperDto.questionnaireDtos.isEmpty()) {
            binding.noQuestionsText.visibility = View.VISIBLE
            binding.quesRecyclerview.visibility = View.GONE
        } else {
            binding.noQuestionsText.visibility = View.GONE
            binding.quesRecyclerview.visibility = View.VISIBLE
        }
        // Update question and answers to adaptor
        quesAndAnsAdaptor.updateQuesAndAnsList(
            viewModel.getQuesListItem(getServiceDetailsDTO.data.questionnaireWrapperDto),
            viewModel.getAnswers(getServiceDetailsDTO.data.questionnaireWrapperDto)
        )
    }

    private fun setInitialDetails() {
        viewModel.serviceTitle =
            "${intent.getStringExtra(AppConstant.SERVICE_NAME) ?: ""} ${getString(R.string.order_summary)}"
        // TODO Need to check API team for this icon
        viewModel.serviceIcon = ""
        // eventServiceDescriptionId for get service details API call
        viewModel.eventServiceDescriptionId = intent.getLongExtra(
            AppConstant.EVENT_SERVICE_DESCRIPTION_ID,
            0
        )
        setTitleAndIcon()
    }

    private fun setTitleAndIcon() {
        binding.serviceTitle.text = viewModel.serviceTitle
        if (viewModel.serviceIcon.isEmpty()) {
            binding.titleImage.setImageDrawable(getDrawable(R.drawable.custom_button_corner_ok_fade))
        } else {
            Util.setIcon(
                this,
                viewModel.serviceIcon,
                R.drawable.custom_button_corner_ok_fade,
                binding.titleImage
            )
        }
    }
}