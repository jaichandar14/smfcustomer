package com.smf.customer.data.model.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class OTPValidationResponseDTO (
    var success: Boolean,
    var data: Int,
    var result: Result,
    var errorMessage: String
):ResponseDTO()

@Parcelize
data class Result(var info: String): Parcelable