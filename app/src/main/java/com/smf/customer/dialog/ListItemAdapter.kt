package com.smf.customer.dialog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.smf.customer.R
import com.smf.customer.app.listener.AdapterOneClickListener
import com.smf.customer.data.model.dto.DialogListItem
import com.smf.customer.databinding.LayoutDialogListItemBinding

class ListItemAdapter(var adapterOneClickListener: AdapterOneClickListener) :
    RecyclerView.Adapter<ListItemAdapter.ListItemViewHolder>() {

    private lateinit var mDialogListItemList: ArrayList<DialogListItem>
    lateinit var mSelectedServicesMap: HashMap<Int?, Boolean?>

    fun setDialogListItemList(
        dialogListItemList: ArrayList<DialogListItem>,
        selectedServicesMap: HashMap<Int?, Boolean?>
    ) {
        mDialogListItemList = dialogListItemList
        mSelectedServicesMap = selectedServicesMap
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemViewHolder {
        val binding =
            LayoutDialogListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListItemViewHolder, position: Int) {
        holder.setData(mDialogListItemList[position], position)
    }

    override fun getItemCount(): Int {
        return mDialogListItemList.size
    }

    inner class ListItemViewHolder(var layout: LayoutDialogListItemBinding) :
        RecyclerView.ViewHolder(layout.root) {
        fun setData(dialogListItem: DialogListItem, position: Int) {
            layout.tvItem.text = dialogListItem.serviceName
            if (mSelectedServicesMap[position] == true) {
                layout.serviceSelectionImage.setImageResource(R.drawable.new_selection)
                layout.serviceSelectionImage.tag = R.drawable.new_selection
            } else {
                layout.serviceSelectionImage.setImageResource(R.drawable.unselect)
                layout.serviceSelectionImage.tag = R.drawable.unselect
            }

            itemView.setOnClickListener {
                updateData(position)
            }
        }

        // Method for update image after onClick
        private fun updateData(position: Int) {
            // Get tag and update icon
            var integer = layout.serviceSelectionImage.tag
            integer = integer ?: 0

            when (integer) {
                R.drawable.unselect -> {
                    layout.serviceSelectionImage.setImageResource(R.drawable.new_selection)
                    layout.serviceSelectionImage.tag = R.drawable.new_selection
                    serviceClickListener?.onServiceClicked(position, true)
                }
                R.drawable.new_selection -> {
                    layout.serviceSelectionImage.setImageResource(R.drawable.unselect)
                    layout.serviceSelectionImage.tag = R.drawable.unselect
                    serviceClickListener?.onServiceClicked(position, false)
                }
                else -> {
                    layout.serviceSelectionImage.setImageResource(R.drawable.unselect)
                    layout.serviceSelectionImage.tag = R.drawable.unselect
                }
            }
        }
    }

    private var serviceClickListener: ServiceClickListener? = null

    // Initializing TimeSlotIconClickListener Interface
    fun setOnClickListener(listener: ServiceClickListener) {
        serviceClickListener = listener
    }

    // Interface For TimeSlot Icon Click
    interface ServiceClickListener {
        fun onServiceClicked(listPosition: Int, status: Boolean)
    }

}