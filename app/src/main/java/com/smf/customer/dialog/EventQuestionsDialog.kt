package com.smf.customer.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.smf.customer.R
import com.smf.customer.app.base.BaseDialogFragment
import com.smf.customer.app.constant.AppConstant
import com.smf.customer.app.listener.AdapterOneClickListener
import com.smf.customer.data.model.dto.QuestionListItem
import com.smf.customer.databinding.DialogEventQuestionsListBinding
import com.smf.customer.listener.DialogThreeButtonListener

class EventQuestionsDialog : BaseDialogFragment(), AdapterOneClickListener {
    var TAG: String = this.javaClass.simpleName
    private lateinit var threeButtonListener: DialogThreeButtonListener
    private lateinit var eventQuestionsCallback: EventQuestionsCallback
    private lateinit var dataBinding: DialogEventQuestionsListBinding

    private var questionList = ArrayList<QuestionListItem>()
    private var selectedAnswerPositionMap = HashMap<Int, Int>()
    private var listItemAdapter = EventQusListAdapter(this)
    private var questionNumber: Int = 0

    companion object {
        private const val QUS_LIST_ITEM = "QUS_LIST_ITEM"
        private const val SELECTED_ANS_LIST = "SELECTED_ANS_LIST"
        private const val QUESTION_NUMBER = "QUESTION_NUMBER"
        private const val QUESTION_BTN_STATUS = "QUESTION_BTN_STATUS"
        fun newInstance(
            questionListItem: ArrayList<QuestionListItem>,
            questionBtnStatus: String,
            selectedAnswerPositionMap: HashMap<Int, Int>,
            questionNumber: Int,
            threeButtonListener: DialogThreeButtonListener,
            eventQuestionsCallback: EventQuestionsCallback
        ): EventQuestionsDialog {
            val args = Bundle()
            args.putSerializable(QUS_LIST_ITEM, questionListItem)
            args.putSerializable(QUESTION_BTN_STATUS, questionBtnStatus)
            args.putSerializable(SELECTED_ANS_LIST, selectedAnswerPositionMap)
            args.putSerializable(QUESTION_NUMBER, questionNumber)
            val eventQuestionsDialog = EventQuestionsDialog()
            eventQuestionsDialog.arguments = args
            eventQuestionsDialog.isCancelable = false
            eventQuestionsDialog.threeButtonListener = threeButtonListener
            eventQuestionsDialog.eventQuestionsCallback = eventQuestionsCallback
            return eventQuestionsDialog
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        dataBinding = DataBindingUtil.inflate(
            inflater, R.layout.dialog_event_questions_list, container, false
        )
        return dataBinding.root
    }

    override fun onStart() {
        super.onStart()
        // Setting Dialog Fragment Size
        dialogFragmentSize()
    }

    override fun setData() {
        // Options recyclerView initial setup
        dataBinding.optionsRecyclerView.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
        dataBinding.optionsRecyclerView.adapter = listItemAdapter
        // Setting data for local variables
        questionList =
            requireArguments().getSerializable(QUS_LIST_ITEM) as ArrayList<QuestionListItem>
        selectedAnswerPositionMap =
            requireArguments().getSerializable(SELECTED_ANS_LIST) as HashMap<Int, Int>
        questionNumber = requireArguments().getSerializable(QUESTION_NUMBER) as Int

        // Display Initial questions
        // Verify answer selected or not
        if (selectedAnswerPositionMap.containsKey(questionNumber)) {
            // If already answered then send specific position
            showQuestions(selectedAnswerPositionMap[questionNumber])
        } else {
            // display initial question
            showQuestions(null)
        }
    }

    private fun showQuestions(selectedPosition: Int?) {
        // Method for Previous button visibility
        previousBtnVisibility()
        // Method for Next button visibility
        nextBtnVisibility()
        // Method for cancel button text
        setCancelBtnText()
        while (questionNumber < questionList.size) {
            dataBinding.question.text = questionList[questionNumber].question
            listItemAdapter.setDialogListItemList(
                questionList[questionNumber].choice,
                selectedPosition,
                requireArguments().getSerializable(QUESTION_BTN_STATUS) as String,
                questionList[questionNumber].questionType
            )
            break
        }
    }

    override fun setupClickListeners() {
        dataBinding.nextBtn.setOnClickListener {
            // Verify answer selected or not
            if (selectedAnswerPositionMap.containsKey(questionNumber)) {
                // Increase question number
                questionNumber += 1
                // Verify questionNumber is not greater than questionList
                if (questionNumber < questionList.size) {
                    // Verify question answered or not for send position value
                    if (selectedAnswerPositionMap.containsKey(questionNumber)) {
                        // If already answered then send specific position
                        showQuestions(selectedAnswerPositionMap[questionNumber])
                    } else {
                        // If not selected then send null position value
                        showQuestions(null)
                    }
                } else {
                    questionNumber -= 1
                }
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.please_select_the_answer),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        dataBinding.previousBtn.setOnClickListener {
            // Decrease question number
            questionNumber -= 1
            if (questionNumber < questionList.size && questionNumber >= 0) {
                // Send previous question answer position
                if (selectedAnswerPositionMap.containsKey(questionNumber)) {
                    showQuestions(selectedAnswerPositionMap[questionNumber])
                }
            } else {
                questionNumber += 1
            }
        }

        dataBinding.cancelBtn.setOnClickListener {
            if (dataBinding.cancelBtn.text.toString() != AppConstant.CLOSE) {
                // Update button status and current question number
                eventQuestionsCallback.dialogStatus(
                    dataBinding.cancelBtn.text.toString(), questionNumber
                )
            }
            // Update dialog flag
            threeButtonListener.onCancelClick(this)
            this.dismiss()
        }
    }

    override fun onOneClick(position: Int) {
        Log.d(TAG, "onOneClick: $questionNumber $position")
        // Update Selected answer inside dialog
        selectedAnswerPositionMap[questionNumber] = position
        // Update Selected answer to Activity
        eventQuestionsCallback.updateSelectedAnswer(questionNumber, position)
        // Method for changing cancel button text
        setCancelBtnText()
    }

    override fun onPause() {
        super.onPause()
        // Update current question Number while dialog dismiss
        eventQuestionsCallback.updateQusNumberOnScreenRotation(questionNumber, this)
        dismissAllowingStateLoss()
    }

    private fun previousBtnVisibility() {
        if (questionNumber == 0) {
            dataBinding.previousBtn.visibility = View.GONE
        } else {
            dataBinding.previousBtn.visibility = View.VISIBLE
        }
    }

    private fun nextBtnVisibility() {
        if (questionNumber == questionList.size - 1) {
            dataBinding.nextBtn.visibility = View.GONE
        } else {
            dataBinding.nextBtn.visibility = View.VISIBLE
        }
    }

    private fun setCancelBtnText() {
        if ((requireArguments()
                .getSerializable(QUESTION_BTN_STATUS) as String)
                .contains(getString(R.string.view_order))
        ) {
            dataBinding.cancelBtn.text = AppConstant.CLOSE
        } else {
            if (questionNumber == questionList.size - 1) {
                // Verify answer selected or not
                if (selectedAnswerPositionMap.containsKey(questionNumber)) {
                    dataBinding.cancelBtn.text = AppConstant.SUBMIT
                } else {
                    dataBinding.cancelBtn.text = AppConstant.CANCEL
                }
            } else {
                dataBinding.cancelBtn.text = AppConstant.CANCEL
            }
        }
    }

    // Setting Dialog Fragment Size
    private fun dialogFragmentSize() {
        val window: Window? = dialog?.window
        val params: WindowManager.LayoutParams = window!!.attributes
        params.width = ((resources.displayMetrics.widthPixels * 0.9).toInt())
        window.attributes = params
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }
}