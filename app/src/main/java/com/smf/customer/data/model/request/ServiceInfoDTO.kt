package com.smf.customer.data.model.request

import java.math.BigDecimal

data class ServiceInfoDTO(
    val eventServiceDescriptionDto: EventServiceDescriptionDto,
    val eventServiceId: Int,
    val questionnaireWrapperDto: QuestionnaireWrapperDto
)

data class QuestionnaireWrapperDto(
    val noOfEventOrganizers: Int,
    val noOfVendors: Int,
    val questionnaireDtos: List<QuestionnaireDtoService>
)

data class QuestionnaireDtoService(
    val active: Boolean,
    val checkBoxAnswers: List<String>,
    val eventTemplateId: Int,
    val id: Int,
    val questionFormat: String,
    val questionMetadata: QuestionMetadataService,
    val serviceCategoryId: Int
)

data class QuestionMetadataService(
    val answer: String,
    val choices: List<String>,
    val eventOrganizer: String,
    val filter: String,
    val isMandatory: Boolean,
    val question: String,
    val questionType: String,
    val vendor: String
)

data class EventServiceVenueDto(
    val redius: String,
    val zipCode: String
)

data class EventServiceDescriptionDto(
    val eventServiceBudgetDto: EventServiceBudgetDto,
    val eventServiceDateDto: EventServiceDateDto,
    val eventServiceVenueDto: EventServiceVenueDto
)

data class EventServiceDateDto(
    val biddingCutOffDate: Any?,
    val leadPeriod: Int,
    val preferredSlots: List<String>,
    val serviceDate: String
)

data class EventServiceBudgetDto(
    val currencyType: String,
    val estimatedBudget: BigDecimal
)