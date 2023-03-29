package com.smf.customer.view.dashboard.fragment.serviceFragment.servicedetailsdashboard.expandablelist

import com.smf.customer.app.constant.AppConstant

data class ParentData(
    val parentTitle: String? = null,
    var type: Int = AppConstant.PARENT,
    var subList: MutableList<ChildData> = ArrayList(),
    var isExpanded: Boolean = false
)

data class ChildData(val childTitle:String)
