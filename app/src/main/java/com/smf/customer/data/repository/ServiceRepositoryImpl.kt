package com.smf.customer.data.repository

import com.smf.customer.data.api_service.ProvideDetailsService
import com.smf.customer.data.model.response.BudgetCalcInfoDTO
import com.smf.customer.data.model.response.BudgetCalcResDTO
import com.smf.customer.data.model.response.EventQuestionsResponseDTO
import com.smf.customer.data.model.response.ServiceSlotsDTO
import io.reactivex.Observable
import javax.inject.Inject

class ServiceRepositoryImpl @Inject constructor(provideDetailsService: ProvideDetailsService) :
    ProvideDetailsService {

    private var mProvideDetailsService: ProvideDetailsService = provideDetailsService

    override fun getServiceSlots(
        idToken: String,
        serviceId: Int,
        serviceDate: String
    ): Observable<ServiceSlotsDTO> {
        return mProvideDetailsService.getServiceSlots(idToken, serviceId, serviceDate)
    }

    override fun getBudgetCalcInfo(idToken: String, amount: String): Observable<BudgetCalcInfoDTO> {
        return mProvideDetailsService.getBudgetCalcInfo(idToken, amount)
    }

    override fun putBudgetCalcInfo(idToken: String, amount: String): Observable<BudgetCalcResDTO> {
        return mProvideDetailsService.putBudgetCalcInfo(idToken, amount)
    }

    override fun getServiceDetailQuestions(
        idToken: String,
        serviceId: Int
    ): Observable<EventQuestionsResponseDTO> {
        return mProvideDetailsService.getServiceDetailQuestions(idToken, serviceId)
    }

}