package com.smf.customer.utility

import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker

// Material design date picker
object DatePicker {
    val newInstance: MaterialDatePicker<Long>

    init {
        // Makes only dates from today forward selectable.
        val constraintsBuilder =
            CalendarConstraints.Builder().setValidator(DateValidatorPointForward.now())
        newInstance = MaterialDatePicker.Builder.datePicker().setTitleText("Event Date")
            .setCalendarConstraints(constraintsBuilder.build()).build()
    }
}