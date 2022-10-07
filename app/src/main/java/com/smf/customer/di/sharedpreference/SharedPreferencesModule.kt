package com.smf.customer.di.sharedpreference

import android.content.Context
import android.content.SharedPreferences
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.smf.customer.R
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
    fun provideSharedPreferences(mainKeyAlias: MasterKey): SharedPreferences {

        return EncryptedSharedPreferences.create(
            context,
            sharedPreferenceName,
            mainKeyAlias,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    @Provides
    fun getMainKeyAlias(): MasterKey {
        val spec = KeyGenParameterSpec.Builder(
            context.getString(R.string._androidx_security_master_key_),
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setKeySize(256)
            .build()

        return MasterKey.Builder(context)
            .setKeyGenParameterSpec(spec)
            .build()
    }

}