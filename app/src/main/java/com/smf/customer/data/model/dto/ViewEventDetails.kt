package com.smf.customer.data.model.dto

import android.os.Parcelable
import com.smf.customer.data.model.response.QuestionnaireWrapperDto
import kotlinx.parcelize.Parcelize

@Parcelize
data class ViewEventDetails(
    var eventTitle: String = "",
    var categoryIcon : String = "",
    var eventName: String = "",
    var eventDate: String = "",
    var noOfAttendees: String = "",
    var address1: String = "",
    var address2: String = "",
    var state: String = "",
    var city: String = "",
    var zipCode: String = "",
    var name: String = "",
    var emailId: String = "",
    var mobileNumberWithCountryCode: String = "",
    var eventQuestionMetaDataDto: QuestionnaireWrapperDto? = null
) : Parcelable
