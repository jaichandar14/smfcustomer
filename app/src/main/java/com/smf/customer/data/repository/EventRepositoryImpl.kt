package com.smf.customer.data.repository

import com.smf.customer.data.api_service.EventService
import com.smf.customer.view.myevents.model.MyEventsDto
import io.reactivex.Observable
import javax.inject.Inject

class EventRepositoryImpl @Inject constructor(eventService: EventService) : EventService {
    private var mEventService: EventService = eventService

    override fun getMyEvents(idToken: String): Observable<MyEventsDto> {
        return mEventService.getMyEvents(idToken).doOnNext {  }
    }

}