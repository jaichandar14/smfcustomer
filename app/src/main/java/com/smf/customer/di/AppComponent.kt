package com.smf.customer.di

import com.smf.customer.di.retrofit.NetworkModule
import com.smf.customer.di.sharedpreference.SharedPreferencesModule
import com.smf.customer.view.dashboard.DashBoardActivity
import com.smf.customer.view.dashboard.DashBoardViewModel
import com.smf.customer.view.dashboard.fragment.MainDashBoardFragment
import com.smf.customer.view.emailotp.EmailOTPActivity
import com.smf.customer.view.emailotp.EmailOTPViewModel
import com.smf.customer.view.eventDetails.EventDetailsActivity
import com.smf.customer.view.eventDetails.EventDetailsViewModel
import com.smf.customer.view.login.LoginActivity
import com.smf.customer.view.login.LoginViewModel
import com.smf.customer.view.myevents.MyEventsActivity
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
    fun inject(activity: DashBoardActivity)
    fun inject(viewModel: DashBoardViewModel)
    fun inject(fragment: MainDashBoardFragment)
    fun inject(activity: MyEventsActivity)
    fun inject(activity: EventDetailsActivity)
    fun inject(viewModel: EventDetailsViewModel)
}