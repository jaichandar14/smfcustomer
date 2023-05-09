package com.smf.customer.view.vieweventdetails

import com.smf.customer.app.base.BaseViewModel
import com.smf.customer.data.model.dto.ViewEventDetails
import com.smf.customer.data.model.response.QuestionnaireWrapperDto

class ViewEventDetailsViewModel : BaseViewModel() {

    var viewEventDetails: ViewEventDetails? = null

    fun getQuesListItem(
        eventQuestionMetaDataDto: QuestionnaireWrapperDto
    ): ArrayList<String> {
        return ArrayList<String>().apply {
            eventQuestionMetaDataDto.questionnaireDtos.forEach {
                this.add(it.questionMetadata.question)
            }
        }
    }

    fun getAnswers(
        eventQuestionMetaDataDto: QuestionnaireWrapperDto
    ): ArrayList<String> {
        return ArrayList<String>().apply {
            eventQuestionMetaDataDto.questionnaireDtos.forEach { questionnaireDto ->
                this.add(questionnaireDto.questionMetadata.answer ?: "")
            }
        }
    }
}