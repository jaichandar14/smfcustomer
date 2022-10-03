package com.smf.customer.app.listener

interface SelectItemStringParamListener : DialogDismissListener, DialogTwoButtonListener {
    fun onPositiveClick(inputValue : String)
}