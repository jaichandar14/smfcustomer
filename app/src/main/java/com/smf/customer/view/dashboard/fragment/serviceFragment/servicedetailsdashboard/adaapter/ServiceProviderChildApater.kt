package com.smf.customer.view.dashboard.fragment.serviceFragment.servicedetailsdashboard.adaapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.smf.customer.data.model.response.ServiceProviderBiddingResponseDto
import com.smf.customer.databinding.ServiceDashboardChildRowBinding

class ServiceProviderChildApater :
    RecyclerView.Adapter<ServiceProviderChildApater.ServiceProvidersListChildViewHolder>() {

    private var myEventsList = ArrayList<ServiceProviderBiddingResponseDto>()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ServiceProvidersListChildViewHolder = ServiceProvidersListChildViewHolder(
        ServiceDashboardChildRowBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: ServiceProvidersListChildViewHolder, position: Int) {
        holder.onBind(myEventsList[position], position)

    }

    override fun getItemCount(): Int {
        return myEventsList.size
    }

    inner class ServiceProvidersListChildViewHolder(var view: ServiceDashboardChildRowBinding) :
        RecyclerView.ViewHolder(view.root) {
        private var titleText = view.childTitle
        private var childBranch = view.childSubTitle
        var amount = view.amount

        // Method For Fixing xml views and Values
        fun onBind(myEvents: ServiceProviderBiddingResponseDto, position: Int) {
            titleText.text = myEvents.serviceProviderName
            childBranch.text = myEvents.branchName
            amount.text = myEvents.bidValue.toString()
        }
    }

    //Method For Refreshing Invoices
    @SuppressLint("NotifyDataSetChanged")
    fun refreshItems(invoice: ArrayList<ServiceProviderBiddingResponseDto>) {
        myEventsList.clear()
        myEventsList.addAll(invoice)
        notifyDataSetChanged()
    }
}