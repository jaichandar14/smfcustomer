package com.smf.customer.view.dashboard.adaptor

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.smf.customer.R
import com.smf.customer.view.dashboard.model.EventStatusDTO

// 3262
class EventStatus : RecyclerView.Adapter<EventStatus.EventStatusViewHolder>() {

    private var myEventsList = ArrayList<EventStatusDTO>()
    private var onClickListener: OnServiceClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventStatusViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.event_status, parent, false)
        return EventStatusViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: EventStatusViewHolder, position: Int) {
        holder.onBind(myEventsList[position],position)


    }

    override fun getItemCount(): Int {
        return myEventsList.size
    }

    inner class EventStatusViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private var titleText = view.findViewById<TextView>(R.id.title_text)
        // Method For Fixing xml views and Values
        fun onBind(myEvents: EventStatusDTO, position: Int) {
            titleText.text = myEvents.title
            Log.d("TAG", "onBind jai: $myEvents $position")
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