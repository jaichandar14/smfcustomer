package com.smf.customer.data.api_service

import com.smf.customer.BuildConfig
import com.smf.customer.view.myevents.model.MyEventsDto
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Header

interface EventService {
    // 3275
    @GET(BuildConfig.apiType + "event/api/events/event-types")
    fun getMyEvents(@Header("Authorization") idToken: String): Observable<MyEventsDto>

}