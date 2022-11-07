package com.smf.customer.view.dashboard.adaptor

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.smf.customer.R
import com.smf.customer.view.dashboard.model.EventStatusDTO

// 3262
class ServicesStatus: RecyclerView.Adapter<ServicesStatus.ServicesStatusViewHolder>() {

    private var myEventsList = ArrayList<EventStatusDTO>()
    private var onClickListener: OnServiceClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServicesStatusViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.service_status_cardview, parent, false)
        return ServicesStatusViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ServicesStatusViewHolder, position: Int) {
        holder.onBind(myEventsList[position])

    }

    override fun getItemCount(): Int {
        return myEventsList.size
    }

    inner class ServicesStatusViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private var titleText = view.findViewById<TextView>(R.id.actions_list)
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