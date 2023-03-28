package com.smf.customer.data.api_service

import com.smf.customer.BuildConfig
import com.smf.customer.data.model.request.ServiceInfoDTO
import com.smf.customer.data.model.response.*
import io.reactivex.Observable
import retrofit2.http.*

interface ProvideDetailsService {

    @GET(BuildConfig.apiType + "event/api/events/event-service-slots/{service-category-id}")
    fun getServiceSlots(
        @Header("Authorization") idToken: String,
        @Path("service-category-id") serviceCategoryId: Int,
        @Query("serviceDate") serviceDate: String,
    ): Observable<ServiceSlotsDTO>

    @GET(BuildConfig.apiType + "event/api/events/budget-calculation-info/{event-id}/{event-service-description-id}/{estimated-budget}")
    fun getBudgetCalcInfo(
        @Header("Authorization") idToken: String,
        @Path("event-id") eventId: Int,
        @Path("event-service-description-id") eventServiceDescriptionId: Long,
        @Path("estimated-budget") estimatedBudget: String,
    ): Observable<BudgetCalcInfoDTO>

    @PUT(BuildConfig.apiType + "event/api/events/budget-calculation-info/{event-id}/{estimated-budget}")
    fun putBudgetCalcInfo(
        @Header("Authorization") idToken: String,
        @Path("event-id") eventId: Int,
        @Path("estimated-budget") estimatedBudget: String,
    ): Observable<BudgetCalcResDTO>

    // 3431 - Get Service Detail Questions
    @GET(BuildConfig.apiType + "event/api/events/event-service-questionnaire/{event-service-id}")
    fun getServiceDetailQuestions(
        @Header("Authorization") idToken: String,
        @Path("event-service-id") eventServiceId: Int,
    ): Observable<EventQuestionsResponseDTO>

    // 3434 - Post Service description
    @POST(BuildConfig.apiType + "event/api/events/event-service-description")
    fun postServiceDescription(
        @Header("Authorization") idToken: String, @Body serviceInfo: ServiceInfoDTO
    ): Observable<EventInfoResponseDto>

}