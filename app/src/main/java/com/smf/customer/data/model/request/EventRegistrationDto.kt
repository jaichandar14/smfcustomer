package com.smf.customer.data.model.request

import com.smf.customer.data.model.response.QuestionnaireWrapperDto
import java.math.BigDecimal

data class EventRegistrationDto(
    val eventId: Int,
    val eventTypeId: Int,
    val eventTypeName: String,
    val eventMetaDataDto: EventMetaDataDto,
    val eventQuestionMetaDataDto: QuestionnaireWrapperDto,
    val eventOrganizerId: String,
    val eventStatus: String = "",
    val statusMessage: String = "",
    val eventBudgetDto: EventBudgetDto = EventBudgetDto(),
    val eventMetaData: String = "",
    val categoryIcon: String = "",
    val rejectionComments: String = ""
)

data class EventMetaDataDto(
    val eventInformationDto: EventInformationDto,
    val hostInformationDto: HostInformationDto,
    val venueInformationDto: VenueInformationDto
)

data class EventInformationDto(
    val actualEventBudget: String,
    val attendeesCount: String,
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

data class EventBudgetDto(
    val totalEstimatedBudget: BigDecimal = "0".toBigDecimal(),
    val totalAllocatedBudget: BigDecimal = "0".toBigDecimal(),
    val allocatedBudgetPercentage: BigDecimal = "0".toBigDecimal(),
    val totalRemainingBudget: BigDecimal = "0".toBigDecimal(),
    val remainingBudgetPercentage: BigDecimal = "0".toBigDecimal(),
    val estimatedEventBudget: BigDecimal = "0".toBigDecimal(),
    val serviceCount: Int = 0,
    val eventStatus: String = "",
//    val eventServiceDtos: List<EventServiceDto> = ArrayList(), // this parameter not required
    val currencyType: String = "",
    val actualEventbudget: String = "0",
    val totalUtilizedBudget: BigDecimal = "0".toBigDecimal(),
    val totalActualServiceBudget: BigDecimal = "0".toBigDecimal()
)