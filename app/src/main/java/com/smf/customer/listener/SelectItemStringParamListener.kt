package com.smf.customer.app.listener

import com.smf.customer.listener.DialogTwoButtonListener

interface SelectItemStringParamListener : DialogDismissListener, DialogTwoButtonListener {
    fun onPositiveClick(inputValue : String)
}