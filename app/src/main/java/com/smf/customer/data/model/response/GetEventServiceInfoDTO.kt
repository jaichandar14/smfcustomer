package com.smf.customer.data.model.response

import java.math.BigDecimal

data class GetEventServiceInfoDTO(
    val data: EventServiceInfoDto,
    val result: Result,
    val success: Boolean
) : ResponseDTO()

data class EventServiceInfoDto(
    val eventServiceCountsDto: ModifyEventServiceCountsDto,
    val eventServiceDtos: List<EventServiceDto>,
    val eventStatus: String,
    val eventTrackStatus: String
)

data class EventServiceDto(
    val actualServiceBudget: BigDecimal,
    val allocatedBudget: BigDecimal,
    val bidRequestedCount: Int,
    val biddingCutOffDate: String,
    val biddingResponseCount: Int,
    val costingType: String,
    val currencyType: String,
    val customerServiceCategory: Boolean,
    val defaultServiceCategory: Boolean,
    val estimatedBudget: BigDecimal,
    val eventServiceDescriptionId: Long,
    val eventServiceId: Int,
    val eventServiceStatus: String,
    val isServiceRequired: Boolean,
    val leadPeriod: Int,
    val remainingBudget: BigDecimal,
    val serviceCategoryId: Int,
    val serviceCloseDate: String,
    val serviceDate: String,
    val serviceName: String,
    val serviceProviderName: Any,
    val serviceStartDate: String,
    val serviceTemplateIcon: String,
    val templateIcon: Any
)

data class ModifyEventServiceCountsDto(
    val bidAccepted: Long,
    val biddingInProgress: Long,
    val biddingStarted: Long,
    val noBidsReceived: Long,
    val noVendorsAvailable: Long,
    val notServiceableExpired: Long,
    val pendingAdminApproval: Long,
    val provideOrderDetails: Long,
    val rejected: Long,
    val revoked: Long,
    val sendforApproval: Long,
    val serviceCompleted: Long,
    val serviceInProgress: Long,
    val underReview: Long
)