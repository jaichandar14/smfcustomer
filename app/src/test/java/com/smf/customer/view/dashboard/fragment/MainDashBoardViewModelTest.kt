package com.smf.customer.view.dashboard.fragment

import com.smf.customer.app.constant.AppConstant
import com.smf.customer.view.dashboard.DashBoardActivity
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class MainDashBoardViewModelTest {
    @MockK
    private var mainDashBoardViewModel: MainDashBoardViewModel = mockk()

    @BeforeAll
    fun setUp() {
        MockKAnnotations.init(this)

    }
    @Test
    fun getEventCount() {
        every { mainDashBoardViewModel.getEventCount("jcjavenu540345") } returns Unit
        mainDashBoardViewModel.getEventCount("jcjavenu540345")
        verify(exactly = 1) { mainDashBoardViewModel.getEventCount("jcjavenu540345") }
    }

    @Test
    fun getEventStatus() {
        val mEventStatusList = ArrayList<String>()
        mEventStatusList.add(AppConstant.APPROVED)
        every { mainDashBoardViewModel.getEventStatus("jcjavenu540345",mEventStatusList) } returns Unit
        mainDashBoardViewModel.getEventStatus("jcjavenu540345",mEventStatusList)
        assertAll(  {verify(exactly = 1) { mainDashBoardViewModel.getEventStatus("jcjavenu540345",mEventStatusList)}  })
    }
}