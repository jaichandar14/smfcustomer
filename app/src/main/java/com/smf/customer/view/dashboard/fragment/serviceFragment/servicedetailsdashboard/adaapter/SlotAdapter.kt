package com.smf.customer.view.dashboard.fragment.serviceFragment.servicedetailsdashboard.adaapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.smf.customer.R
import com.smf.customer.view.dashboard.model.EventStatusDTO

class SlotAdapter : RecyclerView.Adapter<SlotAdapter.SlotViewHolder>() {

    private var myEventsList = ArrayList<String>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SlotViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.prefered_slot_recyclerview, parent, false)
        return SlotViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SlotViewHolder, position: Int) {
        holder.onBind(myEventsList[position], position)
    }

    override fun getItemCount(): Int {
        return myEventsList.size
    }

    inner class SlotViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private var titleText = view.findViewById<TextView>(R.id.cut_off_date)
        var titleValue = view.findViewById<TextView>(R.id.cut_off_date_tx)

        // Method For Fixing xml views and Values
        fun onBind(myEvents: String, position: Int) {
            titleText.text = myEvents
        }


    }

    //Method For Refreshing Invoices
    @SuppressLint("NotifyDataSetChanged")
    fun refreshItems(invoice: ArrayList<String>) {
        myEventsList.clear()
        myEventsList.addAll(invoice)
        notifyDataSetChanged()
    }

}