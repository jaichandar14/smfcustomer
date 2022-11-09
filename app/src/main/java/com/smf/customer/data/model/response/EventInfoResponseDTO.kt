package com.smf.customer.data.model.response

data class EventInfoResponseDto(
    val `data`: DataDto,
    val result: Results,
    val success: Boolean
): ResponseDTO()

data class Results(
    val info: String,
    val systemMessage: String
)

data class DataDto(
    val docId: String,
    val key: Int,
    val message: String,
    val status: String,
    val statusCode: Int
)