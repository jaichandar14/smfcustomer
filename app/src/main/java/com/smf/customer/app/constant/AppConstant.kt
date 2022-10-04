package com.smf.customer.app.constant

object AppConstant {
    const val AUTHORIZATION = "authorization"
    const val EXTRA = "EXTRA"
    // CODE
    val SUCCESS_CODE = 200
    val FORBIDDEN = 403
    val BAD_REQUEST = 400
    val NOT_FOUND = 404
    val UNAUTHORISED = 401
    val TIMEOUT_EXCEPTION = 440
    val TOO_MANY_REQUEST_CODE = 429

    val SERVICE_UNAVAILABLE = 503
    val SERVICE_UNAVAILABLE_2 = 502
    val INTERNAL_SERVER_ERROR_CODE = 500
    val OTP_SENT_CODE = 20022
    val OTP_NOT_SENT_CODE = 20023
    val OTP_ALREADY_SENT_CODE = 20024
    val OTP_EXPIRED = 20025
    val INVALID_OTP = 20026
    val OTP_MATCH_CODE = 20027
}