package com.smf.customer.di.sharedpreference

import android.content.SharedPreferences
import com.smf.customer.data.model.request.EventInfoDTO
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
open class SharedPrefsHelper
@Inject
constructor(private val mSharedPreferences: SharedPreferences) {

    var editor: SharedPreferences.Editor = mSharedPreferences.edit()

    fun remove(key: String) {
        editor.remove(key)
        editor.commit()
    }

    fun put(key: String, value: String) {
        editor.putString(key, value)
        editor.commit()
    }

    fun put(key: String, value: Int) {
        editor.putInt(key, value)
        editor.commit()
    }

    fun put(key: String, value: Float) {
        editor.putFloat(key, value)
        editor.commit()
    }

    fun put(key: String, value: Boolean) {
        editor.putBoolean(key, value)
        editor.commit()
    }

    fun put(key: String, value: Long) {
        editor.putLong(key, value)
        editor.commit()
    }

    operator fun get(key: String, defaultValue: String): String {
        return mSharedPreferences.getString(key, defaultValue.toString())!!
    }

    operator fun get(key: String, defaultValue: Int): Int {
        return mSharedPreferences.getInt(key, defaultValue)
    }

    operator fun get(key: String, defaultValue: Long): Long {
        return mSharedPreferences.getLong(key, defaultValue)
    }

    operator fun get(key: String, defaultValue: Float): Float {
        return mSharedPreferences.getFloat(key, defaultValue)
    }

    operator fun get(key: String, defaultValue: Boolean): Boolean {
        return mSharedPreferences.getBoolean(key, defaultValue)
    }

    fun getFullName() : String {
        return get(SharedPrefConstant.FIRST_NAME, "Guest")!! + " "+get(SharedPrefConstant.LAST_NAME, "User")
    }

    fun deleteSavedData(key: String) {
        editor.remove(key).commit()
    }

}