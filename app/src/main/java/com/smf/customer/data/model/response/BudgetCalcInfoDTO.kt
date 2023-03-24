package com.smf.customer.data.model.response

import java.math.BigDecimal

data class BudgetCalcInfoDTO(
    val `data`: DataBudget,
    val result: Result,
    val success: Boolean
) : ResponseDTO()

data class DataBudget(
    val currencyType: String,
    val estimatedEventBudget: BigDecimal,
    val eventId: Int,
    val eventServiceDescriptionId: Long,
    val eventServiceId: Int,
    val remainingBudget: BigDecimal
)