package com.smf.customer.data.model.response

import java.math.BigDecimal

data class GetEventInfoDTO(
    val `data`: EventInfo,
    val result: Result,
    val success: Boolean
) : ResponseDTO()

data class EventInfo(
    val categoryIcon: String,
    val eventBudgetDto: EventBudgetDto,
    val eventId: Int,
    val eventMetaData: String,
    val eventMetaDataDto: EventMetaDataDto,
    val eventOrganizerId: String,
    val eventQuestionMetaDataDto: QuestionnaireWrapperDto, // value same for GetEventInfoDTO and EventQuestionsResponseDTO
    val eventStatus: String,
    val eventTypeId: Int,
    val eventTypeName: String,
    val rejectionComments: String,
    val statusMessage: String
)

data class EventBudgetDto(
    val actualEventbudget: String,
    val allocatedBudgetPercentage: BigDecimal,
    val currencyType: String,
    val estimatedEventBudget: BigDecimal,
//    val eventServiceDtos: List<EventServiceDto, // Not required
    val eventStatus: String,
    val remainingBudgetPercentage: BigDecimal,
    val serviceCount: Int,
    val totalActualServiceBudget: BigDecimal,
    val totalAllocatedBudget: BigDecimal,
    val totalEstimatedBudget: BigDecimal,
    val totalRemainingBudget: BigDecimal,
    val totalUtilizedBudget: BigDecimal
)

data class EventMetaDataDto(
    val eventInformationDto: EventInformationDto,
    val hostInformationDto: HostInformationDto,
    val venueInformationDto: VenueInformationDto
)

data class EventInformationDto(
    val actualEventBudget: String,
    val attendeesCount: Int,
    val currencyType: String,
    val estimatedEventBudget: String,
    val eventDate: String,
    val eventName: String
)

data class HostInformationDto(
    val email: String,
    val mobile: String,
    val name: String
)

data class VenueInformationDto(
    val addressLine1: String,
    val addressLine2: String,
    val city: String,
    val country: String,
    val knownVenue: Boolean,
    val state: String,
    val zipCode: String
)