package com.smf.customer.data.model.request

import com.smf.customer.data.model.response.ResponseDTO

data class AddServicesReqDTO(
    val actualServiceBudget: Any?,
    val allocatedBudget: Any?,
    val bidRequestedCount: Int,
    val biddingCutOffDate: Any?,
    val biddingResponseCount: Int,
    val costingType: String,
    val currencyType: Any?,
    val customerServiceCategory: Any?,
    val defaultServiceCategory: Boolean,
    val estimatedBudget: Any?,
    val eventServiceDescriptionId: Int,
    val eventServiceId: Int,
    val eventServiceStatus: Any?,
    val isServiceRequired: Any?,
    val leadPeriod: Int,
    val remainingBudget: Any?,
    val serviceCategoryId: Int,
    val serviceCloseDate: Any?,
    val serviceDate: Any?,
    val serviceName: String,
    val serviceProviderName: Any?,
    val serviceStartDate: Any?,
    val serviceTemplateIcon: Any?,
    val templateIcon: Any?
) : ResponseDTO()