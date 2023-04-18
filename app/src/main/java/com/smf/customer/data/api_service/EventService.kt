package com.smf.customer.data.api_service

import com.smf.customer.BuildConfig
import com.smf.customer.data.model.request.AddServicesReqDTO
import com.smf.customer.data.model.request.EventInfoDTO
import com.smf.customer.data.model.response.*
import com.smf.customer.view.myevents.model.MyEventsDto
import io.reactivex.Observable
import retrofit2.http.*

interface EventService {
    // 3275
    @GET(BuildConfig.apiType + "event/api/events/event-types")
    fun getMyEvents(@Header("Authorization") idToken: String): Observable<MyEventsDto>

    // 3273 - Get Event Detail Questions
    @GET(BuildConfig.apiType + "event/api/app-events/event-template-questionnaire/{event-template-id}")
    fun getEventDetailQuestions(
        @Header("Authorization") idToken: String,
        @Path("event-template-id") eventTemplateId: Int,
    ): Observable<EventQuestionsResponseDTO>

    // 3273 - Post Event Info
    @POST(BuildConfig.apiType + "event/api/app-events/event-info")
    fun postEventInfo(
        @Header("Authorization") idToken: String, @Body eventInfo: EventInfoDTO
    ): Observable<EventInfoResponseDto>

    // 3463 - Put Event Info
    @PUT(BuildConfig.apiType + "event/api/app-events/event-info")
    fun putEventInfo(
        @Header("Authorization") idToken: String, @Body eventInfo: EventInfoDTO
    ): Observable<EventInfoResponseDto>

    // For modify event api call
    @GET(BuildConfig.apiType + "event/api/events/event-service-info/{event-id}")
    fun getEventServiceInfo(
        @Header("Authorization") idToken: String,
        @Path("event-id") eventId: Int,
    ): Observable<GetEventServiceInfoDTO>

    @GET(BuildConfig.apiType + "event/api/app-events/event-info/{event-id}")
    fun getEventInfo(
        @Header("Authorization") idToken: String,
        @Path("event-id") eventId: Int,
    ): Observable<GetEventInfo>

    @GET(BuildConfig.apiType + "event/api/events/event-services/{event-template-id}")
    fun getAddServices(
        @Header("Authorization") idToken: String,
        @Path("event-template-id") eventTemplateId: Int,
    ): Observable<GetServicesDTO>

    @POST(BuildConfig.apiType + "event/api/events/event-service-info/{event-id}")
    fun postAddServices(
        @Header("Authorization") idToken: String,
        @Path("event-id") eventId: Int,
        @Body addServicesReqDTO: List<AddServicesReqDTO>
    ): Observable<EventInfoResponseDto>
}