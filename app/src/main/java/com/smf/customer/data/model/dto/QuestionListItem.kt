package com.smf.customer.data.model.dto

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class QuestionListItem(
    val question: String,
    val choice: ArrayList<String>?,
    val questionType: String,
    val isMandatory  : Boolean
) : Parcelable