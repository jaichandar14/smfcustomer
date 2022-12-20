package com.smf.customer.data.model.request

data class EventInfoDTO(
    val eventId: Int,
    val eventMetaDataDto: EventMetaDataDto,
    val eventOrganizerId: String,
    val eventQuestionMetaDataDto: EventQuestionMetaDataDto,
    val eventTypeId: Int,
    val id: String
)

data class EventInformationDto(
    val actualEventBudget: String,
    val attendeesCount: String,
    val currencyType: String,
    val estimatedEventBudget: Int,
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