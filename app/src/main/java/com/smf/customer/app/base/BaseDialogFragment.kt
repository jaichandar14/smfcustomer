package com.smf.customer.app.base

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.smf.customer.app.listener.DialogDismissListener
import com.smf.customer.dialog.DialogConstant

abstract class BaseDialogFragment : DialogFragment() {

    lateinit var dialogDismissListener: DialogDismissListener

    override fun onStart() {
        super.onStart()
        if (this.tag == DialogConstant.INTERNET_DIALOG) {
            dialog?.setCancelable(false)
            dialog?.window?.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT
            )
        } else {
            dialog?.window?.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListeners()
        setData()
    }

    override fun onDestroy() {
        if (::dialogDismissListener.isInitialized) {
            dialogDismissListener.onDialogDismissed()
        }
        super.onDestroy()
    }

    abstract fun setData()
    abstract fun setupClickListeners()

}