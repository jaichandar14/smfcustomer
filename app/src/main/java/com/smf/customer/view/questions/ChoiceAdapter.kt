package com.smf.customer.view.questions

import android.annotation.SuppressLint
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputLayout
import com.smf.customer.R
import com.smf.customer.app.base.MyApplication

class ChoiceAdapter(
    var updateSelectedAnswerToMap: (ArrayList<String>) -> Unit,
    val choiceList: ArrayList<String>, val questionType: String, var from: String,
    var selectedAnswer: ArrayList<String>, var questionBtnStatus: String, var context: Context
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val VIEW_TYPE_EDIT_TEXT = 1
        const val VIEW_TYPE_CHECK_BOX = 2
        const val VIEW_TYPE_DATE = 3
        const val VIEW_TYPE_RADIO_BTN = 4
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            VIEW_TYPE_EDIT_TEXT -> {
                return EditTextViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.edit_text_list_item, parent, false)
                )
            }
            VIEW_TYPE_CHECK_BOX -> {
                return CheckBtnViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.check_box_list_item, parent, false)
                )
            }
            VIEW_TYPE_DATE -> {
                return DateViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.date_list_item, parent, false)
                )
            }
            VIEW_TYPE_RADIO_BTN -> {
                return RadioViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.radio_btn_list_item, parent, false)
                )
            }
            else -> {
                return RadioViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.radio_btn_list_item, parent, false)
                )
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (questionType) {
            context.getString(R.string.comment_box) -> {
                (holder as EditTextViewHolder).bind(position)
            }
            context.getString(R.string.multiple_choices) -> {
                (holder as CheckBtnViewHolder).bind(position)
            }
            context.getString(R.string.date_time) -> {
                (holder as DateViewHolder).bind(position)
            }
            context.getString(R.string.radio_button) -> {
                (holder as RadioViewHolder).bind(position)
            }
            else -> {
                (holder as RadioViewHolder).bind(position)
            }
        }
    }

    override fun getItemCount(): Int {
        return choiceList.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (questionType) {
            context.getString(R.string.comment_box) -> {
                VIEW_TYPE_EDIT_TEXT
            }
            context.getString(R.string.multiple_choices) -> {
                VIEW_TYPE_CHECK_BOX
            }
            context.getString(R.string.date_time) -> {
                VIEW_TYPE_DATE
            }
            context.getString(R.string.radio_button) -> {
                VIEW_TYPE_RADIO_BTN
            }
            else -> {
                VIEW_TYPE_RADIO_BTN
            }
        }
    }

    private inner class RadioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var radioButton: RadioButton = itemView.findViewById(R.id.radio_button)

        @SuppressLint("NotifyDataSetChanged")
        fun bind(position: Int) {
            radioButton.text = choiceList[position]
            if (questionBtnStatus.contains(MyApplication.appContext.resources.getString(R.string.view_order))) {
                radioButton.isEnabled = false
            }
            // Checked selected radio button
            radioButton.isChecked = selectedAnswer.contains(choiceList[position])
            // set listener on radio button
            radioButton.setOnClickListener {
                if (!questionBtnStatus.contains(MyApplication.appContext.resources.getString(R.string.view_order))) {
                    // update selected position
                    selectedAnswer.clear()
                    selectedAnswer.add(choiceList[absoluteAdapterPosition])
                    Log.d("TAG", "onBindViewHolder: contain $selectedAnswer")
                    // Update Selected Answer to Map
                    updateSelectedAnswerToMap(selectedAnswer)
                    // Notify adapter to refresh the radio button
                    notifyDataSetChanged()
                }
            }
        }
    }

    private inner class CheckBtnViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var checkBox: CheckBox = itemView.findViewById(R.id.check_box)
        fun bind(position: Int) {
            checkBox.text = choiceList[position]
            if (questionBtnStatus.contains(MyApplication.appContext.resources.getString(R.string.view_order))) {
                checkBox.isEnabled = false
            }
            // Checked selected check box
            checkBox.isChecked = selectedAnswer.contains(choiceList[position])
            // set listener on radio button
            checkBox.setOnClickListener {
                if (!questionBtnStatus.contains(MyApplication.appContext.resources.getString(R.string.view_order))) {
                    if (checkBox.isChecked) {
                        // Remove selected position
                        selectedAnswer.add(choiceList[absoluteAdapterPosition])
                    } else {
                        // Update selected position
                        selectedAnswer.remove(choiceList[absoluteAdapterPosition])
                    }
                    // Update Selected Answer to Map
                    updateSelectedAnswerToMap(selectedAnswer)
                }
            }
        }
    }

    private inner class DateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var eventDateET: EditText = itemView.findViewById(R.id.event_date)
        var calendarImage: ImageView = itemView.findViewById(R.id.calendar_image)
        fun bind(position: Int) {
            if (selectedAnswer.isNotEmpty()) {
                eventDateET.setText(selectedAnswer[0])
            } else {
                eventDateET.setText("")
            }
            eventDateET.setOnClickListener {
                // Show date picker dialog
                updateSelectedAnswerToMap(ArrayList())
            }
            calendarImage.setOnClickListener {
                // Show date picker dialog
                updateSelectedAnswerToMap(ArrayList())
            }
        }
    }

    private inner class EditTextViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var editText: EditText = itemView.findViewById(R.id.edit_text_ans)
        var textInputLayout: TextInputLayout = itemView.findViewById(R.id.edit_text_layout)
        fun bind(position: Int) {
            if (selectedAnswer.isNotEmpty()) {
                editText.setText(selectedAnswer[0])
            } else {
                editText.setText("")
            }
            when (from) {
                context.getString(R.string.event_details_activity) -> {
                    textInputLayout.hint = context.getString(R.string.about_event)
                }
                else -> {
                    textInputLayout.hint = context.getString(R.string.about_service)
                }
            }
            editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    // Update Selected Answer to Map
                    updateSelectedAnswerToMap(ArrayList<String>().apply { add(s.toString()) })
                }

                override fun afterTextChanged(s: Editable?) {}

            })
        }
    }
}