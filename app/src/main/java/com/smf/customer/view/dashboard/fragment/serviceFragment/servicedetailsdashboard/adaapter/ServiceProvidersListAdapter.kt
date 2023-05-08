package com.smf.customer.view.dashboard.fragment.serviceFragment.servicedetailsdashboard.adaapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smf.customer.app.base.MyApplication
import com.smf.customer.app.constant.AppConstant
import com.smf.customer.data.model.response.ServiceProviderBiddingResponseDto
import com.smf.customer.databinding.ServiceDashboardParedntRowBinding

class ServiceProvidersListAdapter :
    RecyclerView.Adapter<ServiceProvidersListAdapter.ServiceProvidersListViewHolder>(),
    ServiceProviderChildApater.OnServiceClickListener {

    private var myEventsList = ArrayList<String>()
    private var isExpandable: Boolean? = null
    private lateinit var mBiddingDetailsRecyclerView: RecyclerView
    private lateinit var mAdapterChildProvidersDetails: ServiceProviderChildApater
    var serviceProviderBiddingResponseDtos = ArrayList<ServiceProviderBiddingResponseDto>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ServiceProvidersListViewHolder = ServiceProvidersListViewHolder(
        ServiceDashboardParedntRowBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: ServiceProvidersListViewHolder, position: Int) {
        holder.onBind(myEventsList[position], position)

    }

    override fun getItemCount(): Int {
        return myEventsList.size
    }

    inner class ServiceProvidersListViewHolder(var view: ServiceDashboardParedntRowBinding) :
        RecyclerView.ViewHolder(view.root) {
        private var titleText = view.parentTitle
        private var addClick = view.layout1

        // Method For Fixing xml views and Values
        fun onBind(myEvents: String, position: Int) {
            titleText.text = myEvents
            view.details = position == 0
            addClick.apply {
                if (myEvents == AppConstant.BIDDING_RESPONSE) {
                    setOnClickListener {
                        view.details = view.details != true
                    }
                }
            }
            mBiddingFlowRecycler(view)
        }


    }

    private fun mBiddingFlowRecycler(mDataBinding: ServiceDashboardParedntRowBinding) {
        mBiddingDetailsRecyclerView = mDataBinding.childRecyclerview
        mAdapterChildProvidersDetails =
            ServiceProviderChildApater()
        mBiddingDetailsRecyclerView.layoutManager =
            LinearLayoutManager(MyApplication.appContext, LinearLayoutManager.VERTICAL, false)
        mBiddingDetailsRecyclerView.adapter = mAdapterChildProvidersDetails
        mAdapterChildProvidersDetails.setOnClickListener(this)
        val parentData: Array<String> =
            arrayOf(
                AppConstant.BIDDING_RESPONSE,
                AppConstant.PAYMENT,
                AppConstant.REVIEW_AND_FEEDBACK
            )
        mAdapterChildProvidersDetails.refreshItems(serviceProviderBiddingResponseDtos)
    }

    //Method For Refreshing Invoices
    @SuppressLint("NotifyDataSetChanged")
    fun refreshItems(
        invoice: Array<String>,
        expandable: Boolean,
        serviceProviderBiddingResponseDtos: List<ServiceProviderBiddingResponseDto>
    ) {
        myEventsList.clear()
        myEventsList.addAll(invoice)
        isExpandable = expandable
        this.serviceProviderBiddingResponseDtos.addAll(serviceProviderBiddingResponseDtos)
        notifyDataSetChanged()
    }

    private var callBackInterface: OnServiceClickListener? = null

    // Initializing Listener Interface
    fun setOnClickListener(listener: OnServiceClickListener) {
        callBackInterface = listener
    }

    // Interface For Invoice Click Listener
    interface OnServiceClickListener {
        fun onClickSubmitBtn(myEvents: ServiceProviderBiddingResponseDto)
    }

    override fun onClickSubmitBtn(myEvents: ServiceProviderBiddingResponseDto) {
        callBackInterface?.onClickSubmitBtn(myEvents)
    }

}