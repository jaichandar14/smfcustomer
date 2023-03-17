package com.smf.customer.data.model.response

data class GetEventInfo(
    val `data`: GetDataDto,
    val result: GetResult,
    val success: Boolean
) : ResponseDTO(
)

data class GetResult(
    val info: String,
    val systemMessage: String
)

data class GetDataDto(
    val eventMetaDataDto: EventMetaDataDto
)

data class EventMetaDataDto(
    val eventInformationDto: EventInformationDto
)

data class EventInformationDto(
    val eventDate: String,
    val eventName: String
)