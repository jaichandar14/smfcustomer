package com.smf.customer.view.provideservicedetails.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.smf.customer.R

class TimeSlotsAdapter :
    RecyclerView.Adapter<TimeSlotsAdapter.TimeSlotItemViewHolder>() {

    private var timeSlotList = ArrayList<String>()
    var selectedSlotsList = ArrayList<String>()

    @SuppressLint("NotifyDataSetChanged")
    fun setTimeSlotList(
        timeSlotList: ArrayList<String>,
        selectedSlotsList: ArrayList<String>
    ) {
        this.timeSlotList.clear()
        this.timeSlotList = timeSlotList
        this.selectedSlotsList = selectedSlotsList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeSlotItemViewHolder {
        // Initialize view
        val view: View = LayoutInflater.from(parent.context).inflate(
            R.layout.time_slot_view, parent, false
        )
        // Pass holder view
        return TimeSlotItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: TimeSlotItemViewHolder, position: Int) {
        // Set text on radio button
        holder.setData(timeSlotList[position], position)
    }

    override fun getItemCount(): Int {
        return timeSlotList.size
    }

    inner class TimeSlotItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Assign variable
        var timeSlotIcon: ImageView = view.findViewById(R.id.time_slot_icon)
        var timeSlotText: TextView = view.findViewById(R.id.time_slot_text)

        fun setData(timeSlot: String, position: Int) {
            // Initialize image and text
            if (selectedSlotsList.contains(timeSlotList[position])) {
                timeSlotIcon.setImageResource(R.drawable.new_selection)
                timeSlotIcon.tag = R.drawable.new_selection
                timeSlotText.text = timeSlot
            } else {
                timeSlotIcon.setImageResource(R.drawable.unselect)
                timeSlotIcon.tag = R.drawable.unselect
                timeSlotText.text = timeSlot
            }
            // Initialize onClick listener
            itemView.setOnClickListener {
                updateData(position)
            }
        }

        // Method for update image after onClick
        private fun updateData(position: Int) {
            // Get tag and update icon
            var integer = timeSlotIcon.tag
            integer = integer ?: 0

            when (integer) {
                R.drawable.unselect -> {
                    timeSlotIcon.setImageResource(R.drawable.new_selection)
                    timeSlotIcon.tag = R.drawable.new_selection
                    timeSlotIconOnClickListener?.onSlotClicked(position, true)
                }
                R.drawable.new_selection -> {
                    timeSlotIcon.setImageResource(R.drawable.unselect)
                    timeSlotIcon.tag = R.drawable.unselect
                    timeSlotIconOnClickListener?.onSlotClicked(position, false)
                }
                else -> {
                    timeSlotIcon.setImageResource(R.drawable.unselect)
                    timeSlotIcon.tag = R.drawable.unselect
                }
            }
        }
    }

    private var timeSlotIconOnClickListener: TimeSlotIconClickListener? = null

    // Initializing TimeSlotIconClickListener Interface
    fun setOnClickListener(listener: TimeSlotIconClickListener) {
        timeSlotIconOnClickListener = listener
    }

    // Interface For TimeSlot Icon Click
    interface TimeSlotIconClickListener {
        fun onSlotClicked(listPosition: Int, status: Boolean)
    }
}