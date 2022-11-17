package com.smf.customer.view.dashboard.adaptor

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.smf.customer.R
import com.smf.customer.app.constant.AppConstant
import com.smf.customer.view.dashboard.responsedto.EventDtos
import java.time.LocalDate
import java.time.format.DateTimeFormatter

// 3262
class EventOverView : RecyclerView.Adapter<EventOverView.EvenOverViewViewHolder>() {

    private var myEventsList: ArrayList<EventDtos>? = ArrayList<EventDtos>()
    private var onClickListener: OnServiceClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EvenOverViewViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.my_event_cardview, parent, false)
        return EvenOverViewViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: EvenOverViewViewHolder, position: Int) {
        myEventsList?.get(position)?.let { holder.onBind(it) }

    }

    override fun getItemCount(): Int {
        return myEventsList?.size!!
    }

    inner class EvenOverViewViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private var titleText = view.findViewById<TextView>(R.id.even_name_tx)
        private var dateText = view.findViewById<TextView>(R.id.event_date_tx)

        // Method For Fixing xml views and Values
        @SuppressLint("NewApi")
        fun onBind(myEvents: EventDtos) {
            val formatter = DateTimeFormatter.ofPattern(AppConstant.DATE_FORMAT)
            val date = LocalDate.parse(myEvents.eventDate, formatter)
            val formatter2 = DateTimeFormatter.ofPattern(AppConstant.MonthDate)
            titleText.text = myEvents.eventName
            dateText.text = date.format(formatter2)
        }
    }

    //Method For Refreshing Invoices
    @SuppressLint("NotifyDataSetChanged")
    fun refreshItems(invoice: ArrayList<EventDtos>) {
        myEventsList?.clear()
        myEventsList?.addAll(invoice)
        notifyDataSetChanged()
    }

    // Initializing Listener Interface
    fun setOnClickListener(listener: OnServiceClickListener) {
        onClickListener = listener
    }

    // Interface For Invoice Click Listener
    interface OnServiceClickListener

}