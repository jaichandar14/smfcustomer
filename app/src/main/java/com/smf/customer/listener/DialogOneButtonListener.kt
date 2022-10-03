package com.smf.customer.app.listener

import androidx.fragment.app.DialogFragment

interface DialogOneButtonListener : DialogDismissListener {
    fun onPositiveClick(dialogFragment : DialogFragment)
}