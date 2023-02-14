package com.smf.customer.view.questions

import com.smf.customer.app.base.BaseViewModel
import com.smf.customer.app.base.MyApplication

class QuestionsViewModel : BaseViewModel() {

    var selectedAnswerMap = HashMap<Int, ArrayList<String>>()

    init {
        MyApplication.applicationComponent.inject(this)
    }

}