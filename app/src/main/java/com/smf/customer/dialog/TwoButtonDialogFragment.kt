package com.smf.customer.dialog

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.smf.customer.R
import com.smf.customer.app.base.BaseDialogFragment
import com.smf.customer.databinding.FragmentDialogTwoButtonBinding
import com.smf.customer.listener.DialogTwoButtonListener

class TwoButtonDialogFragment : BaseDialogFragment() {
    private lateinit var twoButtonListener: DialogTwoButtonListener
    private lateinit var dataBinding: FragmentDialogTwoButtonBinding

    companion object {
        private const val KEY_TITLE = "KEY_TITLE"
        private const val KEY_SUBTITLE = "KEY_SUBTITLE"
        private const val KEY_BUTTON_1 = "KEY_BUTTON_1"
        private const val KEY_BUTTON_2 = "KEY_BUTTON_2"
        fun newInstance(
            title: String,
            subTitle: String,
            twoButtonListener: DialogTwoButtonListener,
            isCancelable: Boolean = true,
            positiveBtn: Int = R.string.yes,
            negativeBtn: Int = R.string.no
        ): TwoButtonDialogFragment {
            val args = Bundle()
            args.putString(KEY_TITLE, title)
            args.putString(KEY_SUBTITLE, subTitle)
            args.putInt(KEY_BUTTON_1, positiveBtn)
            args.putInt(KEY_BUTTON_2, negativeBtn)
            val fragment = TwoButtonDialogFragment()
            fragment.arguments = args
            fragment.isCancelable = isCancelable
            fragment.twoButtonListener = twoButtonListener
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_dialog_two_button, container, false)
        return dataBinding.root
    }

    override fun setData() {
            dataBinding.subText.apply {
                if (tag == DialogConstant.AMOUNT_HIGHLIGHTED_DIALOG) {
                    text = requireArguments().getString(KEY_SUBTITLE)
                    textSize= com.intuit.sdp.R.dimen._22sdp.toFloat()
                    setTypeface(null, Typeface.BOLD)
                }else{
                    text = requireArguments().getString(KEY_SUBTITLE)
                }
            }
        dataBinding.messageText.text = requireArguments().getString(KEY_TITLE)
        dataBinding.yesBtn.text = getString(requireArguments().getInt(KEY_BUTTON_1))
        dataBinding.noBtn.text = getString(requireArguments().getInt(KEY_BUTTON_2))
    }

    override fun setupClickListeners() {
        dataBinding.noBtn.setOnClickListener {
            twoButtonListener.onNegativeClick(this)
        }
        dataBinding.yesBtn.setOnClickListener {
            twoButtonListener.onPositiveClick(this)
        }
    }

//    override fun onPause() {
//        super.onPause()
//        dismissAllowingStateLoss()
//    }

}