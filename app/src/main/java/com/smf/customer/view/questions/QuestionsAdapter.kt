package com.smf.customer.view.questions

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smf.customer.R
import com.smf.customer.app.constant.AppConstant
import com.smf.customer.data.model.dto.QuestionListItem
import com.smf.customer.utility.DateTimePicker

class QuestionsAdapter(
    var questionListItem: ArrayList<QuestionListItem>,
    var selectedAnswerPositionMap: HashMap<Int, ArrayList<String>>,
    var context: Context,
    var questionBtnStatus: String,
    var questionNumberList: ArrayList<Int>,
    var from: String,
    var mandatoryQuesErrorPositionList: ArrayList<Int> = ArrayList<Int>()
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    @SuppressLint("NotifyDataSetChanged")
    fun updateValues(
        questionListItem: ArrayList<QuestionListItem>,
        selectedAnswerPositionMap: HashMap<Int, ArrayList<String>>,
        questionBtnStatus: String,
        questionNumberList: ArrayList<Int>,
        mandatoryQuesErrorPositionList: ArrayList<Int>
    ) {
        this.questionListItem = questionListItem
        this.selectedAnswerPositionMap = selectedAnswerPositionMap
        this.questionBtnStatus = questionBtnStatus
        this.questionNumberList = questionNumberList
        this.mandatoryQuesErrorPositionList = mandatoryQuesErrorPositionList
        notifyDataSetChanged()
    }

    companion object {
        const val VIEW_TYPE_EDIT_TEXT = 1
        const val VIEW_TYPE_CHECK_BOX = 2
        const val VIEW_TYPE_DATE = 3
        const val VIEW_TYPE_RADIO_BTN = 4
        const val VIEW_TYPE_DROP_DOWN = 5
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            VIEW_TYPE_EDIT_TEXT -> {
                return EditTextViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.edit_text_list_item_layout, parent, false)
                )
            }
            VIEW_TYPE_CHECK_BOX -> {
                return CheckBoxViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.check_box_list_item_layout, parent, false)
                )
            }
            VIEW_TYPE_DATE -> {
                return DateViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.date_list_item_layout, parent, false)
                )
            }
            VIEW_TYPE_RADIO_BTN -> {
                return RadioBtnViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.radio_btn_list_item_layout, parent, false)
                )
            }
            ChoiceAdapter.VIEW_TYPE_DROP_DOWN -> {
                return DropDownViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.drop_down_list_layout, parent, false)
                )
            }
            else -> {
                return RadioBtnViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.radio_btn_list_item_layout, parent, false)
                )
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (questionListItem[position].questionType) {
            context.getString(R.string.comment_box) -> {
                (holder as EditTextViewHolder).bind(position)
            }
            context.getString(R.string.multiple_choices) -> {
                (holder as CheckBoxViewHolder).bind(position)
            }
            context.getString(R.string.date_time) -> {
                (holder as DateViewHolder).bind(position)
            }
            context.getString(R.string.radio_button) -> {
                (holder as RadioBtnViewHolder).bind(position)
            }
            context.getString(R.string.drop_down) -> {
                (holder as DropDownViewHolder).bind(position)
            }
            else -> {
                (holder as RadioBtnViewHolder).bind(position)
            }
        }
    }

    override fun getItemCount(): Int {
        return questionListItem.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (questionListItem[position].questionType) {
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
            context.getString(R.string.drop_down) -> {
                VIEW_TYPE_DROP_DOWN
            }
            else -> {
                VIEW_TYPE_RADIO_BTN
            }
        }
    }

    private inner class EditTextViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var question: TextView = itemView.findViewById(R.id.edit_text_qus_title)
        var choiceRecycler: RecyclerView = itemView.findViewById(R.id.edit_text_recycler_view)
        var error: TextView = itemView.findViewById(R.id.edit_text_error)
        var choiceAdapter: ChoiceAdapter? = null

        fun bind(position: Int) {
            val recyclerModel = questionListItem[position]
            if (questionListItem[position].isMandatory) {
                question.text = "Q${questionNumberList[position] + 1}. ${recyclerModel.question} *"
            } else {
                question.text = "Q${questionNumberList[position] + 1}. ${recyclerModel.question}"
            }
            // Error visibility
            initialErrorVisibility(error, position)

            val selectedAnswer = selectedAnswerPositionMap[questionNumberList[position]]
            Log.d(
                "TAG",
                "onTextChanged: start $position ${questionNumberList[position]} " +
                        "$selectedAnswer " +
                        "${selectedAnswerPositionMap[questionNumberList[position]]}"
            )
            // If selectedAnswer list is null replace empty list to send adapter
            val updateSelectedAnswer = selectedAnswer ?: ArrayList()
            val updateChoiceList =
                questionListItem[position].choice ?: ArrayList<String>().apply { add("") }
            choiceAdapter = ChoiceAdapter(
                { it -> updateSelectedAnswerToMap(it, position) },
                updateChoiceList,
                questionListItem[position].questionType,
                from,
                updateSelectedAnswer,
                questionBtnStatus,
                context
            )
            choiceRecycler.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            choiceRecycler.adapter = choiceAdapter

        }

        fun updateSelectedAnswerToMap(selectedAnswer: ArrayList<String>, position: Int) {
            Log.d(
                "TAG", "updateSelectedAnswerToMap:edit text called ${position + 1} $selectedAnswer"
            )
            updateAnswerListener?.updateAnswer(position, selectedAnswer, questionNumberList)
            // Verify error visibility
            errorVisibilityAfterValue(error, selectedAnswer, position)
        }

    }

    private inner class CheckBoxViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var question: TextView = itemView.findViewById(R.id.check_box_qus_title)
        var choiceRecycler: RecyclerView = itemView.findViewById(R.id.check_options_recycler_view)
        var error: TextView = itemView.findViewById(R.id.check_options_error)
        var choiceAdapter: ChoiceAdapter? = null

        fun bind(position: Int) {
            val recyclerViewModel = questionListItem[position]
            if (questionListItem[position].isMandatory) {
                question.text =
                    "Q${questionNumberList[position] + 1}. ${recyclerViewModel.question} *"
            } else {
                question.text =
                    "Q${questionNumberList[position] + 1}. ${recyclerViewModel.question}"
            }
            // Error visibility
            initialErrorVisibility(error, position)

            val selectedAnswer = selectedAnswerPositionMap[questionNumberList[position]]
            Log.d(
                "TAG",
                "bind: selectedAnswer check $selectedAnswer $position ${questionNumberList[position]}"
            )
            // If selectedAnswer list is null replace empty list to send adapter
            val updateSelectedAnswer = selectedAnswer ?: ArrayList()
            Log.d("TAG", "bind: selectedAnswer check af $updateSelectedAnswer")
            choiceAdapter = ChoiceAdapter(
                { it -> updateSelectedAnswerToMap(it, position) },
                questionListItem[position].choice!!,
                questionListItem[position].questionType,
                from,
                updateSelectedAnswer,
                questionBtnStatus, context
            )
            choiceRecycler.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            choiceRecycler.adapter = choiceAdapter
        }

        fun updateSelectedAnswerToMap(selectedAnswer: ArrayList<String>, position: Int) {
            Log.d(
                "TAG", "updateSelectedAnswerToMap:check box called ${position + 1} $selectedAnswer"
            )
            updateAnswerListener?.updateAnswer(position, selectedAnswer, questionNumberList)
            // Verify error visibility
            errorVisibilityAfterValue(error, selectedAnswer, position)
        }
    }

    private inner class DateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var question: TextView = itemView.findViewById(R.id.date_qus_title)
        var choiceRecycler: RecyclerView = itemView.findViewById(R.id.date_recycler_view)
        var error: TextView = itemView.findViewById(R.id.date_error)
        var choiceAdapter: ChoiceAdapter? = null

        fun bind(position: Int) {
            val recyclerViewModel = questionListItem[position]
            if (questionListItem[position].isMandatory) {
                question.text =
                    "Q${questionNumberList[position] + 1}. ${recyclerViewModel.question} *"
            } else {
                question.text =
                    "Q${questionNumberList[position] + 1}. ${recyclerViewModel.question}"
            }
            // Error visibility
            initialErrorVisibility(error, position)

            val selectedAnswer = selectedAnswerPositionMap[questionNumberList[position]]
            // Verify error visibility after notifyItemChanged
            selectedAnswer?.let { errorVisibilityAfterValue(error, selectedAnswer, position) }
            Log.d(
                "TAG",
                "onTextChanged: start $position ${questionNumberList[position]} " +
                        "$selectedAnswer " +
                        "${selectedAnswerPositionMap[questionNumberList[position]]}"
            )
            // If selectedAnswer list is null replace empty list to send adapter
            val updateSelectedAnswer = selectedAnswer ?: ArrayList()
            val updateChoiceList =
                questionListItem[position].choice ?: ArrayList<String>().apply { add("") }
            choiceAdapter = ChoiceAdapter(
                { updateSelectedAnswerToMap(it, position, selectedAnswer) },
                updateChoiceList,
                questionListItem[position].questionType,
                from,
                updateSelectedAnswer,
                questionBtnStatus, context
            )
            choiceRecycler.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            choiceRecycler.adapter = choiceAdapter
        }

        fun updateSelectedAnswerToMap(
            selectedDate: ArrayList<String>,
            position: Int,
            selectedAnswer: ArrayList<String>?
        ) {
            // Show date picker dialog
            showDatePickerDialog(position, selectedAnswer)
        }

        @SuppressLint("SimpleDateFormat")
        fun showDatePickerDialog(position: Int, selectedAnswer: ArrayList<String>?) {
            // formatting date to long value
            val preSelectedDateLongValue = selectedAnswer?.let {
                val preSelectedDate = it[0].substring(0, it[0].indexOf("T"))
                DateTimePicker.convertDateToLong(preSelectedDate)
            }
            // Get date picker title text
            val datePickerTitle = DateTimePicker.getDatePickerTitle(from, context)
            //Get date picker
            val datePicker = DateTimePicker.getDatePicker(datePickerTitle, preSelectedDateLongValue)

            datePicker.addOnPositiveButtonClickListener {
                Log.d("TAG", "showTimePickerDialog: date $it")
                // formatting date as MM/dd/yyyy
                val formattedDate = DateTimePicker.convertLongToDate(it)
                // Show time picker
                showTimePickerDialog(formattedDate, selectedAnswer, position)
            }
            // show picker using this
            if (!datePicker.isVisible) {
                datePicker.show(
                    (context as QuestionsActivity).supportFragmentManager,
                    AppConstant.MATERIAL_DATE_PICKER
                )
            }
        }
    }

    fun showTimePickerDialog(
        formattedDate: String,
        selectedAnswer: ArrayList<String>?,
        position: Int
    ) {
        val time = selectedAnswer?.let {
            it[0].substring(it[0].indexOf("T") + 1).split(":")
                    as ArrayList<String>
        }
        Log.d("TAG", "showTimePickerDialog: time $formattedDate $selectedAnswer $time")
        // Get time picker
        val timePicker = if (time.isNullOrEmpty()) {
            // Set default time
            DateTimePicker.getTimePicker(context, 13, 10, R.string.select_time)
        } else {
            // Set previous selected time
            DateTimePicker.getTimePicker(
                context,
                time[0].toInt(),
                time[1].toInt(),
                R.string.select_time
            )
        }

        timePicker.addOnPositiveButtonClickListener {
            var pickedHour: Int = timePicker.hour
            val pickedMinute: Int = timePicker.minute
            Log.d("TAG", "showTimePickerDialog: pos $pickedHour:$pickedMinute")
            pickedHour = if (pickedHour == 0) 24 else pickedHour

            val dateAndTime = formattedDate + "T" + pickedHour + ":" +
                    if (pickedMinute < 10) "0$pickedMinute" else pickedMinute
            // Update selected date
            updateAnswerListener?.updateAnswer(
                position,
                ArrayList<String>().apply { add(dateAndTime) },
                questionNumberList
            )
            // Refresh date edit text UI
            notifyItemChanged(position)
        }

        // show time picker
        if (!timePicker.isVisible) {
            timePicker.show(
                (context as QuestionsActivity).supportFragmentManager,
                AppConstant.MATERIAL_TIME_PICKER
            )
        }
    }

    private inner class RadioBtnViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var question: TextView = itemView.findViewById(R.id.radio_btn_qus_title)
        var choiceRecycler: RecyclerView = itemView.findViewById(R.id.radio_options_recycler_view)
        var error: TextView = itemView.findViewById(R.id.radio_options_error)
        var choiceAdapter: ChoiceAdapter? = null

        fun bind(position: Int) {
            val recyclerViewModel = questionListItem[position]
            if (questionListItem[position].isMandatory) {
                question.text =
                    "Q${questionNumberList[position] + 1}. ${recyclerViewModel.question} *"
            } else {
                question.text =
                    "Q${questionNumberList[position] + 1}. ${recyclerViewModel.question}"
            }
            // Error visibility
            initialErrorVisibility(error, position)

            val selectedAnswer = selectedAnswerPositionMap[questionNumberList[position]]
            Log.d(
                "TAG",
                "bind: selectedAnswer radio $selectedAnswer $position ${questionNumberList[position]}"
            )
            // If selectedAnswer list is null replace empty list to send adapter
            val updateSelectedAnswer = selectedAnswer ?: ArrayList()
            Log.d("TAG", "bind: selectedAnswer radio af $updateSelectedAnswer")
            choiceAdapter = ChoiceAdapter(
                { it -> updateSelectedAnswerToMap(it, position) },
                questionListItem[position].choice!!,
                questionListItem[position].questionType,
                from,
                updateSelectedAnswer,
                questionBtnStatus, context
            )
            choiceRecycler.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            choiceRecycler.adapter = choiceAdapter
        }

        fun updateSelectedAnswerToMap(selectedAnswer: ArrayList<String>, position: Int) {
            Log.d(
                "TAG",
                "updateSelectedAnswerToMap: Radio button called ${position + 1} $selectedAnswer"
            )
            updateAnswerListener?.updateAnswer(position, selectedAnswer, questionNumberList)
            // Verify error visibility
            errorVisibilityAfterValue(error, selectedAnswer, position)
        }
    }

    private inner class DropDownViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var question: TextView = itemView.findViewById(R.id.drop_down_qus_title)
        var choiceRecycler: RecyclerView = itemView.findViewById(R.id.drop_down_recycler_view)
        var error: TextView = itemView.findViewById(R.id.drop_down_error)
        var choiceAdapter: ChoiceAdapter? = null

        fun bind(position: Int) {
            val recyclerModel = questionListItem[position]
            if (questionListItem[position].isMandatory) {
                question.text = "Q${questionNumberList[position] + 1}. ${recyclerModel.question} *"
            } else {
                question.text = "Q${questionNumberList[position] + 1}. ${recyclerModel.question}"
            }
            // Error visibility
            initialErrorVisibility(error, position)

            val selectedAnswer = selectedAnswerPositionMap[questionNumberList[position]]

            // If selectedAnswer list is null replace empty list to send adapter
            val updateSelectedAnswer = selectedAnswer ?: ArrayList()
            val updateChoiceList =
                questionListItem[position].choice ?: ArrayList<String>().apply { add("") }
            Log.d(
                "TAG",
                "onTextChanged: start drop $recyclerModel ,$position ${questionNumberList[position]} " +
                        "$selectedAnswer " +
                        "${selectedAnswerPositionMap[questionNumberList[position]]} ,$updateSelectedAnswer"
            )
            choiceAdapter = ChoiceAdapter(
                { it -> updateSelectedAnswerToMap(it, position) },
                updateChoiceList,
                questionListItem[position].questionType,
                from,
                updateSelectedAnswer,
                questionBtnStatus,
                context
            )
            choiceRecycler.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            choiceRecycler.adapter = choiceAdapter
        }

        fun updateSelectedAnswerToMap(selectedAnswer: ArrayList<String>, position: Int) {
            Log.d(
                "TAG", "updateSelectedAnswerToMap:edit text called ${position + 1} $selectedAnswer"
            )
            updateAnswerListener?.updateAnswer(position, selectedAnswer, questionNumberList)
            // Verify error visibility
            errorVisibilityAfterValue(error, selectedAnswer, position)
        }
    }

    private fun initialErrorVisibility(errorView: TextView, position: Int) {
        errorView.isVisible = mandatoryQuesErrorPositionList.contains(position)
    }

    private fun errorVisibilityAfterValue(
        errorView: TextView,
        selectedAnswer: ArrayList<String>,
        position: Int
    ) {
        if (questionListItem[position].isMandatory) {
            errorView.isVisible = selectedAnswer[0].isEmpty()
        }
    }

    private var updateAnswerListener: UpdateAnswerListener? = null

    // Initializing Listener Interface
    fun setUpdateAnswerListener(listener: UpdateAnswerListener) {
        updateAnswerListener = listener
    }

    // Interface For Invoice Click Listener
    interface UpdateAnswerListener {
        fun updateAnswer(
            position: Int, selectedAnswer: ArrayList<String>, questionNumberList: ArrayList<Int>
        )
    }
}