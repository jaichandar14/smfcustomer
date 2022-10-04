package com.smf.customer.dialog

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.smf.customer.app.listener.AdapterOneClickListener
import com.smf.customer.data.model.dto.DialogListItem
import com.smf.customer.databinding.LayoutDialogListItemBinding

class ListItemAdapter(var adapterOneClickListener: AdapterOneClickListener) :
    RecyclerView.Adapter<ListItemAdapter.ListItemViewHolder>() {
    class ListItemViewHolder(var layout: LayoutDialogListItemBinding) :
        RecyclerView.ViewHolder(layout.root) {
        fun setData(dialogListItem: DialogListItem) {
            layout.tvItem.text = dialogListItem.name
        }
    }

    private lateinit var mDialogListItemList: ArrayList<DialogListItem>
    var selectedPosition = -1
    fun setDialogListItemList(dialogListItemList: ArrayList<DialogListItem>) {
        mDialogListItemList = dialogListItemList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemViewHolder {
        var binding =
            LayoutDialogListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListItemViewHolder, position: Int) {
        holder.itemView.tag = position
        holder.itemView.setOnClickListener {
            adapterOneClickListener.onOneClick(it.tag.toString().toInt())
            selectedPosition = position
            notifyDataSetChanged()
        }
        setBackground(position, holder)

        holder.setData(mDialogListItemList[position])
    }

    fun setBackground(position: Int, holder: ListItemViewHolder) {
        if (selectedPosition == position) {
            holder.layout.cvBackground.setCardBackgroundColor(Color.DKGRAY)
            holder.layout.tvItem.setTextColor(Color.WHITE)
        } else {
            holder.layout.cvBackground.setCardBackgroundColor(Color.WHITE)
            holder.layout.tvItem.setTextColor(Color.BLACK)
        }
    }

    override fun getItemCount(): Int {
        return mDialogListItemList.size
    }
}