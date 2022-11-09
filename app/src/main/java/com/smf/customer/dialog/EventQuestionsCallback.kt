package com.smf.customer.dialog

interface EventQuestionsCallback {
    fun updateSelectedAnswer(questionNumber: Int, position: Int)
    fun dialogStatus(status: String, questionNumber: Int)
}