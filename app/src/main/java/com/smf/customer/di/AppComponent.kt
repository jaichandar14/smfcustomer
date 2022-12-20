package com.smf.customer.di

import com.smf.customer.di.retrofit.NetworkModule
import com.smf.customer.di.sharedpreference.SharedPreferencesModule
import com.smf.customer.view.dashboard.DashBoardActivity
import com.smf.customer.view.dashboard.DashBoardViewModel
import com.smf.customer.view.dashboard.fragment.MainDashBoardFragment
import com.smf.customer.view.dashboard.fragment.MainDashBoardViewModel
import com.smf.customer.view.dashboard.fragment.serviceFragment.eventListDashBoard.EventsDashBoardFragment
import com.smf.customer.view.dashboard.fragment.serviceFragment.eventListDashBoard.EventsDashBoardViewModel
import com.smf.customer.view.emailotp.EmailOTPActivity
import com.smf.customer.view.emailotp.EmailOTPViewModel
import com.smf.customer.view.eventDetails.EventDetailsActivity
import com.smf.customer.view.eventDetails.EventDetailsViewModel
import com.smf.customer.view.login.LoginActivity
import com.smf.customer.view.login.LoginViewModel
import com.smf.customer.view.myevents.MyEventsActivity
import com.smf.customer.view.myevents.MyEventsViewModel
import com.smf.customer.view.provideservicedetails.ProvideServiceDetailsActivity
import com.smf.customer.view.provideservicedetails.ProvideServiceViewModel
import com.smf.customer.view.questions.QuestionsActivity
import com.smf.customer.view.questions.QuestionsViewModel
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
    fun inject(viewModel: MainDashBoardViewModel)
    fun inject(activity: MyEventsActivity)
    fun inject(viewModel: MyEventsViewModel)
    fun inject(activity: EventDetailsActivity)
    fun inject(viewModel: EventDetailsViewModel)
    fun inject(activity: ProvideServiceDetailsActivity)
    fun inject(viewModel: ProvideServiceViewModel)
    fun inject(activity: EventsDashBoardFragment)
    fun inject(viewModel: EventsDashBoardViewModel)
    fun inject(activity: QuestionsActivity)
    fun inject(viewModel: QuestionsViewModel)
}