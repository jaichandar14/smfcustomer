package com.smf.customer.data.repository

import com.smf.customer.data.api_service.ProvideDetailsService
import com.smf.customer.data.model.request.ServiceInfoDTO
import com.smf.customer.data.model.response.*
import io.reactivex.Observable
import javax.inject.Inject

class ServiceRepositoryImpl @Inject constructor(provideDetailsService: ProvideDetailsService) :
    ProvideDetailsService {

    private var mProvideDetailsService: ProvideDetailsService = provideDetailsService

    override fun getServiceSlots(
        idToken: String,
        serviceCategoryId: Int,
        serviceDate: String
    ): Observable<ServiceSlotsDTO> {
        return mProvideDetailsService.getServiceSlots(idToken, serviceCategoryId, serviceDate)
    }

    override fun getBudgetCalcInfo(
        idToken: String,
        eventId: Int,
        eventServiceDescriptionId: Long,
        estimatedBudget: String
    ): Observable<BudgetCalcInfoDTO> {
        return mProvideDetailsService.getBudgetCalcInfo(
            idToken,
            eventId,
            eventServiceDescriptionId,
            estimatedBudget
        )
    }

    override fun putBudgetCalcInfo(
        idToken: String,
        eventId: Int,
        estimatedBudget: String
    ): Observable<BudgetCalcResDTO> {
        return mProvideDetailsService.putBudgetCalcInfo(idToken, eventId, estimatedBudget)
    }

    override fun getServiceDetailQuestions(
        idToken: String,
        eventServiceId: Int
    ): Observable<EventQuestionsResponseDTO> {
        return mProvideDetailsService.getServiceDetailQuestions(idToken, eventServiceId)
    }

    override fun postServiceDescription(
        idToken: String,
        serviceInfo: ServiceInfoDTO
    ): Observable<EventInfoResponseDto> {
        return mProvideDetailsService.postServiceDescription(idToken, serviceInfo)
    }

    override fun getServiceDescription(
        idToken: String,
        eventServiceDescriptionId: Long
    ): Observable<GetServiceDetailsDTO> {
        return mProvideDetailsService.getServiceDescription(idToken, eventServiceDescriptionId)
    }

    override fun putServiceDescription(
        idToken: String,
        serviceInfo: ServiceInfoDTO
    ): Observable<EventInfoResponseDto> {
        return mProvideDetailsService.putServiceDescription(idToken, serviceInfo)
    }

}