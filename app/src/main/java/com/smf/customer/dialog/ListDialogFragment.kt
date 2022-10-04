package com.smf.customer.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.smf.customer.R
import com.smf.customer.app.base.BaseDialogFragment
import com.smf.customer.app.listener.AdapterOneClickListener
import com.smf.customer.app.listener.SelectItemStringParamListener
import com.smf.customer.data.model.dto.DialogListItem
import com.smf.customer.databinding.FragmentDialogListBinding
import kotlin.collections.ArrayList

class ListDialogFragment : BaseDialogFragment(), AdapterOneClickListener {
    private lateinit var twoButtonListener: SelectItemStringParamListener

    companion object {
        private const val KEY_TITLE = "KEY_TITLE"
        private const val KEY_LIST_ITEM = "LIST_ITEM"
        private const val KEY_BUTTON_1 = "KEY_BUTTON_1"
        private const val KEY_BUTTON_2 = "KEY_BUTTON_2"

        fun newInstance(
            title: String,
            dialogListItemList: ArrayList<DialogListItem>,
            twoButtonListener: SelectItemStringParamListener,
            isCancelable: Boolean = true,
            positiveBtn: Int = R.string.yes,
            negativeBtn: Int = R.string.no
        ): ListDialogFragment {
            val args = Bundle()
            args.putString(KEY_TITLE, title)
            args.putSerializable(KEY_LIST_ITEM, dialogListItemList)
            args.putInt(KEY_BUTTON_1, positiveBtn)
            args.putInt(KEY_BUTTON_2, negativeBtn)
            val fragment = ListDialogFragment()
            fragment.arguments = args
            fragment.isCancelable = isCancelable
            fragment.twoButtonListener = twoButtonListener
            return fragment
        }
    }

    var dialogListItemList = ArrayList<DialogListItem>()
    private lateinit var dataBinding: FragmentDialogListBinding
    var listItemAdapter = ListItemAdapter(this)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_dialog_list, container, false)
        return dataBinding.root
    }

    override fun setData() {
        dataBinding.clError.visibility = View.GONE
        dataBinding.rcList.layoutManager = LinearLayoutManager(this.context)
        dataBinding.rcList.adapter = listItemAdapter

        dataBinding.tvTitle.text = requireArguments().getString(KEY_TITLE)
        dataBinding.btnPositive.text =
            getString(requireArguments().getInt(KEY_BUTTON_1))
        dataBinding.btnNegative.text =
            getString(requireArguments().getInt(KEY_BUTTON_2))
        dialogListItemList =
            requireArguments().getSerializable(KEY_LIST_ITEM) as ArrayList<DialogListItem>
        listItemAdapter.setDialogListItemList(dialogListItemList)

    }

    override fun setupClickListeners() {
        dataBinding.btnNegative.setOnClickListener {
            twoButtonListener.onNegativeClick(this)
        }
        dataBinding.btnPositive.setOnClickListener {
            if (mPosition >= 0) {
                twoButtonListener.onPositiveClick(dialogListItemList[mPosition].id)
                twoButtonListener.onPositiveClick(this)
            } else {
                dataBinding.clError.visibility = View.VISIBLE
            }
        }
        dataBinding.ivClose.setOnClickListener {
            dataBinding.clError.visibility = View.GONE
        }
    }

    override fun onPause() {
        super.onPause()
        dismissAllowingStateLoss()
    }

    var mPosition = -1
    override fun onOneClick(position: Int) {
        mPosition = position
    }
}