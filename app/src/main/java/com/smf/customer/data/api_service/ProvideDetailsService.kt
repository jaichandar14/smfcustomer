package com.smf.customer.data.api_service

import com.smf.customer.BuildConfig
import com.smf.customer.data.model.response.BudgetCalcInfoDTO
import com.smf.customer.data.model.response.BudgetCalcResDTO
import com.smf.customer.data.model.response.EventQuestionsResponseDTO
import com.smf.customer.data.model.response.ServiceSlotsDTO
import io.reactivex.Observable
import retrofit2.http.*

interface ProvideDetailsService {

    @GET(BuildConfig.apiType + "event/api/events/event-service-slots/{service-id}")
    fun getServiceSlots(
        @Header("Authorization") idToken: String,
        @Path("service-id") serviceId: Int,
        @Query("serviceDate") serviceDate: String,
    ): Observable<ServiceSlotsDTO>

//    @PathVariable("event-id") int eventId,
//    @PathVariable("event-service-description-id") long eventServiceDescriptionId,
//    @PathVariable("estimated-budget") String estimatedBudget)
    @GET(BuildConfig.apiType + "event/api/events/budget-calculation-info/1320/0/{amount}")
    fun getBudgetCalcInfo(
        @Header("Authorization") idToken: String,
        @Path("amount") amount: String,
    ): Observable<BudgetCalcInfoDTO>

    @PUT(BuildConfig.apiType + "event/api/events/budget-calculation-info/1320/{amount}")
    fun putBudgetCalcInfo(
        @Header("Authorization") idToken: String,
        @Path("amount") amount: String,
    ): Observable<BudgetCalcResDTO>

    // 3431 - Get Service Detail Questions
    @GET(BuildConfig.apiType + "event/api/events/event-service-questionnaire/{service-id}")
    fun getServiceDetailQuestions(
        @Header("Authorization") idToken: String,
        @Path("service-id") serviceId: Int,
    ): Observable<EventQuestionsResponseDTO>

}