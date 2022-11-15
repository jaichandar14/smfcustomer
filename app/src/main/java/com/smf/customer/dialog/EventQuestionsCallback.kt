package com.smf.customer.dialog

import androidx.fragment.app.DialogFragment

interface EventQuestionsCallback {
    fun updateSelectedAnswer(questionNumber: Int, position: Int)
    fun dialogStatus(status: String, questionNumber: Int)
    fun updateQusNumberOnScreenRotation(questionNumber: Int, dialogFragment: DialogFragment)
}