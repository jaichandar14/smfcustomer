package com.smf.customer.view.dashboard.responsedto

import android.os.Parcelable
import com.smf.customer.data.model.response.ResponseDTO
import kotlinx.parcelize.Parcelize

@Parcelize
data class EventCountDto(
    var success: String,
    var data: MyEventData,
    var result: MyEventResult
) : Parcelable, ResponseDTO()

@Parcelize
data class MyEventData(
    var newEventsCount: Int,
    var pendingEventsCount: Int,
    var rejectedEventsCount: Int,
    var revokedEventsCount: Int,
    var totalEventsCount: Int,
    var underReviewEventsCount: Int,
    var approvedEventsCount: Int,
    var closedEventsCount: Int,
    var deletedEventsCount: Int,
) : Parcelable

@Parcelize
data class MyEventResult(var info: String) : Parcelable