package com.smf.customer.dialog

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.recyclerview.widget.RecyclerView
import com.smf.customer.R
import com.smf.customer.app.base.MyApplication
import com.smf.customer.app.listener.AdapterOneClickListener

class EventQusListAdapter(var adapterOneClickListener: AdapterOneClickListener) :
    RecyclerView.Adapter<EventQusListAdapter.ListItemViewHolder>() {

    private lateinit var choiceList: ArrayList<String>
    var selectedPosition = -1
    var questionBtnStatus = ""

    fun setDialogListItemList(
        choiceList: ArrayList<String>,
        selectedPosition: Int?,
        questionBtnStatus: String,
        questionType: String
    ) {
        this.choiceList = choiceList
        this.questionBtnStatus = questionBtnStatus
        Log.d("TAG", "setData: ${choiceList.size} $selectedPosition")
        if (selectedPosition != null) {
            this.selectedPosition = selectedPosition
        } else {
            this.selectedPosition = -1
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemViewHolder {
        // Initialize view
        val view: View = LayoutInflater.from(parent.context).inflate(
            R.layout.radio_btn_list_item, parent, false
        )
        // Pass holder view
        return ListItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListItemViewHolder, position: Int) {
        // Set text on radio button
        holder.setData(choiceList[position])
        // Checked selected radio button
        holder.radioButton.isChecked = position == selectedPosition

        // set listener on radio button
        holder.radioButton.setOnClickListener {
            if (!questionBtnStatus.contains(
                    MyApplication.appContext.resources.getString(R.string.View_order)
                )
            ) {
                Log.d("TAG", "onBindViewHolder: contain")
                // update selected position
                selectedPosition = holder.absoluteAdapterPosition
                // Call listener
                adapterOneClickListener.onOneClick(position)
                // Notify adapter to refresh the radio button
                notifyDataSetChanged()
            }
        }
    }

    override fun getItemId(position: Int): Long {
        // pass position
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        // pass position
        return position
    }

    override fun getItemCount(): Int {
        return choiceList.size
    }

    inner class ListItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Assign variable
        var radioButton: RadioButton = view.findViewById(R.id.radio_button)

        fun setData(choice: String) {
            Log.d("TAG", "setData: choice $choice")
            radioButton.text = choice
            if (questionBtnStatus.contains(
                    MyApplication.appContext.resources.getString(R.string.View_order)
                )
            ) {
                radioButton.isEnabled = false
            }
        }
    }
}