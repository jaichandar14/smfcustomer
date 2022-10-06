package com.smf.customer.di.retrofit

import com.smf.customer.BuildConfig
import com.smf.customer.data.api_service.UserService
import com.smf.customer.data.repository.UserRepositoryImpl
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class RetrofitHelper
@Inject
constructor(private val retrofit: Retrofit.Builder) {

    private fun getUserService(): UserService {
        val retrofitR = retrofit.baseUrl(BuildConfig.base_url).build()
        return retrofitR.create(UserService::class.java)
    }

    fun getUserRepository(): UserRepositoryImpl {
        return UserRepositoryImpl(getUserService())
    }
}