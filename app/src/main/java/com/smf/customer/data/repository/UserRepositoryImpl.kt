package com.smf.customer.data.repository

import com.smf.customer.data.api_service.UserService
import com.smf.customer.data.model.request.UserRequestDTO
import com.smf.customer.data.model.response.LoginResponseDTO
import io.reactivex.Observable
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(userService: UserService): UserService{
    private var mUserService: UserService = userService

    override fun loginClient(requestDTO: UserRequestDTO): Observable<LoginResponseDTO> {
        return mUserService.loginClient(requestDTO).doOnNext {  }
    }
}