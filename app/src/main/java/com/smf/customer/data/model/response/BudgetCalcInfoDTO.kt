package com.smf.customer.data.model.response

data class BudgetCalcInfoDTO(
    val `data`: DataBudget,
    val result: Result,
    val success: Boolean
) : ResponseDTO()

data class DataBudget(
    val currencyType: Any,
    val estimatedEventBudget: Int,
    val eventId: Int,
    val eventServiceDescriptionId: Int,
    val eventServiceId: Int,
    val remainingBudget: Int
)