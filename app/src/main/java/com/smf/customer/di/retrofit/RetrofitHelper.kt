package com.smf.customer.di.retrofit

import com.smf.customer.BuildConfig
import com.smf.customer.data.api_service.DashBoardService
import com.smf.customer.data.api_service.EventService
import com.smf.customer.data.api_service.UserService
import com.smf.customer.data.repository.DashBoardRepositoryImpl
import com.smf.customer.data.repository.EventRepositoryImpl
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

    private fun getEventService(): EventService {
        val retrofitR1 = retrofit.baseUrl(BuildConfig.base_url).build()
        return retrofitR1.create(EventService::class.java)
    }

    fun getEventRepository(): EventRepositoryImpl {
        return EventRepositoryImpl(getEventService())
    }

    private fun getDashBoardService(): DashBoardService {
        val retrofitR1 = retrofit.baseUrl(BuildConfig.base_url).build()
        return retrofitR1.create(DashBoardService::class.java)
    }

    fun getDashBoardRepository(): DashBoardRepositoryImpl {
        return DashBoardRepositoryImpl(getDashBoardService())
    }
}