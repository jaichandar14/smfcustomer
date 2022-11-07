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
import com.smf.customer.app.listener.AdapterOneClickListener
import com.smf.customer.data.model.dto.QuestionListItem
import com.smf.customer.databinding.DialogEventQuestionsListBinding
import com.smf.customer.listener.DialogThreeButtonListener
import com.smf.customer.view.eventDetails.EventDetailsActivity

class EventQuestionsDialog : BaseDialogFragment(), AdapterOneClickListener {

    private lateinit var threeButtonListener: DialogThreeButtonListener
    private lateinit var dataBinding: DialogEventQuestionsListBinding

    var questionList = ArrayList<QuestionListItem>()
    var listItemAdapter = EventQusListAdapter(this)
    private var questionNumber: Int = 0

    companion object {
        private const val QUS_LIST_ITEM = "QUS_LIST_ITEM"

        fun newInstance(
            questionListItem: ArrayList<QuestionListItem>,
            threeButtonListener: DialogThreeButtonListener
        ): EventQuestionsDialog {
            val args = Bundle()
            args.putSerializable(QUS_LIST_ITEM, questionListItem)
            val eventQuestionsDialog = EventQuestionsDialog()
            eventQuestionsDialog.arguments = args
            eventQuestionsDialog.isCancelable = false
            eventQuestionsDialog.threeButtonListener = threeButtonListener
            eventQuestionsDialog.dialogDismissListener = threeButtonListener
            return eventQuestionsDialog
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.dialog_event_questions_list,
                container,
                false
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

        questionList =
            requireArguments().getSerializable(QUS_LIST_ITEM) as ArrayList<QuestionListItem>

        // Display Initial questions
        // Verify answer selected or not
        if (EventDetailsActivity.selectedAnswerPositionMap.containsKey(questionNumber)) {
            // If already answered then send specific position
            showQuestions(EventDetailsActivity.selectedAnswerPositionMap[questionNumber])
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
                selectedPosition
            )
            break
        }
    }

    override fun setupClickListeners() {
        dataBinding.nextBtn.setOnClickListener {
            threeButtonListener.onPositiveClick(this)
            // Verify answer selected or not
            if (EventDetailsActivity.selectedAnswerPositionMap.containsKey(questionNumber)) {
                // Increase question number
                questionNumber += 1
                // Verify questionNumber is not greater than questionList
                if (questionNumber < questionList.size) {
                    // Verify question answered or not for send position value
                    if (EventDetailsActivity.selectedAnswerPositionMap.containsKey(questionNumber)) {
                        // If already answered then send specific position
                        showQuestions(EventDetailsActivity.selectedAnswerPositionMap[questionNumber])
                    } else {
                        // If not selected then send null position value
                        showQuestions(null)
                    }
                } else {
                    questionNumber -= 1
                }
            } else {
                Toast.makeText(requireContext(), "Please select the answer", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        dataBinding.previousBtn.setOnClickListener {
            threeButtonListener.onNegativeClick(this)
            // Decrease question number
            questionNumber -= 1
            if (questionNumber < questionList.size && questionNumber >= 0) {
                // Send previous question answer position
                if (EventDetailsActivity.selectedAnswerPositionMap.containsKey(questionNumber)) {
                    showQuestions(EventDetailsActivity.selectedAnswerPositionMap[questionNumber])
                }
            } else {
                questionNumber += 1
            }
        }

        dataBinding.cancelBtn.setOnClickListener {
            threeButtonListener.onCancelClick(this)
            this.dismiss()
//            // Verify answer selected or not
//            if (EventDetailsActivity.selectedAnswerPositionMap.containsKey(questionNumber)) {
//                this.dismiss()
//            } else {
//                this.dismiss()
//            }
        }
    }

    override fun onOneClick(position: Int) {
        Log.d("TAG", "onOneClick: $questionNumber $position")
        // Update Selected answer to EventDetailsActivity
        EventDetailsActivity.selectedAnswerPositionMap[questionNumber] = position

        // Method for changing cancel button text
        setCancelBtnText()
    }

    override fun onPause() {
        super.onPause()
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
        if (questionNumber == questionList.size - 1) {
            // Verify answer selected or not
            if (EventDetailsActivity.selectedAnswerPositionMap.containsKey(questionNumber)) {
                dataBinding.cancelBtn.text = "Submit"
            } else {
                dataBinding.cancelBtn.text = "Cancel"
            }
        } else {
            dataBinding.cancelBtn.text = "Cancel"
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