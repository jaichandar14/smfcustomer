package com.smf.customer.view.vieweventdetails.adaptor

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.smf.customer.R

class QuesAndAnsAdaptor(val context: Context) :
    RecyclerView.Adapter<QuesAndAnsAdaptor.QusAndAnsViewHolder>() {

    var questions = ArrayList<String>()
    var answers = ArrayList<String>()

    @SuppressLint("NotifyDataSetChanged")
    fun updateQuesAndAnsList(
        questions: ArrayList<String>,
        answers: ArrayList<String>
    ) {
        this.questions = questions
        this.answers = answers
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QusAndAnsViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(
            R.layout.view_question_layout, parent, false
        )
        return QusAndAnsViewHolder(view)
    }

    override fun onBindViewHolder(holder: QusAndAnsViewHolder, position: Int) {
        holder.setData(position)
    }

    override fun getItemCount(): Int {
        return questions.size
    }

    inner class QusAndAnsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var questionNumber: TextView = view.findViewById(R.id.question_number_text)
        private var questionText: TextView = view.findViewById(R.id.question_text)
        var answerText: TextView = view.findViewById(R.id.answer_text)

        @SuppressLint("SetTextI18n")
        fun setData(position: Int) {
            questionNumber.text = "Q${position + 1}. "
            questionText.text = questions[position]
            answerText.text = "Ans: ${answers[position]}"
        }

    }
}