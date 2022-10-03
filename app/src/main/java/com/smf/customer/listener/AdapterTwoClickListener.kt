package com.smf.customer.app.listener


interface AdapterTwoClickListener : AdapterOneClickListener {
    fun onTwoClick(position: Int)
}