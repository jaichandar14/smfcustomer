package com.smf.customer.data.model.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class EventQuestionsResponseDTO(
    val `data`: QuestionnaireWrapperDto,
    val result: Result,
    val success: Boolean
) : ResponseDTO(), Parcelable

@Parcelize
data class QuestionnaireWrapperDto(
    val noOfEventOrganizers: Int,
    val noOfVendors: Int,
    val questionnaireDtos: List<QuestionnaireDto>
) : Parcelable

@Parcelize
data class QuestionMetadata(
    val answer: String?,
    val choices: List<String>,
    val eventOrganizer: String,
    val filter: String,
    val question: String,
    val questionType: String,
    val vendor: String,
    val isMandatory: Boolean
) : Parcelable

@Parcelize
data class QuestionnaireDto(
    val active: Boolean,
    val eventTemplateId: Int,
    val id: Int,
    val questionMetadata: QuestionMetadata,
    val serviceCategoryId: Int
) : Parcelable
