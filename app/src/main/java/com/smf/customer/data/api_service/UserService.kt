package com.smf.customer.data.api_service

import com.smf.customer.BuildConfig
import com.smf.customer.data.model.request.UserRequestDTO
import com.smf.customer.data.model.response.GetLoginInfoDTO
import com.smf.customer.data.model.response.GetUserDetails
import com.smf.customer.data.model.response.LoginResponseDTO
import com.smf.customer.data.model.response.OTPValidationResponseDTO
import io.reactivex.Observable
import retrofit2.http.*

interface UserService {

    @POST("user/v1/login/")
    fun loginClient(@Body userRequestDTO: UserRequestDTO): Observable<LoginResponseDTO>

    @GET(BuildConfig.apiType + "no-auth/api/authentication/user-info")
    fun getUserDetails(@Query("loginName") loginName: String): Observable<GetUserDetails>

    // 3245 - Invalid OTP Entry validation
    @GET(BuildConfig.apiType + "no-auth/api/authentication/login-failure")
    fun setOTPValidation(
        @Query("isSuccessful") isSuccessful: Boolean,
        @Query("userName") userName: String,
    ): Observable<OTPValidationResponseDTO>

    // 3245 - GetLogin Details
    @GET(BuildConfig.apiType + "user/api/app-authentication/login")
     fun getLoginInfo(@Header("Authorization") idToken: String):Observable<GetLoginInfoDTO>

}
