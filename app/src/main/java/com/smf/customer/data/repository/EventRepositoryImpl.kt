package com.smf.customer.data.repository

import com.smf.customer.data.api_service.EventService
import com.smf.customer.data.model.request.EventInfoDTO
import com.smf.customer.data.model.response.EventInfoResponseDto
import com.smf.customer.data.model.response.EventQuestionsResponseDTO
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
        eventInfo: EventInfoDTO
    ): Observable<EventInfoResponseDto> {
        return mEventService.postEventInfo(idToken, eventInfo).doOnNext { }
    }

}