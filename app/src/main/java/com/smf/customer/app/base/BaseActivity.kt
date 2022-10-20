package com.smf.customer.app.base

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.smf.customer.R
import com.smf.customer.di.sharedpreference.SharedPrefConstant
import com.smf.customer.di.sharedpreference.SharedPrefsHelper
import com.smf.customer.dialog.DialogConstant
import com.smf.customer.dialog.InternetErrorDialog
import com.smf.customer.listener.DialogTwoButtonListener
import com.smf.customer.utility.ConnectionLiveData
import com.smf.customer.utility.MyToast
import com.smf.customer.utility.Util
import com.smf.customer.view.splash.SplashActivity
import javax.inject.Inject

abstract class BaseActivity<T : BaseViewModel> : AppActivity(), DialogTwoButtonListener {
    @Inject
    lateinit var preferenceHelper: SharedPrefsHelper
    var TAG: String = this.javaClass.simpleName
    lateinit var viewModel: T
    private lateinit var connectionLiveData: ConnectionLiveData
    private var networkDialog: InternetErrorDialog? = null

    open fun observer() {
        viewModel.toastMessage.observe(this) { message ->
            MyToast.show(this, message, Toast.LENGTH_LONG)
        }
        viewModel.retryErrorMessage.observe(this) { _ ->
            showRetryDialog()
        }
        viewModel.logout.observe(this) { logout ->
            if (logout) {
                viewModel.preferenceHelper.put(
                    SharedPrefConstant.IS_USER_LOGGED_IN,
                    false
                )
                viewModel.preferenceHelper.put(
                    SharedPrefConstant.ACCESS_TOKEN,
                    ""
                )
                SplashActivity.starter(this, logout = true)
            }
        }
        // Observer For Network state
        connectionLiveData.observe(this) { isNetworkAvailable ->
            when (isNetworkAvailable) {
                true -> {
                    Log.d(TAG, "connect onAvailable: act available $isNetworkAvailable")
                    networkDialog?.dismiss()
                }
                false -> {
                    Log.d(TAG, "connect onAvailable: act not available $isNetworkAvailable")
                }
            }
        }
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        if (::preferenceHelper.isInitialized) {
            Util.setUserIdToCrashlytics(preferenceHelper[SharedPrefConstant.USER_ID, ""])
        }
        if (::viewModel.isInitialized) {
            if (viewModel.isRetryDialogVisible()) {
                showRetryDialog()
            }
        }
        connectionLiveData = ConnectionLiveData(this)
        observer()
    }

    open fun showRetryDialog() {
        viewModel.showRetryDialogFlag()
        viewModel.retryErrorMessage.value?.let { getString(it) }?.let {
            when (it) {
                getString(R.string.Internet_error) -> {
                    networkDialog = InternetErrorDialog.newInstance(this)
                    networkDialog!!.show(supportFragmentManager, DialogConstant.INTERNET_DIALOG)
                }
                else -> {
//                    TwoButtonDialogFragment.newInstance(
//                        getString(R.string.retry),
//                        it,
//                        this,
//                        positiveBtn = R.string.retry,
//                    ).show(supportFragmentManager, DialogConstant.RETRY_DIALOG)
                }
            }
        }
    }

    override fun onNegativeClick(dialogFragment: DialogFragment) {
        dialogFragment.dismiss()
        when {
            dialogFragment.tag.equals(DialogConstant.RETRY_DIALOG) -> {
                viewModel.hideRetryDialogFlag()
            }
        }
    }

    override fun onPositiveClick(dialogFragment: DialogFragment) {
        dialogFragment.dismiss()
        when {
            dialogFragment.tag.equals(DialogConstant.RETRY_DIALOG) -> {
                viewModel.hideRetryDialogFlag()
                viewModel.doNetworkOperation()
            }
        }
    }

    override fun onDialogDismissed() {
        viewModel.hideRetryDialogFlag()
    }

}