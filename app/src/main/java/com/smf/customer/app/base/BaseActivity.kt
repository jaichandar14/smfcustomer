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
            message?.let { MyToast.show(this, message, Toast.LENGTH_LONG) }
        }
        viewModel.retryErrorMessage.observe(this) { retryErrorMessage ->
            showRetryDialog(retryErrorMessage?.let { getString(it) })
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
                    if (networkDialog?.isVisible == true) {
                        viewModel.hideRetryDialogFlag()
                        viewModel.doNetworkOperation()
                        networkDialog?.dismiss()
                    }

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
                showRetryDialog(getString(R.string.internet_error))
            }
        }
        connectionLiveData = ConnectionLiveData(this)
        observer()
    }

    open fun showRetryDialog(retryErrorMessage: String?) {
        retryErrorMessage?.let {
            when (it) {
                getString(R.string.internet_error) -> {
                    viewModel.showRetryDialogFlag()
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
        when {
            dialogFragment.tag.equals(DialogConstant.RETRY_DIALOG) -> {
                dialogFragment.dismiss()
                viewModel.hideRetryDialogFlag()
            }
        }
    }

    override fun onPositiveClick(dialogFragment: DialogFragment) {
        when {
            dialogFragment.tag.equals(DialogConstant.RETRY_DIALOG) -> {
                dialogFragment.dismiss()
                viewModel.hideRetryDialogFlag()
                viewModel.doNetworkOperation()
            }
        }
    }

    override fun onDialogDismissed() {
        viewModel.hideRetryDialogFlag()
    }

    override fun onDestroy() {
        super.onDestroy()
        // Clear toast and Internet error dialog live data
        viewModel.clearToastData()
        viewModel.clearInternetDialogData()
    }

}