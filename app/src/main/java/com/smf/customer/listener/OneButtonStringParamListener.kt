package com.smf.customer.app.listener

interface OneButtonStringParamListener : DialogDismissListener, DialogOneButtonListener {
    fun onPositiveClick(inputValue : String)
}