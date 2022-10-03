package com.smf.customer.di

import com.smf.customer.SplashActivity
import com.smf.customer.di.retrofit.NetworkModule
import com.smf.customer.di.sharedpreference.SharedPreferencesModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [SharedPreferencesModule::class, NetworkModule::class])
interface AppComponent {

    fun inject(activity: SplashActivity)

}