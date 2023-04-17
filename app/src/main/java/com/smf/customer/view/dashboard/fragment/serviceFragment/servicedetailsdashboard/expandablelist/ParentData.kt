package com.smf.customer.view.dashboard.fragment.serviceFragment.servicedetailsdashboard.expandablelist

import com.smf.customer.app.constant.AppConstant
import com.smf.customer.data.model.response.ServiceProviderBiddingResponseDto
import java.math.BigDecimal

data class ParentData(
    val parentTitle: String? = null,
    var type: Int = AppConstant.PARENT,
    var subList: ArrayList<ServiceProviderBiddingResponseDto> = ArrayList(),
    var isExpanded: Boolean = false
)
//val childTitle:String,
data class ChildData(
                     val childTitle: String,

                     val bidValue: BigDecimal,

                     val currencyType: String,

                     val branchName: String,

                     val costingType: String,)
