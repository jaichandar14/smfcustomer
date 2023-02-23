package com.smf.customer.view.dashboard.fragment

import com.smf.customer.app.constant.AppConstant
import com.smf.customer.view.dashboard.responsedto.MyEventData
import io.mockk.*
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class MainDashBoardFragmentTest {

    @MockK
    private var mainDashBoardFragment: MainDashBoardFragment = mockk()

    @BeforeAll
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun onScreenRotation() {
        every { mainDashBoardFragment.onScreenRotation() } returns Unit
        mainDashBoardFragment.onScreenRotation()
        verify(exactly = 1) { mainDashBoardFragment.onScreenRotation() }
    }

    @Test
    fun dashBoardGetApiCall() {
        every { mainDashBoardFragment.dashBoardGetApiCall() } returns Unit
        mainDashBoardFragment.dashBoardGetApiCall()
        verify(exactly = 1) { mainDashBoardFragment.dashBoardGetApiCall() }
    }

    @Test
    fun onClickActive() {
        val mylist = MyEventData(1, 1, 1, 1, 1, 1, 1, 1, 1)
         every { mainDashBoardFragment.onClickTabItems(0, mylist) } returns AppConstant.APPROVED
        val type = mainDashBoardFragment.onClickTabItems(0, mylist)
        Assertions.assertEquals(type, AppConstant.APPROVED)
    }
    @Test
    fun onClickPending() {
        val mylist = MyEventData(1, 1, 1, 1, 1, 1, 1, 1, 1)
        every { mainDashBoardFragment.onClickTabItems(1, mylist) } returns AppConstant.PENDING_ADMIN_APPROVAL
        val type = mainDashBoardFragment.onClickTabItems(1, mylist)
        Assertions.assertEquals(type, AppConstant.PENDING_ADMIN_APPROVAL)
    }
    @Test
    fun onClickDraft() {
        val mylist = MyEventData(1, 1, 1, 1, 1, 1, 1, 1, 1)
        every { mainDashBoardFragment.onClickTabItems(2, mylist) } returns AppConstant.NEW
        val type = mainDashBoardFragment.onClickTabItems(2, mylist)
        Assertions.assertEquals(type, AppConstant.NEW)
    }
    @Test
    fun onClickRejected() {
        val mylist = MyEventData(1, 1, 1, 1, 1, 1, 1, 1, 1)
        every { mainDashBoardFragment.onClickTabItems(3, mylist) } returns AppConstant.REJECTED
        verify (inverse = true){
            mainDashBoardFragment.onClickTabItems(3, mylist)
        }
    }
    @Test
    fun onClickClosed() {
        val mylist = MyEventData(1, 1, 1, 1, 1, 1, 1, 1, 1)
        every { mainDashBoardFragment.onClickTabItems(4, mylist) } returns AppConstant.CLOSED
        val type = mainDashBoardFragment.onClickTabItems(4, mylist)
        Assertions.assertEquals(type,AppConstant.CLOSED
        )
    }
    @Test
    fun mEventOverviewRecycler(){
        every { mainDashBoardFragment.mEventOverviewRecycler(3) } returns Unit
        mainDashBoardFragment.mEventOverviewRecycler(3)
        verify(exactly = 1) { mainDashBoardFragment.mEventOverviewRecycler(3) }
    }
    @Test
    fun mServiceStatusRecycler(){
        every { mainDashBoardFragment.mServiceStatusRecycler() } returns Unit
        mainDashBoardFragment.mServiceStatusRecycler()
        verify(exactly = 1) { mainDashBoardFragment.mServiceStatusRecycler() }

    }
}