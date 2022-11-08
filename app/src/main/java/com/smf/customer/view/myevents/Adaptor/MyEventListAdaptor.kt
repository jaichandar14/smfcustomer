package com.smf.customer.view.myevents.Adaptor

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.smf.customer.R
import com.smf.customer.view.dashboard.model.EventStatusDTO

// 3262
class MyEventListAdaptor : RecyclerView.Adapter<MyEventListAdaptor.MyEventListViewHolder>() {

    private var myEventsList = ArrayList<EventStatusDTO>()
    var pos: Int? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyEventListViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.myevent_list_carview, parent, false)
        return MyEventListViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyEventListViewHolder, position: Int) {
        holder.onBind(myEventsList[position], position)
        if (pos?.equals(null) == true) {
            holder.tickIcon.visibility = View.INVISIBLE
        } else if (pos == position) {
            holder.tickIcon.visibility = View.VISIBLE
        } else {
            holder.tickIcon.visibility = View.INVISIBLE
        }
    }

    override fun getItemCount(): Int {
        return myEventsList.size
    }

    inner class MyEventListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private var titleText = view.findViewById<TextView>(R.id.even_name_tx)
        var tickIcon = view.findViewById<ImageView>(R.id.tick_icon)
        var cardView = view.findViewById<CardView>(R.id.action_card_view_layout)

        // Method For Fixing xml views and Values
        var zeroCount = 0
        fun onBind(myEvents: EventStatusDTO, position: Int) {
            titleText.text = myEvents.title
            cardView.setOnClickListener {
                Log.d("TAG", "onBind: $position")
                if (position == 0) {
                    zeroCount += 1
                }
                callBackInterface?.onclick(position)
            }


        }

    }

    //Method For Refreshing Invoices
    @SuppressLint("NotifyDataSetChanged")
    fun refreshItems(invoice: List<EventStatusDTO>, position: Int?) {
        myEventsList.clear()
        myEventsList.addAll(invoice)
        Log.d("TAG", "refreshItems: $pos")
        pos = position
        notifyDataSetChanged()
    }

    private var callBackInterface: OnServiceClickListener? = null

    // Initializing Listener Interface
    fun setOnClickListener(listener: OnServiceClickListener) {
        callBackInterface = listener
    }

    // Interface For Invoice Click Listener
    interface OnServiceClickListener {
        fun onclick(position: Int?)
    }

}