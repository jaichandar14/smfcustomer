package com.smf.customer.data.model.response

data class ServiceSlotsDTO(
    val `data`: List<String>,
    val result: Result,
    val success: Boolean
) : ResponseDTO()