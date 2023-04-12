package com.smf.customer.di.sharedpreference

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.smf.customer.data.model.dto.QuestionListItem
import com.smf.customer.data.model.response.EventQuestionsResponseDTO
import java.lang.reflect.Type
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

    fun putHashMap(key: String, obj: Any) {
        Gson().apply {
            val json = this.toJson(obj)
            editor.putString(key, json)
            editor.apply()
        }
    }

    fun getHashMap(key: String): HashMap<Int, Any>? {
        return Gson().let {
            val json = mSharedPreferences.getString(key, "")
            val type: Type = object : TypeToken<HashMap<Int, Any>>() {}.type
            it.fromJson(json, type)
        }
    }

    fun putArrayList(key: String, obj: Any) {
        Gson().apply {
            val json = this.toJson(obj)
            editor.putString(key, json)
            editor.apply()
        }
    }

    fun getArrayList(key: String): ArrayList<Any> {
        return Gson().let {
            val json = mSharedPreferences.getString(key, "")
            val type: Type = object : TypeToken<ArrayList<Any>>() {}.type
            it.fromJson(json, type)
        }
    }

    fun getArrayIntList(key: String): ArrayList<Int> {
        return Gson().let {
            val json = mSharedPreferences.getString(key, "")
            val type: Type = object : TypeToken<ArrayList<Int>>() {}.type
            it.fromJson(json, type)
        }
    }

    fun getQuestionsList(key: String): ArrayList<QuestionListItem> {
        return Gson().let {
            val json = mSharedPreferences.getString(key, "")
            val type: Type = object : TypeToken<ArrayList<QuestionListItem>>() {}.type
            it.fromJson(json, type)
        }
    }

    fun putObject(key: String, obj: Any?) {
        Gson().apply {
            val json = this.toJson(obj)
            editor.putString(key, json)
            editor.apply()
        }
    }

    fun getObject(key: String): EventQuestionsResponseDTO? {
        return Gson().let {
            val json = mSharedPreferences.getString(key, null)
            val type: Type = object : TypeToken<EventQuestionsResponseDTO>() {}.type
            it.fromJson(json, type)
        }
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

    fun getFullName(): String {
        return get(
            SharedPrefConstant.FIRST_NAME,
            "Guest"
        )!! + " " + get(SharedPrefConstant.LAST_NAME, "User")
    }

    fun deleteSavedData(key: String) {
        editor.remove(key).commit()
    }

}