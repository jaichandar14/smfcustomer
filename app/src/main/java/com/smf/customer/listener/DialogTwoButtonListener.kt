package com.smf.customer.listener

import androidx.fragment.app.DialogFragment
import com.smf.customer.app.listener.DialogOneButtonListener

interface DialogTwoButtonListener : DialogOneButtonListener {
    fun onNegativeClick(dialogFragment : DialogFragment)
}