package com.smf.customer.listener

import androidx.fragment.app.DialogFragment

interface DialogThreeButtonListener : DialogTwoButtonListener {
    fun onCancelClick(dialogFragment: DialogFragment)
}