package com.smf.customer.view.myevents.model

import android.os.Parcelable
import com.smf.customer.data.model.response.ResponseDTO
import kotlinx.parcelize.Parcelize

@Parcelize
data class MyEventsDto(
    var success: String,
    var data: ArrayList<MyEventData>,
    var result: MyEventResult
) : Parcelable, ResponseDTO()

@Parcelize
data class MyEventData(
    var eventTemplateId: Int,
    var eventName: String,
) : Parcelable

@Parcelize
data class MyEventResult(var info: String) : Parcelable