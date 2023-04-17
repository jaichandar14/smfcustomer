package com.smf.customer.data.repository

import com.smf.customer.data.api_service.DashBoardService
import com.smf.customer.data.model.response.*
import com.smf.customer.view.dashboard.responsedto.EventCountDto
import com.smf.customer.view.dashboard.responsedto.EventStatusResponse
import io.reactivex.Observable
import javax.inject.Inject

class DashBoardRepositoryImpl @Inject constructor(dashBoardService: DashBoardService) :
    DashBoardService {
    private var mDashBoardService: DashBoardService = dashBoardService

    override fun getEventCount(idToken: String, username: String): Observable<EventCountDto> {
        return mDashBoardService.getEventCount(idToken, username).doOnNext { }
    }

    override fun getEventStatus(
        idToken: String,
        username: String,
        status: ArrayList<String>
    ): Observable<EventStatusResponse> {
        return mDashBoardService.getEventStatus(idToken, username, status).doOnNext { }
    }

    override fun getEventServiceInfo(
        idToken: String,
        eventId: Int
    ): Observable<GetEventServiceInfo> {
        return mDashBoardService.getEventServiceInfo(idToken, eventId).doOnNext { }
    }

    override fun sendForApproval(
        idToken: String,
        eventId: Int,
        status: String
    ): Observable<EventInfoResponseDto> {
        return mDashBoardService.sendForApproval(idToken, eventId, status).doOnNext { }
    }

    override fun sendEventTrackStatus(
        idToken: String,
        eventId: Int,
        eventTrackStatus: String
    ): Observable<ServiceInfoResponse> {
        return mDashBoardService.sendEventTrackStatus(idToken, eventId, eventTrackStatus)
            .doOnNext { }

    }

    override fun getBiddingResponse(
        idToken: String,
        eventId: Int,
        eventServiceDescriptionId: Long
    ): Observable<EventServiceBiddingResponseDto> {
        return mDashBoardService.getBiddingResponse(idToken, eventId, eventServiceDescriptionId)
            .doOnNext { }
    }

    override fun deleteService(idToken: String, eventId: Int): Observable<BiddingInfoResponse> {
        return mDashBoardService.deleteService(idToken, eventId).doOnNext { }
    }
}
