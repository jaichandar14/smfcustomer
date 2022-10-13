package com.smf.customer.data.model.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GetUserDetails(
    var success: String,
    var data: GetUserData,
    var result: GetUserResult
) : Parcelable, ResponseDTO()

@Parcelize
data class GetUserData(
    var userId: Int,
    var roleId: Int,
    var role: String,
    var firstName: String,
    var userName: String,
    var lastName: String,
    var email: String,
    var mobileNumber: String,
    var userStatus: String,
    var isActive: Boolean
) : Parcelable

@Parcelize
data class GetUserResult(var info: String) : Parcelable