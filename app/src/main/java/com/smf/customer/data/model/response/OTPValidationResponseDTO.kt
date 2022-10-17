package com.smf.customer.data.model.response

data class OTPValidationResponseDTO (
    var success: Boolean,
    var data: Int,
    var result: Result,
    var errorMessage: String
):ResponseDTO()


data class Result(var info: String)