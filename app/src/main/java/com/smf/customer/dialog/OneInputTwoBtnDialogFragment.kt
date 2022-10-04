package com.smf.customer.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.smf.customer.R
import com.smf.customer.app.base.BaseDialogFragment
import com.smf.customer.app.listener.OneButtonStringParamListener
import com.smf.customer.databinding.FragmentDialogTwoBtnInputBinding

class OneInputTwoBtnDialogFragment : BaseDialogFragment() {
    private lateinit var oneButtonStringParamListener : OneButtonStringParamListener
    companion object {
        private const val KEY_TITLE = "KEY_TITLE"
        private const val KEY_INPUT_TYPE = "KEY_INPUT_TYPE"
        private const val KEY_INPUT_HINT = "KEY_INPUT_HINT"
        private const val KEY_POSITIVE = "KEY_POSITIVE"
        private const val KEY_NEGATIVE = "KEY_NEGATIVE"

        fun newInstance(title: String, inputType : Int, inputHint : String,
                        oneButtonStringParamListener : OneButtonStringParamListener,
                        positiveButton : Int = R.string.yes, negativeButton : Int = R.string.no)
                        : OneInputTwoBtnDialogFragment {
            val args = Bundle()
            args.putString(KEY_TITLE, title)
            args.putInt(KEY_INPUT_TYPE, inputType)
            args.putString(KEY_INPUT_HINT, inputHint)
            args.putInt(KEY_POSITIVE, positiveButton)
            args.putInt(KEY_NEGATIVE, negativeButton)
            val fragment = OneInputTwoBtnDialogFragment()
            fragment.arguments = args
            fragment.isCancelable = false
            fragment.oneButtonStringParamListener = oneButtonStringParamListener
            fragment.dialogDismissListener = oneButtonStringParamListener
                return fragment
        }
    }

    private lateinit var dataBinding : FragmentDialogTwoBtnInputBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_dialog_two_btn_input, container, false)
        return dataBinding.root
    }

    override fun setData() {
        dataBinding.tvTitle.text = requireArguments().getString(KEY_TITLE)
        dataBinding.tilInput.hint = requireArguments().getString(KEY_INPUT_HINT)
        dataBinding.etInput.inputType = requireArguments().getInt(KEY_INPUT_TYPE)
        dataBinding.btnPositive.text = getString(requireArguments().getInt(KEY_POSITIVE))
        dataBinding.btnNegative.text = getString(requireArguments().getInt(KEY_NEGATIVE))

    }

    override fun setupClickListeners() {
        dataBinding.btnNegative.setOnClickListener {
            oneButtonStringParamListener.onPositiveClick(this)
            dismiss()
        }
        dataBinding.btnPositive.setOnClickListener {
            dismiss()
            oneButtonStringParamListener.onPositiveClick(dataBinding.etInput.text.toString())
        }
    }
}