package com.smf.customer.data.model.response

data class EventQuestionsResponseDTO(
    val `data`: Data,
    val result: Result,
    val success: Boolean
) : ResponseDTO()

data class QuestionMetadata(
    val answer: Any,
    val choices: List<String>,
    val eventOrganizer: String,
    val filter: String,
    val question: String,
    val questionType: String,
    val vendor: String
)

data class QuestionnaireDto(
    val active: Boolean,
    val eventTemplateId: Int,
    val id: Int,
    val questionMetadata: QuestionMetadata,
    val serviceCategoryId: Int
)

data class Data(
    val noOfEventOrganizers: Int,
    val noOfVendors: Int,
    val questionnaireDtos: List<QuestionnaireDto>
)