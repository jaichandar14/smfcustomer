package com.smf.customer.data.model.response

data class BudgetCalcResDTO(
    val `data`: DataRes,
    val result: Result,
    val success: Boolean
): ResponseDTO()

data class DataRes(
    val docId: Any,
    val key: Int,
    val message: Any,
    val status: String,
    val statusCode: Int
)