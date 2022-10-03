package com.smf.customer.app.listener

interface OnDateOrTimeSelectListener {
    fun onDateOrTimeSelect(dateTime:String, timeStamp:Long=0)
}