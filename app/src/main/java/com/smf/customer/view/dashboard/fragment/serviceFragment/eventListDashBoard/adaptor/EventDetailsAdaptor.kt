package com.smf.customer.view.dashboard.fragment.serviceFragment.eventListDashBoard.adaptor

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.smf.customer.R
import com.smf.customer.app.base.MyApplication
import com.smf.customer.app.constant.AppConstant
import com.smf.customer.databinding.DetailscardviewBinding
import com.smf.customer.di.sharedpreference.SharedPrefConstant
import com.smf.customer.di.sharedpreference.SharedPrefsHelper
import com.smf.customer.view.dashboard.model.EventServiceInfoDTO
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject

class EventDetailsAdaptor : RecyclerView.Adapter<EventDetailsAdaptor.EventDetailsViewHolder>() {

    private var myEventsList = ArrayList<EventServiceInfoDTO>()
    private var onClickListener: OnServiceClickListener? = null

    private val formatter: DateTimeFormatter =
        DateTimeFormatter.ofPattern("MM/dd/yyyy", Locale.ENGLISH)

    init {
        MyApplication.applicationComponent?.inject(this)
    }

    @Inject
    lateinit var sharedPrefsHelper: SharedPrefsHelper

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventDetailsViewHolder =
        EventDetailsViewHolder(
            DetailscardviewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: EventDetailsViewHolder, position: Int) {
        holder.onBind(myEventsList[position])
    }

    override fun getItemCount(): Int {
        return myEventsList.size
    }

    inner class EventDetailsViewHolder(var binding: DetailscardviewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        // Method For Fixing xml views and Values
        fun onBind(myEvents: EventServiceInfoDTO) {
            //  titleText.text = myEvents.title
            binding.status =
                (myEvents.eventServiceStatus == AppConstant.NEW) ||
                        (myEvents.eventServiceStatus == AppConstant.PENDING_ADMIN_APPROVAL)
            binding.modifyStatus =
                (myEvents.eventServiceStatus == AppConstant.NEW) ||
                        (myEvents.eventServiceStatus == AppConstant.PENDING_ADMIN_APPROVAL)
                        || (myEvents.eventServiceStatus == AppConstant.BIDDING_STARTED)
            // 3443
            settingValueUi(myEvents)
            // Delete service
            deleteService(myEvents.eventServiceId.toString(), myEvents.serviceName, myEvents)
        }

        private fun deleteService(
            eventServiceId: String,
            serviceName: String?,
            myEvents: EventServiceInfoDTO
        ) {
            binding.deleteIcon.apply {
                if (myEvents.isServiceRequired) {
                    setOnClickListener {
                        callBackInterface?.nonDeletableService(serviceName)
                    }
                } else {
                    setOnClickListener {
                        callBackInterface?.onClickDelete(eventServiceId, serviceName)
                    }
                }
            }
        }

        private fun settingValueUi(myEvents: EventServiceInfoDTO) {
            // Delete button visiblity binding
            binding.deleteVisibility =
                (myEvents.eventServiceStatus == AppConstant.PENDING_ADMIN_APPROVAL) || (myEvents.eventServiceStatus == AppConstant.BIDDING_STARTED)
            if (myEvents.eventServiceStatus == AppConstant.PENDING_ADMIN_APPROVAL) {
                binding.statusTxt.text =
                    MyApplication.appContext.getString(R.string.waiting_for_aprproval)
                binding.modifyTxt.text =
                    MyApplication.appContext.getString(R.string.view_order_details)
            } else if (myEvents.eventServiceStatus == AppConstant.BIDDING_STARTED) {
                binding.btnStartService.apply {
                    text = MyApplication.appContext.getString(R.string.bidding_in_progress)
                }
                binding.modifyTxt.text =
                    MyApplication.appContext.getString(R.string.view_order_details)
            }
            if (myEvents.biddingCutOffDate != null) {
                val cutDate = LocalDate.parse(myEvents.biddingCutOffDate, formatter)
                val formatter2 = DateTimeFormatter.ofPattern("MMM")
                val dateNo = DateTimeFormatter.ofPattern("dd")
                val cutOffDate = cutDate.format(formatter2)
                val cutOffDay = cutDate.format(dateNo)
                binding.cutoffMonthText.text = cutOffDate
                binding.progressDateNumber.text = cutOffDay
            }
            binding.details = myEvents
            binding.progressBar.progress = myEvents.leadPeriod.toInt() * 10
            binding.eventNameTxt.text = sharedPrefsHelper[SharedPrefConstant.EVENT_NAME, ""]
            binding.btnStartService.apply {
                setOnClickListener {
                    if (text == MyApplication.appContext.getString(R.string.bidding_in_progress)) {
                        callBackInterface?.onClickBidding(myEvents)
                    } else {
                        callBackInterface?.onClickProvideDetails(myEvents)
                    }
                }
            }
            binding.modifyTxt.setOnClickListener {
                if (binding.modifyTxt.text == MyApplication.appContext.getString(R.string.view_order_details)) {
                    callBackInterface?.onClickModifyDetails(myEvents, false)
                } else {
                    callBackInterface?.onClickModifyDetails(myEvents, true)
                }
            }
        }
    }

    //Method For Refreshing Invoices
    @SuppressLint("NotifyDataSetChanged")
    fun refreshItems(invoice: List<EventServiceInfoDTO>) {
        myEventsList.clear()
        myEventsList.addAll(invoice)
        notifyDataSetChanged()
    }

    private var callBackInterface: OnServiceClickListener? = null

    // Initializing Listener Interface
    fun setOnClickListener(listener: OnServiceClickListener) {
        callBackInterface = listener
    }

    // Interface For Invoice Click Listener
    interface OnServiceClickListener {
        fun onClickProvideDetails(listMyEvents: EventServiceInfoDTO)
        fun onClickModifyDetails(listMyEvents: EventServiceInfoDTO, isModify: Boolean)
        fun onClickBidding(listMyEvents: EventServiceInfoDTO)
        fun onClickDelete(eventServiceId: String, serviceName: String?)
        fun nonDeletableService(serviceName: String?)
    }
}