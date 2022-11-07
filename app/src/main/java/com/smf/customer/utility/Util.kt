package com.smf.customer.utility

import android.content.Context
import android.util.Log
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.smf.customer.di.sharedpreference.SharedPrefsHelper
import java.io.IOException
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

class Util {
    companion object {
        fun setUserIdToCrashlytics(userId: String) {
            FirebaseCrashlytics.getInstance()
                .setUserId(userId)
        }

        // Method for read JSON file
        fun getCountriesAndStatesList(context: Context, fileName: String): String {
            lateinit var jsonString: String
            try {
                jsonString = context.assets.open(fileName)
                    .bufferedReader()
                    .use { it.readText() }
            } catch (ioException: IOException) {
                Log.d("TAG", "onCreate: ioException $ioException")
            }
            return jsonString
        }

        fun getNewFCMToken(preferenceHelper: SharedPrefsHelper) {

        }

        fun convertToLocalCurrency(amount: String): String {
            val country = "IN"
            val language = "en"
            return NumberFormat.getCurrencyInstance(Locale(language, country))
                .format(amount.toDouble())
        }

        fun isOnlyNumber(string: String): Boolean {
            val regex = "\\d+"
            val pattern = Pattern.compile(regex)
            return pattern.matcher(string).matches()
        }

        fun isBankAccountValid(string: String): Boolean {
            val regex = "^\\d{9,18}\$"
            val pattern = Pattern.compile(regex)
            return pattern.matcher(string).matches()
        }

        fun isIFSCValid(string: String): Boolean {
            val regex = "^[A-Z]{4}0[A-Z0-9]{6}$"
            val pattern = Pattern.compile(regex)
            return pattern.matcher(string).matches()
        }

        fun isMobileNumberValid(string: String): Boolean {
            val regex = "^[6-9]\\d{9}\$"
            val pattern = Pattern.compile(regex)
            return pattern.matcher(string).matches()
        }

        fun isValidEmail(email: String?): Boolean {
            val emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                    "[a-zA-Z0-9_+&*-]+)*@" +
                    "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                    "A-Z]{2,7}$"
            val pat = Pattern.compile(emailRegex)
            return if (email == null) false else pat.matcher(email).matches()
        }

        fun isValidPassword(password: String?): Boolean {
            val reg = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$"
            val pat = Pattern.compile(reg)
            return if (password == null) false else pat.matcher(password).matches()
        }
    }
}