package com.smf.customer.utility

import android.annotation.SuppressLint
import android.content.Context
import android.text.format.DateFormat
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.MaterialTimePicker.INPUT_MODE_KEYBOARD
import com.google.android.material.timepicker.TimeFormat
import com.smf.customer.R
import com.smf.customer.app.constant.AppConstant
import java.text.SimpleDateFormat
import java.util.*

object DateTimePicker {

    fun getDatePickerTitle(from: String, context: Context): String {
        return when (from) {
            context.getString(R.string.event_details_activity) -> {
                context.getString(R.string.event_date)
            }
            else -> {
                context.getString(R.string.service_date)
            }
        }
    }

    fun getDatePicker(title: String, selectedDate: Long?): MaterialDatePicker<Long> {
        return MaterialDatePicker.Builder.datePicker().setTitleText(title)
            .setSelection(selectedDate)
//            .setTheme(R.style.ThemeOverlay_App_DatePicker)
            .build()
    }

    fun getTimePicker(context: Context, hour: Int, minute: Int, title: Int): MaterialTimePicker {
        val isSystem24Hour = DateFormat.is24HourFormat(context.applicationContext)
        val clockFormat = if (isSystem24Hour) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H
        return MaterialTimePicker.Builder()
            .setTimeFormat(clockFormat)
            .setInputMode(INPUT_MODE_KEYBOARD)
            .setHour(hour)
            .setMinute(minute)
            .setTitleText(title)
            .build()
    }

    @SuppressLint("SimpleDateFormat")
    fun convertLongToDate(longValue: Long): String {
        val dateFormatter = SimpleDateFormat(AppConstant.DATE_FORMAT)
        return dateFormatter.format(Date(longValue))
    }

    @SuppressLint("SimpleDateFormat")
    fun convertDateToLong(preSelectedDate: String?): Long {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"), Locale.getDefault())
        val list = preSelectedDate!!.split("/") as ArrayList<String>
        calendar.set(list[2].toInt(), list[0].toInt() - 1, list[1].toInt())
        return calendar.time.time
    }

    fun getFormattedTime(
        pickedHour: Int,
        pickedMinute: Int,
        materialTimePicker: MaterialTimePicker
    ): String {
        when {
            pickedHour > 12 -> {
                return if (pickedMinute < 10) {
                    "${materialTimePicker.hour - 12}:0${materialTimePicker.minute} pm"
                } else {
                    "${materialTimePicker.hour - 12}:${materialTimePicker.minute} pm"
                }
            }
            pickedHour == 12 -> {
                return if (pickedMinute < 10) {
                    "${materialTimePicker.hour}:0${materialTimePicker.minute} pm"
                } else {
                    "${materialTimePicker.hour}:${materialTimePicker.minute} pm"
                }
            }
            pickedHour == 0 -> {
                return if (pickedMinute < 10) {
                    "${materialTimePicker.hour + 12}:0${materialTimePicker.minute} am"
                } else {
                    "${materialTimePicker.hour + 12}:${materialTimePicker.minute} am"
                }
            }
            else -> {
                return if (pickedMinute < 10) {
                    "${materialTimePicker.hour}:0${materialTimePicker.minute} am"
                } else {
                    "${materialTimePicker.hour}:${materialTimePicker.minute} am"
                }
            }
        }
    }

}