package com.smf.customer.data.model.response

data class GetLoginInfoDTO(
    val data: Datas,
    val success: Boolean
):ResponseDTO()

data class Datas(
    val email: String,
    val firstName: String,
    val isActive: Boolean,
    val lastName: String,
    val mobileNumber: String,
    val role: String,
    val roleId: Int,
    val spRegId: Int,
    val userId: Int,
    val userName: String,
    val userStatus: String
)
