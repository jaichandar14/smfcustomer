package com.smf.customer.view.questions

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smf.customer.R
import com.smf.customer.app.base.BaseActivity
import com.smf.customer.app.base.MyApplication
import com.smf.customer.app.constant.AppConstant
import com.smf.customer.data.model.dto.QuestionListItem
import com.smf.customer.databinding.ActivityQuestionsBinding
import com.smf.customer.di.sharedpreference.SharedPrefConstant
import com.smf.customer.di.sharedpreference.SharedPrefsHelper
import com.smf.customer.view.eventDetails.EventDetailsActivity
import com.smf.customer.view.provideservicedetails.ProvideServiceDetailsActivity
import javax.inject.Inject

class QuestionsActivity : BaseActivity<QuestionsViewModel>(),
    QuestionsAdapter.UpdateAnswerListener {
    lateinit var binding: ActivityQuestionsBinding
    lateinit var questionsRecycler: RecyclerView
    lateinit var questionsAdapter: QuestionsAdapter
    private var questionListItem = ArrayList<QuestionListItem>()
    lateinit var questionBtnStatus: String
    var questionNumberList = ArrayList<Int>()
    var fromActivity = ""

    // For current page questions
    var currentPageQuesList = ArrayList<QuestionListItem>()

    // For current page question number
    var currentPageQuesNumberList = ArrayList<Int>()

    companion object {
        var pagination = 0
    }

    @Inject
    lateinit var sharedPrefsHelper: SharedPrefsHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_questions)
        viewModel = ViewModelProvider(this)[QuestionsViewModel::class.java]
        binding.questionsViewModel = viewModel
        binding.lifecycleOwner = this@QuestionsActivity
        MyApplication.applicationComponent?.inject(this)
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
        setButtonVisibility()
    }

    private fun setQuestionsRecycler() {
        // Set initial questions
        currentPageQuesList = setInitialQuestion()
        // Set initial questions numbers
        currentPageQuesNumberList = takeQuestionNumbers()
        // Updating initial pagination
        pagination = 1
        questionsAdapter = QuestionsAdapter(
            currentPageQuesList,
            viewModel.selectedAnswerMap,
            this,
            questionBtnStatus,
            currentPageQuesNumberList,
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

    private fun setButtonVisibility() {
        if (questionBtnStatus.contains(getString(R.string.view_order))) {
            binding.closeBtn.visibility = View.VISIBLE
            binding.saveBtn.visibility = View.GONE
            binding.cancelBtn.visibility = View.GONE
        } else {
            binding.closeBtn.visibility = View.GONE
            binding.saveBtn.visibility = View.VISIBLE
            binding.cancelBtn.visibility = View.VISIBLE
        }
        // Check all mandatory questions are answered
        submitBtnColorVisibility()
    }

    private fun buttonListener() {
        binding.nextBtn.setOnClickListener {
            if (questionBtnStatus.contains(getString(R.string.view_order))) {
                // Show next page without edit
                goToNextPage()
            } else {
                if (mandatoryQuesValidation()) {
                    // Show next page
                    goToNextPage()
                }
            }

        }
        binding.previousBtn.setOnClickListener {
            // Show previous page
            goToPreviousPage()
        }
        binding.cancelBtn.setOnClickListener {
            // Method for move event details page
            backToDetailsPage()
        }
        binding.saveBtn.setOnClickListener {
            // Verify current page mandatory questions answered
            if (mandatoryQuesValidation().not()) {
                return@setOnClickListener
            }
            // Verify all mandatory questions answered
            if (verifyAllMandatoryQuesAnswered()) {
                // Method for move event details page
                backToDetailsPage()
            } else {
                viewModel.showToastMessage(getString(R.string.please_answer_all_mandatory_questions))
            }
        }
        binding.closeBtn.setOnClickListener {
            // Method for move event details page
            backToDetailsPage()
        }
    }

    override fun onBackPressed() {
        // Method for move event details page
        backToDetailsPage()
    }

    private fun backToDetailsPage() {
        if (fromActivity == AppConstant.EVENT_DETAILS_ACTIVITY) {
            // Update selectedAnswerMap for event details page
            sharedPrefsHelper.putHashMap(
                SharedPrefConstant.EVENT_SELECTED_ANSWER_MAP,
                viewModel.selectedAnswerMap
            )
            Intent(this, EventDetailsActivity::class.java).apply {
                this.putExtra(AppConstant.EVENT_QUESTIONS, AppConstant.EVENT_QUESTIONS)
                startActivity(this)
            }
        } else {
            // Update selectedAnswerMap for service details page
            sharedPrefsHelper.putHashMap(
                SharedPrefConstant.SERVICE_SELECTED_ANSWER_MAP,
                viewModel.selectedAnswerMap
            )
            Intent(this, ProvideServiceDetailsActivity::class.java).apply {
                this.putExtra(AppConstant.SERVICE_QUESTIONS, AppConstant.SERVICE_QUESTIONS)
                startActivity(this)
            }
        }
        finish()
    }

    private fun mandatoryQuesValidation(): Boolean {
        // Verify all mandatory questions answered
        val mandatoryQuesErrorPositionList = getMandatoryQuesErrorPositionList()
        return if (mandatoryQuesErrorPositionList.isNotEmpty()) {
            // Update recycler view with Error Values
            updateAdapterValues(mandatoryQuesErrorPositionList)
            // Scroll to top error position
            questionsRecycler.smoothScrollToPosition(mandatoryQuesErrorPositionList[0])
            false
        } else {
            true
        }
    }

    private fun getMandatoryQuesErrorPositionList(): ArrayList<Int> {
        val mandatoryQuesErrorPositionList = ArrayList<Int>().apply {
            // Here update and return current page recycler view list position
            currentPageQuesList.forEach {
                if (it.isMandatory &&
                    viewModel.selectedAnswerMap.containsKey(questionListItem.indexOf(it)).not()
                ) {
                    this.add(currentPageQuesList.indexOf(it))
                }
            }
        }
        return mandatoryQuesErrorPositionList
    }

    private fun goToNextPage() {
        // Update current page
        pagination += 1
        val endQuestion = (pagination) * 5

        if (questionListItem.size > endQuestion) {
            // show next button
            nextBtnVisibility(true)
            // Show prev button
            prevBtnVisibility(true)
            // Display question number
            setQuestionNumber("${(endQuestion - 5) + 1}-$endQuestion")

            updateCurrentPageDetails(
                questionListItem.subList(endQuestion - 5, endQuestion)
                    .toMutableList() as ArrayList,
                questionNumberList.subList(endQuestion - 5, endQuestion)
                    .toMutableList() as ArrayList
            )

            updateAdapterValues()
        } else {
            // Hide next button
            nextBtnVisibility(false)
            // Show prev button
            prevBtnVisibility(true)
            // Display question number
            setQuestionNumber("${(endQuestion - 5) + 1}-${questionListItem.size}")

            updateCurrentPageDetails(
                questionListItem.subList(endQuestion - 5, questionListItem.size)
                    .toMutableList() as ArrayList,
                questionNumberList.subList(endQuestion - 5, questionListItem.size)
                    .toMutableList() as ArrayList
            )

            updateAdapterValues()
        }
    }

    private fun goToPreviousPage() {
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

            updateCurrentPageDetails(
                questionListItem.subList(endQuestion - 5, endQuestion)
                    .toMutableList() as ArrayList,
                questionNumberList.subList(endQuestion - 5, endQuestion)
                    .toMutableList() as ArrayList
            )

            updateAdapterValues()
        } else {
            // Preview button Visibility
            prevBtnVisibility(false)
            // Next button Visibility
            nextBtnVisibility(true)
            // Display question number
            setQuestionNumber("1-$endQuestion")

            updateCurrentPageDetails(
                questionListItem.subList(0, endQuestion).toMutableList() as ArrayList,
                questionNumberList.subList(0, endQuestion).toMutableList() as ArrayList
            )

            updateAdapterValues()
        }
    }

    private fun updateCurrentPageDetails(
        currentPageQuesList: ArrayList<QuestionListItem>,
        currentPageQuesNumberList: ArrayList<Int>
    ) {
        this.currentPageQuesList = currentPageQuesList
        this.currentPageQuesNumberList = currentPageQuesNumberList
    }

    // Update recyclerView values
    private fun updateAdapterValues(mandatoryQuesErrorPositionList: ArrayList<Int> = ArrayList()) {
        questionsAdapter.updateValues(
            currentPageQuesList,
            viewModel.selectedAnswerMap,
            questionBtnStatus,
            currentPageQuesNumberList,
            mandatoryQuesErrorPositionList
        )
        Log.d(
            TAG,
            "getMandatoryQuesErrorPositionList: update values $currentPageQuesList $currentPageQuesNumberList"
        )
    }

    // Update user selected answer from recycler view UI
    override fun updateAnswer(
        position: Int,
        selectedAnswer: ArrayList<String>,
        questionNumberList: ArrayList<Int>
    ) {
        if (selectedAnswer.isNotEmpty() && selectedAnswer[0].isEmpty()) {
            viewModel.selectedAnswerMap.remove(questionNumberList[position])
        } else {
            viewModel.selectedAnswerMap[questionNumberList[position]] = selectedAnswer
        }
        // Check all mandatory questions are answered
        submitBtnColorVisibility()
    }

    private fun submitBtnColorVisibility() {
        // Verify all mandatory questions are answered
        if (verifyAllMandatoryQuesAnswered()) {
            binding.saveBtn.background = getDrawable(R.drawable.custom_button_corner_ok)
            binding.saveBtn.setTextColor(ContextCompat.getColor(this, R.color.white))
        } else {
            binding.saveBtn.background = getDrawable(R.drawable.custom_button_corner_ok_fade)
            binding.saveBtn.setTextColor(ContextCompat.getColor(this, R.color.gray_text))
        }
    }

    private fun verifyAllMandatoryQuesAnswered(): Boolean {
        val mandatoryQuesIndexList = ArrayList<Int>().apply {
            questionListItem.filter { it.isMandatory }.forEach {
                add(questionListItem.indexOf(it))
            }
        }
        // Verify all mandatory questions are answered
        return viewModel.selectedAnswerMap.keys.containsAll(mandatoryQuesIndexList)
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