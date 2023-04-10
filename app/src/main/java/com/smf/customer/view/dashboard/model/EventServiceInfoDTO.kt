package com.smf.customer.view.dashboard.model

data class EventServiceInfoDTO(
    val serviceName: String?,
    val biddingCutOffDate: String?,
    val serviceDate: String?,
    val eventServiceId: String,
    val serviceCategoryId: String,
    val leadPeriod: String,
    val eventServiceDescriptionId: String,
    val eventServiceStatus: String?,
    val eventName: String?
)

data class EventVisibility(var submit:Boolean=true,var addService:Boolean=true)