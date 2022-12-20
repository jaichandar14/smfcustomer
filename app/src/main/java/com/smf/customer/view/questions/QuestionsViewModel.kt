package com.smf.customer.view.questions

import androidx.lifecycle.MutableLiveData
import com.smf.customer.app.base.BaseViewModel
import com.smf.customer.app.base.MyApplication

class QuestionsViewModel : BaseViewModel() {

    var selectedAnswerLiveDate = MutableLiveData<HashMap<Int, ArrayList<String>>>()
    fun updateSelectedAnswerLiveDate(selectedAnswerPositionMap: HashMap<Int, ArrayList<String>>) {
        selectedAnswerLiveDate.value = selectedAnswerPositionMap
    }

    var selectedAnswerMap = HashMap<Int, ArrayList<String>>()

    init {
        MyApplication.applicationComponent.inject(this)
    }

}