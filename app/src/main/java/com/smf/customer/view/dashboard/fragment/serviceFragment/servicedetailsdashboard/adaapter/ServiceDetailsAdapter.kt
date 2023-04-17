package com.smf.customer.view.dashboard.fragment.serviceFragment.servicedetailsdashboard.adaapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.smf.customer.R
import com.smf.customer.view.dashboard.model.EventStatusDTO

class ServiceDetailsAdapter : RecyclerView.Adapter<ServiceDetailsAdapter.ServiceDetailsViewHolder>() {

    private var myEventsList = ArrayList<EventStatusDTO>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceDetailsViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.service_dashboard_grid, parent, false)
        return ServiceDetailsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ServiceDetailsViewHolder, position: Int) {
        holder.onBind(myEventsList[position], position)
    }

    override fun getItemCount(): Int {
        return myEventsList.size
    }

    inner class ServiceDetailsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private var titleText = view.findViewById<TextView>(R.id.time_left_txt)

        // Method For Fixing xml views and Values
        fun onBind(myEvents: EventStatusDTO, position: Int) {
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

}