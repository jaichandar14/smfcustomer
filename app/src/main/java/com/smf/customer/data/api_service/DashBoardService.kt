package com.smf.customer.data.api_service

import com.smf.customer.BuildConfig
import com.smf.customer.data.model.response.*
import com.smf.customer.view.dashboard.responsedto.EventCountDto
import com.smf.customer.view.dashboard.responsedto.EventStatusResponse
import io.reactivex.Observable
import retrofit2.http.*

interface DashBoardService {
    // 3275
    @GET(BuildConfig.apiType + "event/api/app-events/event-count/{event-organizer-id}")
    fun getEventCount(
        @Header("Authorization") idToken: String,
        @Path("event-organizer-id") username: String,

        ): Observable<EventCountDto>


    @GET(BuildConfig.apiType + "event/api/app-events/user-events/{event-organizer-id}")
    fun getEventStatus(
        @Header("Authorization") idToken: String,
        @Path("event-organizer-id") username: String,
        @Query("status") status: ArrayList<String>,
    ): Observable<EventStatusResponse>


    @GET(BuildConfig.apiType + "event/api/events/event-service-info/{event-id}")
    fun getEventServiceInfo(
        @Header("Authorization") idToken: String,
        @Path("event-id") eventId: Int,
    ): Observable<GetEventServiceInfo>

    // 3439
    @PUT(BuildConfig.apiType + "event/api/events/send-for-approval/{event-id}")
    fun sendForApproval(
        @Header("Authorization") idToken: String,
        @Path("event-id") eventId: Int,
        @Query("comment") status: String
    ): Observable<EventInfoResponseDto>

    //3439
    @PUT(BuildConfig.apiType + "event/api/events/event-track-status/{event-id}/{event-stepper-status}")
    fun sendEventTrackStatus(
        @Header("Authorization") idToken: String,
        @Path("event-id") eventId: Int,
        @Path("event-stepper-status") eventTrackStatus: String
    ): Observable<ServiceInfoResponse>

    //3454
    @GET(BuildConfig.apiType + "event/api/events/event-service-bidding-responses/{event-id}/{event-service-description-id}")
    fun getBiddingResponse(
        @Header("Authorization") idToken: String,
        @Path("event-id") eventId: Int,
        @Path("event-service-description-id") eventServiceDescriptionId: Long
    ): Observable<EventServiceBiddingResponseDto>
    // 3454
    @DELETE(BuildConfig.apiType + "event/api/events/event-service-description/{event-id}")
    fun deleteService(
        @Header("Authorization") idToken: String,
        @Path("event-id") eventId: Int,
    ): Observable<BiddingInfoResponse>
}