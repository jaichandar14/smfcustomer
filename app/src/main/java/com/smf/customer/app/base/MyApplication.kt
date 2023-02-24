package com.smf.customer.app.base

import android.app.Application
import android.content.Context
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import android.util.Log
import com.amplifyframework.AmplifyException
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin
import com.amplifyframework.core.Amplify
import com.amplifyframework.core.AmplifyConfiguration
import com.google.firebase.FirebaseApp
import com.smf.customer.BuildConfig
import com.smf.customer.R
import com.smf.customer.app.constant.AppConstant
import com.smf.customer.di.AppComponent
import com.smf.customer.di.DaggerAppComponent
import com.smf.customer.di.retrofit.NetworkModule
import com.smf.customer.di.sharedpreference.SharedPrefConstant
import com.smf.customer.di.sharedpreference.SharedPreferencesModule

class MyApplication : Application() {
    var TAG: String = this.javaClass.simpleName

    companion object {
        lateinit var appContext: Context
        var applicationComponent: AppComponent? = null
        fun getAppContextInitialization(): Boolean = ::appContext.isInitialized
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
            ).networkModule(
                NetworkModule()
            )
//            .toastModule(ToastModule(this))
            .build()

        // Firebase Initialization
        FirebaseApp.initializeApp(this)

        //Amplify Cognito Integration
        try {
            Amplify.addPlugin(AWSCognitoAuthPlugin())
            val resourceId = if (BuildConfig.FLAVOR == AppConstant.DEV) R.raw.amplifyconfiguration
            else if (BuildConfig.FLAVOR == AppConstant.QA) R.raw.qa_aws
            else if (BuildConfig.FLAVOR == AppConstant.UAT) R.raw.amplifyconfigurationuat
            else R.raw.amplifyconfigurationuat
            val config = AmplifyConfiguration.fromConfigFile(applicationContext, resourceId)
            Amplify.configure(config, applicationContext)
            Log.i(TAG, "Initialized Amplify")
        } catch (error: AmplifyException) {
            Log.e(TAG, "Could not initialize Amplify", error)
        }
    }
}