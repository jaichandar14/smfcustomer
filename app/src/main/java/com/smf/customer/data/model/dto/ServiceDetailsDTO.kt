package com.smf.customer.data.model.dto

import android.os.Parcelable
import com.smf.customer.data.model.response.EventQuestionsResponseDTO
import kotlinx.parcelize.Parcelize

@Parcelize
data class ServiceDetailsDTO(
    var eventId: Int = 0,
    var serviceName: String = "",
    var eventServiceId: Int = 0,
    var serviceCategoryId: Int = 0,
    var eventServiceDescriptionId: Long = 0,
    var eventServiceStatus: String = "",
    var leadPeriod: Long = 0,
    var serviceDate: String = "",
    var zipCode: String = "",
    var timeSlotList: ArrayList<String> = ArrayList(),
    var selectedSlotsList: ArrayList<String> = ArrayList(),
    var estimatedBudget: String ="",
    var estimatedBudgetSymbol: String = "$",
    var totalAmount: String = "",
    var remainingAmount: String = "",
    var milePosition: Int = 0,
    var questionListItem: ArrayList<QuestionListItem> = ArrayList(),
    var questionNumberList: ArrayList<Int> = ArrayList(),
    var eventSelectedAnswerMap: HashMap<Int, ArrayList<String>> = HashMap(),
    var eventQuestionsResponseDTO: EventQuestionsResponseDTO? = null,
) : Parcelable