package com.smf.customer.data.api_service

import com.smf.customer.data.model.request.*
import com.smf.customer.data.model.response.LoginResponseDTO
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST

interface UserService {

    @POST("user/v1/login/")
    fun loginClient(@Body userRequestDTO: UserRequestDTO): Observable<LoginResponseDTO>
}
