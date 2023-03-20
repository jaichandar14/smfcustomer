package com.smf.customer.data.api_service

import com.smf.customer.BuildConfig
import com.smf.customer.data.model.response.BudgetCalcInfoDTO
import com.smf.customer.data.model.response.ServiceSlotsDTO
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface ProvideDetailsService {

    @GET(BuildConfig.apiType + "event/api/events/event-service-slots/{service-id}")
    fun getServiceSlots(
        @Header("Authorization") idToken: String,
        @Path("service-id") serviceId: Int,
        @Query("serviceDate") serviceDate: String,
    ): Observable<ServiceSlotsDTO>

    @GET(BuildConfig.apiType + "event/api/events/budget-calculation-info/1220/0/{amount}")
    fun getBudgetCalcInfo(
        @Header("Authorization") idToken: String,
        @Path("amount") amount: Int,
    ): Observable<BudgetCalcInfoDTO>

}