package com.smf.customer.app.listener

import androidx.fragment.app.DialogFragment

interface DialogTwoButtonListener : DialogOneButtonListener {
    fun onNegativeClick(dialogFragment : DialogFragment)
}