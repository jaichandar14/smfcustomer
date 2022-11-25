package com.smf.customer.view.dashboard.fragment.serviceFragment.eventListDashBoard.adaptor

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.smf.customer.R
import com.smf.customer.view.dashboard.model.EventStatusDTO

class EventDetailsAdaptor : RecyclerView.Adapter<EventDetailsAdaptor.EventDetailsViewHolder>() {

    private var myEventsList = ArrayList<EventStatusDTO>()
    private var onClickListener: OnServiceClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventDetailsViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.detailscardview, parent, false)
        return EventDetailsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: EventDetailsViewHolder, position: Int) {
        holder.onBind(myEventsList[position])

    }

    override fun getItemCount(): Int {
        return myEventsList.size
    }

    inner class EventDetailsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private var titleText = view.findViewById<TextView>(R.id.event_title_text)

        // Method For Fixing xml views and Values
        fun onBind(myEvents: EventStatusDTO) {
            titleText.text = myEvents.title


        }
    }

    //Method For Refreshing Invoices
    @SuppressLint("NotifyDataSetChanged")
    fun refreshItems(invoice: List<EventStatusDTO>) {
        myEventsList.clear()
        myEventsList.addAll(invoice)
        notifyDataSetChanged()
    }

    // Initializing Listener Interface
    fun setOnClickListener(listener: OnServiceClickListener) {
        onClickListener = listener
    }

    // Interface For Invoice Click Listener
    interface OnServiceClickListener

}