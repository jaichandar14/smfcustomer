package com.smf.customer.data.model.response

import java.math.BigDecimal
import java.util.*

data class EventServiceBiddingResponseDto(
    val `data`: DataBidding,
    val result: ResultsBidding,
    val success: Boolean
   ) : ResponseDTO()

data class ResultsBidding(
    val info: String,
    val systemMessage: String
)

data class DataBidding(
 var eventId: Int,

    val eventOrganizerId: String,

    val eventServiceDescriptionId: Long,

    val preferredTimeSlots: List<String>,

    val biddingResponseCount: Int?,

    val bidRequestedCount: Int?,

    val serviceDate: String?,

    val bidRequestedDate: String?,

    val biddingCutOffDate: String?,

    val timeLeft: Long?,

    val serviceProviderBiddingResponseDtos: List<ServiceProviderBiddingResponseDto>

)
data class ServiceProviderBiddingResponseDto(

    val serviceProviderName: String,

    val bidValue: BigDecimal,

    val currencyType: String,

    val branchName: String,

    val costingType: String,

    )