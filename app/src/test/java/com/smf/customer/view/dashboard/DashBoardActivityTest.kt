package com.smf.customer.view.dashboard


import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class DashBoardActivityTest {

    @MockK
    private var dashBoardActivity: DashBoardActivity = mockk()

    @BeforeAll
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun mainFragment() {
        every { dashBoardActivity.mainFragment() } returns Unit
        dashBoardActivity.mainFragment()
        verify(exactly = 1) { dashBoardActivity.mainFragment() }
    }

    @Test
    fun eventListFragment() {
        every { dashBoardActivity.eventListFragment() } returns Unit
        dashBoardActivity.eventListFragment()
        verify(exactly = 1) { dashBoardActivity.eventListFragment() }
    }
}