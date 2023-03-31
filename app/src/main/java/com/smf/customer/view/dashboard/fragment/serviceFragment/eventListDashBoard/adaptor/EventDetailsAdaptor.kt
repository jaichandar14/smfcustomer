package com.smf.customer.view.dashboard.fragment.serviceFragment.eventListDashBoard.adaptor

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.smf.customer.app.base.MyApplication
import com.smf.customer.app.constant.AppConstant
import com.smf.customer.databinding.DetailscardviewBinding
import com.smf.customer.di.sharedpreference.SharedPrefConstant
import com.smf.customer.di.sharedpreference.SharedPrefsHelper
import com.smf.customer.view.dashboard.model.EventServiceInfoDTO
import javax.inject.Inject

class EventDetailsAdaptor : RecyclerView.Adapter<EventDetailsAdaptor.EventDetailsViewHolder>() {

    private var myEventsList = ArrayList<EventServiceInfoDTO>()
    private var onClickListener: OnServiceClickListener? = null

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
            binding.status = myEvents.eventServiceStatus == AppConstant.NEW
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