package com.smf.customer.view.dashboard.fragment.serviceFragment.eventListDashBoard.adaptor

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
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
        // private var titleText = view.findViewById<TextView>(R.id.event_title_text)

        // Method For Fixing xml views and Values
        fun onBind(myEvents: EventServiceInfoDTO) {
            //  titleText.text = myEvents.title
            binding.status =
                (myEvents.eventServiceStatus == AppConstant.NEW) || (myEvents.eventServiceStatus == AppConstant.PENDING_ADMIN_APPROVAL)

            // 3443
            settingValueUi(myEvents)

    }

        private fun settingValueUi(myEvents: EventServiceInfoDTO) {
             if (myEvents.eventServiceStatus == AppConstant.PENDING_ADMIN_APPROVAL) {
                binding.statusTxt.text =
                    MyApplication.appContext.getString(R.string.waiting_for_aprproval)
            }
            if (myEvents.biddingCutOffDate != null)
            {
                val cutDate = LocalDate.parse(myEvents.biddingCutOffDate, formatter)
                val formatter2 = DateTimeFormatter.ofPattern("MMM")
                val cutOffDate = cutDate.format(formatter2)
                binding.cutoffMonthText.text = cutOffDate
            }
            binding.details = myEvents
            binding.progressBar.progress = myEvents.leadPeriod.toInt() * 10
            binding.eventNameTxt.text = sharedPrefsHelper[SharedPrefConstant.EVENT_NAME, ""]
            binding.btnStartService.setOnClickListener {
                callBackInterface?.onClickProvideDetails(myEvents)
                Log.d("TAG", "onBind: ${myEvents.serviceName}")
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
    }
}