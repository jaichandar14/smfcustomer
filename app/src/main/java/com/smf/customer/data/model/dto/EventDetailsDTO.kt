package com.smf.customer.data.model.dto

import android.os.Parcelable
import com.smf.customer.data.model.response.EventQuestionsResponseDTO
import kotlinx.parcelize.Parcelize

@Parcelize
data class EventDetailsDTO(
    var eventTitle: String ="",
    var templateId: Int = 0,
    var eventId: Int = 0,
    var eventName: String = "",
    var eventDate: String = "",
    var noOfAttendees: String = "",
    var currencyPosition: Int = 0,
    var totalBudget: String = "",
    var iKnowVenue: Boolean = true,
    var address1: String = "",
    var address2: String = "",
    var selectedCountryPosition: Int = 0,
    var selectedStatePosition: Int = 0,
    var city: String = "",
    var zipCode: String = "",
    var name: String = "",
    var emailId: String = "",
    var mobileNumberWithCountryCode: String = "",
    var questionListItem: ArrayList<QuestionListItem> = ArrayList(),
    var questionNumberList: ArrayList<Int> = ArrayList(),
    var eventSelectedAnswerMap: HashMap<Int, ArrayList<String>> = HashMap(),
    var eventQuestionsResponseDTO: EventQuestionsResponseDTO? = null,
) : Parcelable
