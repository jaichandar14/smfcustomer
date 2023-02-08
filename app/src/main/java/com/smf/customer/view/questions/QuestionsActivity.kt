package com.smf.customer.view.questions

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smf.customer.R
import com.smf.customer.app.base.BaseActivity
import com.smf.customer.app.base.MyApplication
import com.smf.customer.app.constant.AppConstant
import com.smf.customer.data.model.dto.QuestionListItem
import com.smf.customer.databinding.ActivityQuestionsBinding
import com.smf.customer.view.eventDetails.EventDetailsActivity

class QuestionsActivity : BaseActivity<QuestionsViewModel>(),
    QuestionsAdapter.UpdateAnswerListener {
    lateinit var binding: ActivityQuestionsBinding
    lateinit var questionsRecycler: RecyclerView
    lateinit var questionsAdapter: QuestionsAdapter
    private var questionListItem = ArrayList<QuestionListItem>()
    lateinit var questionBtnStatus: String
    var questionNumberList = ArrayList<Int>()
    var fromActivity = ""

    companion object {
        var pagination = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_questions)
        viewModel = ViewModelProvider(this)[QuestionsViewModel::class.java]
        binding.questionsViewModel = viewModel
        binding.lifecycleOwner = this@QuestionsActivity
        MyApplication.applicationComponent.inject(this)
        init()
    }

    private fun init() {
        // set QuestionListItem
        setQuestionListItem()
        // Initialize recycler view
        questionsRecycler = binding.optionsRecyclerView
        // Set questions recyclerView Data
        setQuestionsRecycler()
        // Set button listener
        buttonListener()
        // Button visibility observer
        buttonVisibilityObserver()
    }

    private fun setQuestionsRecycler() {
        // Set initial questions
        val initialQuestions = setInitialQuestion()
        // Set initial questions numbers
        val questionNumbers = takeQuestionNumbers()
        // Updating pagination
        pagination = 1
        questionsAdapter = QuestionsAdapter(
            initialQuestions,
            viewModel.selectedAnswerMap,
            this,
            questionBtnStatus,
            questionNumbers,
            fromActivity
        )
        questionsRecycler.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        questionsRecycler.adapter = questionsAdapter
        questionsAdapter.setUpdateAnswerListener(this)
    }

    private fun setQuestionListItem() {
        questionListItem =
            intent.getSerializableExtra(AppConstant.QUESTION_LIST_ITEM) as ArrayList<QuestionListItem>
        questionNumberList =
            intent.getIntegerArrayListExtra(AppConstant.QUESTION_NUMBER_LIST) as ArrayList<Int>
        viewModel.selectedAnswerMap =
            intent.getSerializableExtra(AppConstant.SELECTED_ANSWER_MAP) as HashMap<Int, ArrayList<String>>
        fromActivity = intent.getStringExtra(AppConstant.FROM_ACTIVITY).toString()
        questionBtnStatus = intent.getStringExtra(AppConstant.QUESTION_BTN_TEXT) ?: ""
    }

    override fun updateAnswer(
        position: Int,
        selectedAnswer: ArrayList<String>,
        questionNumberList: ArrayList<Int>
    ) {
        viewModel.selectedAnswerMap[questionNumberList[position]] = selectedAnswer
        viewModel.updateSelectedAnswerLiveDate(viewModel.selectedAnswerMap)
        Log.d(TAG, "updateAnswer: ${viewModel.selectedAnswerMap}")
    }

    private fun buttonVisibilityObserver() {
        viewModel.selectedAnswerLiveDate.observe(this, Observer {
//            Log.d(TAG, "updateAnswer: $it")
        })
    }

    private fun buttonListener() {
        binding.nextBtn.setOnClickListener {
            // Update current page
            pagination += 1
            val endQuestion = (pagination) * 5

            if (questionListItem.size >= endQuestion) {
                // show next button
                nextBtnVisibility(true)
                // Show prev button
                prevBtnVisibility(true)
                // Display question number
                setQuestionNumber("${(endQuestion - 5) + 1}-$endQuestion")

                questionsAdapter.updateValues(
                    questionListItem.subList(endQuestion - 5, endQuestion)
                        .toMutableList() as ArrayList,
                    viewModel.selectedAnswerMap,
                    questionBtnStatus,
                    questionNumberList.subList(endQuestion - 5, endQuestion)
                        .toMutableList() as ArrayList
                )
            } else {
                // Hide next button
                nextBtnVisibility(false)
                // Show prev button
                prevBtnVisibility(true)
                // Display question number
                setQuestionNumber("${(endQuestion - 5) + 1}-${questionListItem.size}")

                questionsAdapter.updateValues(
                    questionListItem.subList(endQuestion - 5, questionListItem.size)
                        .toMutableList() as ArrayList,
                    viewModel.selectedAnswerMap,
                    questionBtnStatus,
                    questionNumberList.subList(endQuestion - 5, questionListItem.size)
                        .toMutableList() as ArrayList
                )
            }
        }

        binding.previousBtn.setOnClickListener {
            // Update current page
            pagination -= 1
            val endQuestion = (pagination) * 5
            Log.d(TAG, "buttonListener: preBtn $endQuestion $pagination")

            if ((endQuestion - 5) > 0) {
                // Preview button Visibility
                prevBtnVisibility(true)
                // Next button Visibility
                nextBtnVisibility(true)
                // Display question number
                setQuestionNumber("${(endQuestion - 5) + 1}-$endQuestion")

                questionsAdapter.updateValues(
                    questionListItem.subList(endQuestion - 5, endQuestion)
                        .toMutableList() as ArrayList,
                    viewModel.selectedAnswerMap,
                    questionBtnStatus,
                    questionNumberList.subList(endQuestion - 5, endQuestion)
                        .toMutableList() as ArrayList
                )
            } else {
                // Preview button Visibility
                prevBtnVisibility(false)
                // Next button Visibility
                nextBtnVisibility(true)
                // Display question number
                setQuestionNumber("1-$endQuestion")

                questionsAdapter.updateValues(
                    questionListItem.subList(0, endQuestion).toMutableList() as ArrayList,
                    viewModel.selectedAnswerMap,
                    questionBtnStatus,
                    questionNumberList.subList(0, endQuestion).toMutableList() as ArrayList
                )
            }
        }
        binding.cancelBtn.setOnClickListener {
            // Method for move event details page
            backToEventDetailsPage()
        }
        binding.saveBtn.setOnClickListener {
            // Method for move event details page
            backToEventDetailsPage()
        }
    }

    override fun onBackPressed() {
        // Method for move event details page
        backToEventDetailsPage()
    }

    private fun backToEventDetailsPage() {
        val intent = Intent(this, EventDetailsActivity::class.java)
        intent.putExtra(AppConstant.EVENT_QUESTIONS, AppConstant.EVENT_QUESTIONS)
        intent.putExtra(AppConstant.SELECTED_ANSWER_MAP, viewModel.selectedAnswerMap)
        startActivity(intent)
        finish()
    }

    private fun setInitialQuestion(): ArrayList<QuestionListItem> {
        return if (questionListItem.size >= 5) {
            // Set question number
            setQuestionNumber("1-5")
            // Initial next button visibility
            if (questionListItem.size <= 5) {
                // Hide Next button
                nextBtnVisibility(false)
            } else {
                // Show Next button
                nextBtnVisibility(true)
            }
            questionListItem.subList(0, 5).toList() as ArrayList
        } else {
            // Set question number
            setQuestionNumber("1-${questionListItem.size}")
            questionListItem
        }
    }

    private fun takeQuestionNumbers(): ArrayList<Int> {
        return if (questionListItem.size >= 5) {
            questionNumberList.subList(0, 5).toMutableList() as ArrayList<Int>
        } else {
            questionNumberList.subList(0, questionListItem.size).toMutableList() as ArrayList<Int>
        }
    }

    private fun nextBtnVisibility(status: Boolean) {
        if (status) {
            binding.nextBtn.visibility = View.VISIBLE
        } else {
            binding.nextBtn.visibility = View.INVISIBLE
        }
    }

    private fun prevBtnVisibility(status: Boolean) {
        if (status) {
            binding.previousBtn.visibility = View.VISIBLE
        } else {
            binding.previousBtn.visibility = View.INVISIBLE
        }
    }

    private fun setQuestionNumber(questionNumber: String) {
        binding.quesNumberText.text = "$questionNumber of ${questionListItem.size}"
    }

}