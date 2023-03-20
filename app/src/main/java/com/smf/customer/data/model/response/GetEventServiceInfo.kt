package com.smf.customer.data.model.response

data class GetEventServiceInfo(val data:GetEventServiceDataDto,val result: GetResults,
                               val success: Boolean ): ResponseDTO(
)

data class GetResults(
    val info: String,
    val systemMessage: String
)
data class GetEventServiceDataDto(val eventStatus:String, val eventTrackStatus:String, val eventServiceDtos:ArrayList<EventServiceDtos>)

data class EventServiceDtos(val serviceName:String,val biddingCutOffDate : String, val serviceDate:String,val eventServiceId:String,val serviceCategoryId:String,val leadPeriod:String)
