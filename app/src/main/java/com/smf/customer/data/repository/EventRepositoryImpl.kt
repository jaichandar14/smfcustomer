package com.smf.customer.data.repository

import com.smf.customer.data.api_service.EventService
import com.smf.customer.data.model.request.AddServicesReqDTO
import com.smf.customer.data.model.request.EventRegistrationDto
import com.smf.customer.data.model.response.EventInfoResponseDto
import com.smf.customer.data.model.response.EventQuestionsResponseDTO
import com.smf.customer.data.model.response.GetEventInfoDTO
import com.smf.customer.data.model.response.GetServicesDTO
import com.smf.customer.view.myevents.model.MyEventsDto
import io.reactivex.Observable
import javax.inject.Inject

class EventRepositoryImpl @Inject constructor(eventService: EventService) : EventService {
    private var mEventService: EventService = eventService

    override fun getMyEvents(idToken: String): Observable<MyEventsDto> {
        return mEventService.getMyEvents(idToken).doOnNext { }
    }

    override fun getEventDetailQuestions(
        idToken: String,
        eventTemplateId: Int
    ): Observable<EventQuestionsResponseDTO> {
        return mEventService.getEventDetailQuestions(idToken, eventTemplateId).doOnNext { }
    }

    override fun postEventInfo(
        idToken: String,
        eventRegistrationDto: EventRegistrationDto
    ): Observable<EventInfoResponseDto> {
        return mEventService.postEventInfo(idToken, eventRegistrationDto).doOnNext { }
    }

    override fun putEventInfo(
        idToken: String,
        eventRegistrationDto: EventRegistrationDto
    ): Observable<EventInfoResponseDto> {
        return mEventService.putEventInfo(idToken, eventRegistrationDto).doOnNext { }
    }

    override fun getEventInfo(
        idToken: String,
        eventId: Int
    ): Observable<GetEventInfoDTO> {
        return mEventService.getEventInfo(idToken, eventId).doOnNext {}
    }

    override fun getAddServices(idToken: String, eventTemplateId: Int): Observable<GetServicesDTO> {
        return mEventService.getAddServices(idToken, eventTemplateId)
    }

    override fun postAddServices(
        idToken: String,
        eventId: Int,
        addServicesReqDTO: List<AddServicesReqDTO>
    ): Observable<EventInfoResponseDto> {
        return mEventService.postAddServices(idToken, eventId, addServicesReqDTO)
    }
}