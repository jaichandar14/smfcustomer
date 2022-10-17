package com.smf.customer.di

import com.smf.customer.di.retrofit.NetworkModule
import com.smf.customer.di.sharedpreference.SharedPreferencesModule
import com.smf.customer.view.emailotp.EmailOTPActivity
import com.smf.customer.view.emailotp.EmailOTPViewModel
import com.smf.customer.view.login.LoginActivity
import com.smf.customer.view.login.LoginViewModel
import com.smf.customer.view.splash.SplashActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [SharedPreferencesModule::class, NetworkModule::class])
interface AppComponent {

    fun inject(activity: SplashActivity)
    fun inject(activity: LoginActivity)
    fun inject(viewModel: LoginViewModel)
    fun inject(activity: EmailOTPActivity)
    fun inject(viewModel: EmailOTPViewModel)



}