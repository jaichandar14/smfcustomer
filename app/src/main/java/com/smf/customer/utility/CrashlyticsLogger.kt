package com.smf.customer.utility

import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase

object CrashlyticsLogger : CrashlyticsLoggerInterface {

    override fun log(message: String) {
        Firebase.crashlytics.log(message)
    }

    override fun logException(exception: Exception) {
        Firebase.crashlytics.recordException(exception)
    }

    override fun setUserId(userId: String) {
        Firebase.crashlytics.setUserId(userId)
    }
}