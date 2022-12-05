package com.smf.customer.view.dashboard.fragment.serviceFragment.eventListDashBoard

import androidx.lifecycle.MutableLiveData
import com.smf.customer.app.base.BaseDashboardViewModel
import com.smf.customer.app.base.BaseFragmentViewModel

class EventsDashBoardViewModel : BaseDashboardViewModel()  {

    var pending=MutableLiveData<Boolean>()
    var mTitleTxt=MutableLiveData<String>()
    fun onPending(){
        mTitleTxt.value="Jai"
        pending.value=true
    }
    fun onCompleted(){
        pending.value=false
    }


}