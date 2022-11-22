package com.smf.customer.app.base

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.smf.customer.R
import com.smf.customer.di.sharedpreference.SharedPrefConstant
import com.smf.customer.di.sharedpreference.SharedPrefsHelper
import com.smf.customer.dialog.DialogConstant
import com.smf.customer.dialog.InternetErrorDialog
import com.smf.customer.listener.DialogTwoButtonListener
import com.smf.customer.utility.ConnectionLiveData
import com.smf.customer.utility.MyToast
import com.smf.customer.utility.Util
import javax.inject.Inject

// 3262
abstract class BaseFragment<T : BaseFragmentViewModel> : Fragment(), DialogTwoButtonListener {
    @Inject
    lateinit var preferenceHelper: SharedPrefsHelper
    var TAG: String = this.javaClass.simpleName
    lateinit var viewModel: T
    private lateinit var connectionLiveData: ConnectionLiveData
    private var networkDialog: InternetErrorDialog? = null
    var isAvailable = false
    open fun observer() {
        viewModel.toastMessage.observe(this) { message ->
            message?.let { MyToast.show(requireActivity(), message, Toast.LENGTH_LONG) }
        }
        viewModel.retryErrorMessage.observe(this) { retryErrorMessage ->
            Log.d(TAG, "showRetryDialog: ${retryErrorMessage}")
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
            }
        }
        // Observer For Network state
        connectionLiveData.observe(this) { isNetworkAvailable ->
            when (isNetworkAvailable) {
                true -> {
                    Log.d(TAG, "connect onAvailable: frag available $isNetworkAvailable")
                    if (networkDialog?.isVisible == true) {
                        viewModel.hideRetryDialogFlag()
                        viewModel.doNetworkOperation()
                        isAvailable = true
                        networkDialog?.dismiss()
                    }
                }
                false -> {
                    isAvailable = false
                    Log.d(TAG, "connect onAvailable: frag not available $isNetworkAvailable")
                }
            }
        }
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

    }

    override fun onResume() {
        super.onResume()

    }

    override fun onStart() {
        super.onStart()
        if (::preferenceHelper.isInitialized) {
            Util.setUserIdToCrashlytics(preferenceHelper[SharedPrefConstant.USER_ID, ""])
        }
        if (::viewModel.isInitialized) {
            if (viewModel.isRetryDialogVisible()) {
                showRetryDialog(getString(R.string.internet_error))
            }
        }
        connectionLiveData = ConnectionLiveData(requireActivity())
        observer()
    }

    open fun showRetryDialog(retryErrorMessage: String?) {
        retryErrorMessage?.let {
            when (it) {
                getString(R.string.internet_error) -> {
                    Log.d(TAG, "showRetryDialog: ${networkDialog?.tag}")
                    if (networkDialog?.tag.toString() != DialogConstant.INTERNET_DIALOG) {
                        viewModel.showRetryDialogFlag()
                        networkDialog = InternetErrorDialog.newInstance(this)
                        networkDialog!!.show(
                            requireActivity().supportFragmentManager,
                            DialogConstant.INTERNET_DIALOG
                        )
                        Log.d(
                            TAG,
                            "connect onAvailable hashcode frag: ${networkDialog.hashCode()}"
                        )
                    }


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