package com.smf.customer.data.api_service

import com.smf.customer.BuildConfig
import com.smf.customer.data.model.request.UserRequestDTO
import com.smf.customer.data.model.response.GetUserDetails
import com.smf.customer.data.model.response.LoginResponseDTO
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface UserService {

    @POST("user/v1/login/")
    fun loginClient(@Body userRequestDTO: UserRequestDTO): Observable<LoginResponseDTO>

    @GET(BuildConfig.apiType + "no-auth/api/authentication/user-info")
    fun getUserDetails(@Query("loginName") loginName: String): Observable<GetUserDetails>
}
