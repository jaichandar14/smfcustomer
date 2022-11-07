package com.smf.customer.app.base

import androidx.fragment.app.Fragment
import com.smf.customer.di.sharedpreference.SharedPrefsHelper
import javax.inject.Inject

// 3262
abstract class BaseFragment<T : BaseViewModel> : Fragment() {

    @Inject
    lateinit var preferenceHelper: SharedPrefsHelper
    var TAG: String = this.javaClass.simpleName
    lateinit var viewModel: T
}