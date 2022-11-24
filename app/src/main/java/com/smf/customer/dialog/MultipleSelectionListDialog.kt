package com.smf.customer.dialog

import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.smf.customer.R
import com.smf.customer.app.base.BaseDialogFragment
import com.smf.customer.app.listener.AdapterOneClickListener
import com.smf.customer.data.model.dto.DialogListItem
import com.smf.customer.databinding.FragmentDialogListBinding
import com.smf.customer.listener.DialogTwoButtonListener
import com.smf.customer.utility.Util.Companion.setWidthPercent

class MultipleSelectionListDialog : BaseDialogFragment(), AdapterOneClickListener,
    ListItemAdapter.ServiceClickListener {
    private lateinit var twoButtonListener: DialogTwoButtonListener

    companion object {
        private const val KEY_TITLE = "KEY_TITLE"
        private const val KEY_LIST_ITEM = "LIST_ITEM"
        private const val KEY_BUTTON_1 = "KEY_BUTTON_1"
        private const val KEY_BUTTON_2 = "KEY_BUTTON_2"

        fun newInstance(
            title: String,
            dialogListItemList: ArrayList<DialogListItem>,
            twoButtonListener: DialogTwoButtonListener,
            isCancelable: Boolean = true,
            positiveBtn: Int = R.string.save_services,
            negativeBtn: Int = R.string.cancel
        ): MultipleSelectionListDialog {
            val args = Bundle()
            args.putString(KEY_TITLE, title)
            args.putSerializable(KEY_LIST_ITEM, dialogListItemList)
            args.putInt(KEY_BUTTON_1, positiveBtn)
            args.putInt(KEY_BUTTON_2, negativeBtn)
            val fragment = MultipleSelectionListDialog()
            fragment.arguments = args
            fragment.isCancelable = isCancelable
            fragment.twoButtonListener = twoButtonListener
            return fragment
        }
    }

    private lateinit var dataBinding: FragmentDialogListBinding
    private var listItemAdapter = ListItemAdapter(this)
    private var dialogListItemList = ArrayList<DialogListItem>()
    private val selectedServicesMap = HashMap<Int?, Boolean?>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_dialog_list, container, false)
        // Transparent background(round corner)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dataBinding.root
    }

    override fun onStart() {
        super.onStart()
        // Setting Dialog Fragment Size
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setWidthPercent(90, 80)
        } else {
            if (dialogListItemList.size < 5) {
                setWidthPercent(90, 50)
            } else {
                setWidthPercent(90, 65)
            }
        }
    }

    override fun setData() {
        // Options recyclerView initial setup
        dataBinding.rcList.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
        dataBinding.rcList.adapter = listItemAdapter
        // Setting UI values
        dataBinding.titleText.text = requireArguments().getString(KEY_TITLE)
        dataBinding.saveBtn.text =
            getString(requireArguments().getInt(KEY_BUTTON_1))
        dataBinding.cancelBtn.text =
            getString(requireArguments().getInt(KEY_BUTTON_2))
        dialogListItemList =
            requireArguments().getSerializable(KEY_LIST_ITEM) as ArrayList<DialogListItem>

        for (i in 0 until 9) {
            selectedServicesMap[i] = i == 2 || i == 4
        }
        // Update adapter
        listItemAdapter.setDialogListItemList(dialogListItemList, selectedServicesMap)
    }

    override fun setupClickListeners() {
        dataBinding.saveBtn.setOnClickListener {

        }

        dataBinding.cancelBtn.setOnClickListener {

        }
    }

    override fun onOneClick(position: Int) {

    }

    override fun onPause() {
        super.onPause()
        dismissAllowingStateLoss()
    }

    override fun onServiceClicked(listPosition: Int, status: Boolean) {
        // Update service map
        selectedServicesMap[listPosition] = status
    }

}