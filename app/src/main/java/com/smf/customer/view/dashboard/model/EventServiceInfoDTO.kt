package com.smf.customer.view.dashboard.model

data class EventServiceInfoDTO(
    val serviceName: String?,
    val biddingCutOffDate: String?,
    val serviceDate: String?,
    val eventServiceId: Int,
    val serviceCategoryId: Int,
    val leadPeriod: Long,
    val eventServiceDescriptionId: Long,
    val eventServiceStatus: String?,
    val eventName: String?,
    val isServiceRequired:Boolean,
)

data class EventVisibility(var submit:Boolean=true,var addService:Boolean=true)