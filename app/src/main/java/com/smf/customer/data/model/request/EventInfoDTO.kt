package com.smf.customer.data.model.request

import java.math.BigDecimal

data class EventInfoDTO(
    val eventId: Int,
    val eventTypeId: Int,
    val eventTypeName: String = "",
    val eventMetaDataDto: EventMetaDataDto,
    val eventQuestionMetaDataDto: EventQuestionMetaDataDto,
    val eventOrganizerId: String,
    val eventStatus: String = "",
    val statusMessage: String = "",
    val eventBudgetDto: EventBudgetDto = EventBudgetDto(),
    val eventMetaData: String = "",
    val categoryIcon: String = "",
    val rejectionComments: String = ""
)

data class EventInformationDto(
    val actualEventBudget: String,
    val attendeesCount: String,
    val currencyType: String,
    val estimatedEventBudget: BigDecimal,
    val eventDate: String,
    val eventName: String
)

data class EventMetaDataDto(
    val eventInformationDto: EventInformationDto,
    val hostInformationDto: HostInformationDto,
    val venueInformationDto: VenueInformationDto
)

data class HostInformationDto(
    val email: String,
    val mobile: String,
    val name: String
)

data class EventQuestionMetaDataDto(
    val noOfEventOrganizers: Int,
    val noOfVendors: Int,
    val questionnaireDtos: List<QuestionnaireDto>
)

data class QuestionMetadata(
    val answer: String?,
    val choices: List<String>,
    val eventOrganizer: String,
    val filter: String,
    val question: String,
    val questionType: String,
    val vendor: String
)

data class QuestionnaireDto(
    val active: Boolean,
    val checkBoxAnswers: List<Any>,
    val eventTemplateId: Int,
    val id: Int,
    val questionFormat: String,
    val questionMetadata: QuestionMetadata,
    val serviceCategoryId: Int
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
    val actualEventbudget: String = "",
    val totalUtilizedBudget: BigDecimal = "0".toBigDecimal(),
    val totalActualServiceBudget: BigDecimal = "0".toBigDecimal()
)