package com.smf.customer.di.sharedpreference

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class SharedPreferencesModule(
    private val context: Context,
    private val sharedPreferenceName: String
) {

    @Provides
    @Singleton
    fun provideSharedPreferences(mainKeyAlias: String): SharedPreferences {

        return EncryptedSharedPreferences.create(
            sharedPreferenceName,
            mainKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    @Provides
    fun getMainKeyAlias(): String {
        val keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC
         return MasterKeys.getOrCreate(keyGenParameterSpec)
    }
}