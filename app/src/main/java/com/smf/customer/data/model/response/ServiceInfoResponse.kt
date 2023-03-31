package com.smf.customer.data.model.response

data class ServiceInfoResponse(
    val `data`: DataDtos,
    val result: ResultsDto,
    val success: Boolean
) : ResponseDTO()

data class ResultsDto(
    val info: String,
    val systemMessage: String
)

data class DataDtos(
    val docId: String,
    val key: Int,
    val message: String,
    val status: String,
    val statusCode: Int
)