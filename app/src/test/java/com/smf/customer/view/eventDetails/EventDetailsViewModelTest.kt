package com.smf.customer.view.eventDetails

import com.smf.customer.InstantExecutorExtension
import com.smf.customer.data.model.dto.QuestionListItem
import com.smf.customer.data.model.request.EventInfoDTO
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantExecutorExtension::class)
internal class EventDetailsViewModelTest {

    @MockK
    private var eventDetailsViewModelMockk: EventDetailsViewModel = mockk(relaxed = true)

    @MockK
    private lateinit var eventInfoDTO: EventInfoDTO

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    @DisplayName(
        "Verification for verifyMandatoryQuesAnswered method with empty" +
                " questionListItem and empty eventSelectedAnswerMap"
    )
    fun verifyMandatoryQuesAnswered() {
        val questionListItem = ArrayList<QuestionListItem>()
        val eventSelectedAnswerMap = HashMap<Int, ArrayList<String>>()
        // Parameter questionListItem represents questions
        // Parameter eventSelectedAnswerMap represents answered questions
        every {
            eventDetailsViewModelMockk.verifyMandatoryQuesAnswered(
                questionListItem,
                eventSelectedAnswerMap
            )
        } answers { callOriginal() }
        Assertions.assertEquals(
            eventDetailsViewModelMockk.verifyMandatoryQuesAnswered(
                questionListItem,
                eventSelectedAnswerMap
            ),
            true
        )
    }

    @Test
    @DisplayName(
        "Verification for verifyMandatoryQuesAnswered method " +
                "questionListItem value with mandatory question value true" +
                " and empty eventSelectedAnswerMap"
    )
    fun verifyMandatoryQuesAnsweredOne() {
        val questionListItem = ArrayList<QuestionListItem>()
        questionListItem.add(QuestionListItem("", ArrayList(), "", true))
        val eventSelectedAnswerMap = HashMap<Int, ArrayList<String>>()
        // Parameter questionListItem represents questions
        // Parameter eventSelectedAnswerMap represents answered questions
        every {
            eventDetailsViewModelMockk.verifyMandatoryQuesAnswered(
                questionListItem,
                eventSelectedAnswerMap
            )
        } answers { callOriginal() }
        Assertions.assertEquals(
            eventDetailsViewModelMockk.verifyMandatoryQuesAnswered(
                questionListItem,
                eventSelectedAnswerMap
            ),
            false
        )
    }

    @Test
    @DisplayName(
        "Verification for verifyMandatoryQuesAnswered method " +
                "questionListItem value with mandatory question value true" +
                " and empty eventSelectedAnswerMap"
    )
    fun verifyMandatoryQuesAnsweredTwo() {
        val questionListItem = ArrayList<QuestionListItem>()
        questionListItem.add(QuestionListItem("", ArrayList(), "", false))
        val eventSelectedAnswerMap = HashMap<Int, ArrayList<String>>()
        // Parameter questionListItem represents questions
        // Parameter eventSelectedAnswerMap represents answered questions
        every {
            eventDetailsViewModelMockk.verifyMandatoryQuesAnswered(
                questionListItem,
                eventSelectedAnswerMap
            )
        } answers { callOriginal() }
        Assertions.assertEquals(
            eventDetailsViewModelMockk.verifyMandatoryQuesAnswered(
                questionListItem,
                eventSelectedAnswerMap
            ),
            true
        )
    }

    @Test
    @DisplayName("postEventInfo method callable verification")
    fun postEventInfo() {
        // Parameter eventInfoDTO object to post value to the api
        every { eventDetailsViewModelMockk.postEventInfo(eventInfoDTO) } returns Unit
        eventDetailsViewModelMockk.postEventInfo(eventInfoDTO)
        verify(exactly = 1) { eventDetailsViewModelMockk.postEventInfo(eventInfoDTO) }
    }

    @Test
    @DisplayName("getEventDetailsQuestions method callable verification")
    fun getEventDetailsQuestions() {
        // Parameter eventTemplateId get events
        every { eventDetailsViewModelMockk.getEventDetailsQuestions(102) } returns Unit
        eventDetailsViewModelMockk.getEventDetailsQuestions(102)
        verify(exactly = 1) { eventDetailsViewModelMockk.getEventDetailsQuestions(102) }
    }

}