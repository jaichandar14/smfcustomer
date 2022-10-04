package com.smf.customer.app.base

import android.app.Application
import android.content.Context
import com.smf.customer.di.AppComponent
import com.smf.customer.di.DaggerAppComponent
import com.smf.customer.di.sharedpreference.SharedPrefConstant
import com.smf.customer.di.sharedpreference.SharedPreferencesModule
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import com.smf.customer.BuildConfig


class MyApplication : Application() {
    var TAG : String = this.javaClass.simpleName
    companion object {
        lateinit var appContext : Context
        lateinit var applicationComponent: AppComponent
    }
    operator fun get(context: Context): MyApplication {
        return context.applicationContext as MyApplication
    }

    override fun onCreate() {
        super.onCreate()
        appContext = this
        val builder = VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        applicationComponent = DaggerAppComponent.builder()
            .sharedPreferencesModule(
                SharedPreferencesModule(
                    this,
                    SharedPrefConstant.SHARED_PREFERENCE
                )
            )
//            .networkModule(
//                NetworkModule(
//                                    )
//            )
//            .toastModule(ToastModule(this))
            .build()
        //FirebaseApp.initializeApp(this)
    }
}