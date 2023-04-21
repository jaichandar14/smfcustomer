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
    val serviceName: Any
)

data class EventServiceVenueDto(
    val redius: String,
    val zipCode: String
)

//data class QuestionnaireWrapperDto(
//    val noOfEventOrganizers: Int,
//    val noOfVendors: Int,
//    val questionnaireDtos: List<GetQuestionnaireDto>
//)

//data class GetQuestionnaireDto(
//    val active: Boolean,
//    val eventTemplateId: Int,
//    val id: Int,
//    val questionMetadata: GetQuestionMetadata,
//    val serviceCategoryId: Int
//)

//data class GetQuestionMetadata(
//    val answer: String?,
//    val choices: List<String>,
//    val eventOrganizer: String,
//    val filter: String,
//    val isMandatory: Boolean,
//    val question: String,
//    val questionType: String,
//    val vendor: String
//)
