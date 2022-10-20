package com.smf.customer.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.smf.customer.R
import com.smf.customer.app.base.BaseDialogFragment
import com.smf.customer.databinding.DialogNoInternetBinding
import com.smf.customer.listener.DialogTwoButtonListener

class InternetErrorDialog : BaseDialogFragment() {

    private lateinit var twoButtonListener: DialogTwoButtonListener
    private lateinit var dataBinding: DialogNoInternetBinding

    companion object {
        fun newInstance(twoButtonListener: DialogTwoButtonListener): InternetErrorDialog {
            val internetDialog = InternetErrorDialog()
            internetDialog.twoButtonListener = twoButtonListener
            return internetDialog
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding =
            DataBindingUtil.inflate(inflater, R.layout.dialog_no_internet, container, false)
        return dataBinding.root
    }

    override fun getTheme(): Int {
        return R.style.FullScreenDialogTheme
    }

    override fun setData() {}

    override fun setupClickListeners() {
        dataBinding.btnRetry.setOnClickListener {
            twoButtonListener.onPositiveClick(this)
        }
    }

    override fun onPause() {
        super.onPause()
        dismissAllowingStateLoss()
    }

}