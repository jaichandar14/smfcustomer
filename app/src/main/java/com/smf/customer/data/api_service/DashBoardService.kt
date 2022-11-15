package com.smf.customer.data.api_service

import com.smf.customer.BuildConfig
import com.smf.customer.view.dashboard.responsedto.EventCountDto
import com.smf.customer.view.dashboard.responsedto.EventStatusResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

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
}