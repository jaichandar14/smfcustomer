package com.smf.customer.view.dashboard.responsedto

import android.os.Parcelable
import com.smf.customer.data.model.response.ResponseDTO
import kotlinx.parcelize.Parcelize

@Parcelize
data class EventStatusResponse(
    var success: String,
    var data: EventStatusData,
    var result: EventStatusResult
) : Parcelable, ResponseDTO()

@Parcelize
data class EventStatusData(
    var eventDtos: ArrayList<EventDtos>
) : Parcelable

@Parcelize
data class EventDtos(
    var eventDate: String,
    var eventName: String,
) : Parcelable

@Parcelize
data class EventStatusResult(var info: String) : Parcelable
