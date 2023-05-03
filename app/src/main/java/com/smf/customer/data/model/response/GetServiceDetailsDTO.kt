package com.smf.customer.data.model.response

data class GetServiceDetailsDTO(
    val `data`: GetServiceDetailsData,
    val result: Result,
    val success: Boolean
) : ResponseDTO()

data class GetServiceDetailsData(
    val eventServiceDescriptionDto: EventServiceDescriptionDto,
    val eventServiceDescriptionId: Int,
    val eventServiceId: Int,
    val eventServiceStatus: String,
    val questionnaireWrapperDto: QuestionnaireWrapperDto
)

data class EventServiceDescriptionDto(
    val eventServiceBudgetDto: EventServiceBudgetDto,
    val eventServiceDateDto: EventServiceDateDto,
    val eventServiceVenueDto: EventServiceVenueDto
)

data class EventServiceBudgetDto(
    val actualServiceBudget: String,
    val currencyType: String,
    val estimatedBudget: String
)

data class EventServiceDateDto(
    val biddingCutOffDate: Any,
    val leadPeriod: Int,
    val preferredSlots: List<String>,
    val serviceDate: String,
    val serviceName: String
)

data class EventServiceVenueDto(
    val redius: String,
    val zipCode: String
)
