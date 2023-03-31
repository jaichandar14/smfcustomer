package com.smf.customer.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.smf.customer.R
import com.smf.customer.app.base.BaseDialogFragment
import com.smf.customer.app.listener.DialogOneButtonListener
import com.smf.customer.databinding.FragmentDialogOneButtonBinding

class OneButtonDialogFragment : BaseDialogFragment() {
    private lateinit var oneButtonListener: DialogOneButtonListener

    companion object {
        private const val KEY_TITLE = "KEY_TITLE"
        private const val KEY_SUBTITLE = "KEY_SUBTITLE"
        private const val KEY_BUTTON_TEXT = "KEY_BUTTON_TEXT"
        fun newInstance(
            title: String,
            subTitle: String,
            buttonText: String,
            oneButtonListener: DialogOneButtonListener,
            isCancelable: Boolean = true
        ): OneButtonDialogFragment {
            val args = Bundle()
            args.putString(KEY_TITLE, title)
            args.putString(KEY_SUBTITLE, subTitle)
            args.putString(KEY_BUTTON_TEXT, buttonText)
            val fragment = OneButtonDialogFragment()
            fragment.arguments = args
            fragment.isCancelable = isCancelable
            fragment.oneButtonListener = oneButtonListener
            return fragment
        }
    }

    private lateinit var dataBinding: FragmentDialogOneButtonBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_dialog_one_button, container, false)
        return dataBinding.root
    }

    override fun setData() {
        dataBinding.tvTitle.text = requireArguments().getString(KEY_TITLE)
        dataBinding.tvMessage.text = requireArguments().getString(KEY_SUBTITLE)
        dataBinding.btnPositive.text = requireArguments().getString(KEY_BUTTON_TEXT)
    }

    override fun setupClickListeners() {
        dataBinding.btnPositive.setOnClickListener {
            oneButtonListener.onPositiveClick(this)
        }
    }

    override fun onPause() {
        super.onPause()
        dismissAllowingStateLoss()
    }

}