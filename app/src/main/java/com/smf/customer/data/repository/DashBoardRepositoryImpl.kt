package com.smf.customer.data.repository

import com.smf.customer.data.api_service.DashBoardService
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
}
