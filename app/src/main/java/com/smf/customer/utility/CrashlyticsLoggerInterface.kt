package com.smf.customer.utility

interface CrashlyticsLoggerInterface {
    fun log(message: String)
    fun logException(exception: Exception)
    fun setUserId(userId: String)
}