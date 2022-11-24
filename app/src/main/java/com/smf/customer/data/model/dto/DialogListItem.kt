package com.smf.customer.data.model.dto

data class DialogListItem(
    var serviceIcon: String = "",
    var serviceName: String = "",
    var selectedSlotsPositionMap: HashMap<Int, Boolean> = HashMap()
)