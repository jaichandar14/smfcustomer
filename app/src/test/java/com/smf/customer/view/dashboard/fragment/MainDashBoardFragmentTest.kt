package com.smf.customer.view.dashboard.fragment

import androidx.databinding.ViewDataBinding
import com.smf.customer.app.constant.AppConstant
import com.smf.customer.di.sharedpreference.SharedPrefsHelper
import com.smf.customer.view.dashboard.responsedto.MyEventData
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class MainDashBoardFragmentTest {

    @MockK
    private var mainDashBoardFragment: MainDashBoardFragment = mockk()
    private val myList = MyEventData(1, 1, 1, 1, 1, 1, 1, 1, 1)
    @InjectMockKs
    var mainDashBoardFragment1 = MainDashBoardFragment()
    @MockK
    var viewModel :MainDashBoardViewModel=mockk(relaxed = true)
    @MockK
    var sharedPrefsHelper :SharedPrefsHelper=mockk(relaxed = true)
    @MockK
    var dataBinding :ViewDataBinding=mockk(relaxed = true)


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
        every { mainDashBoardFragment.onClickTabItems(0, myList) } answers { callOriginal() }
        val type = mainDashBoardFragment1.onClickTabItems(0, myList)
        Assertions.assertEquals(type, AppConstant.APPROVED)
    }

    @Test
    fun onClickPending() {
        every { mainDashBoardFragment.onClickTabItems(1, myList) } answers { callOriginal() }
        val type = mainDashBoardFragment1.onClickTabItems(1, myList)
        Assertions.assertEquals(type, AppConstant.PENDING_ADMIN_APPROVAL)
    }

    @Test
    fun onClickDraft() {
        val mylist = MyEventData(1, 1, 1, 1, 1, 1, 1, 1, 1)
        every { mainDashBoardFragment.onClickTabItems(2, mylist) } answers { callOriginal() }
        val type = mainDashBoardFragment.onClickTabItems(2, mylist)
        Assertions.assertEquals(type, AppConstant.NEW)
    }

    @Test
    fun onClickRejected() {
        val mylist = MyEventData(1, 1, 1, 1, 1, 1, 1, 1, 1)
        every { mainDashBoardFragment.onClickTabItems(3, mylist) } answers { callOriginal() }
        verify(inverse = true) {
            mainDashBoardFragment.onClickTabItems(3, mylist)
        }
    }

    @Test
    fun onClickClosed() {
        val mylist = MyEventData(1, 1, 1, 1, 1, 1, 1, 1, 1)
        every { mainDashBoardFragment.onClickTabItems(4, mylist) } answers { callOriginal() }
        val type = mainDashBoardFragment.onClickTabItems(4, mylist)
        Assertions.assertEquals(
            type, AppConstant.CLOSED
        )
    }

    @Test
    fun mEventOverviewRecycler() {
        every { mainDashBoardFragment.mEventOverviewRecycler(3) } returns Unit
        mainDashBoardFragment.mEventOverviewRecycler(3)
        verify(exactly = 1) { mainDashBoardFragment.mEventOverviewRecycler(3) }
    }

    @Test
    fun mServiceStatusRecycler() {
        every { mainDashBoardFragment.mServiceStatusRecycler() } returns Unit
        mainDashBoardFragment.mServiceStatusRecycler()
        verify(exactly = 1) { mainDashBoardFragment.mServiceStatusRecycler() }

    }


}