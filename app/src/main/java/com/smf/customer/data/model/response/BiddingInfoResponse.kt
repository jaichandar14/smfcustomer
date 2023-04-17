package com.smf.customer.data.model.response

data class BiddingInfoResponse   (val `data`: DataDtoes,
val result: ResultsDtos,
val success: Boolean
) : ResponseDTO()

data class ResultsDtos(
    val info: String,
    val systemMessage: String
)

data class DataDtoes(
    val docId: String,
    val key: Int,
    val message: String,
    val status: String,
    val statusCode: Int
)