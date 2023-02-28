package com.smf.customer.view.myevents

import androidx.databinding.ViewDataBinding
import com.smf.customer.app.base.AppActivity
import com.smf.customer.app.constant.AppConstant
import com.smf.customer.di.sharedpreference.SharedPrefsHelper
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class MyEventsActivityTest{
    @MockK
      var myEventsActivity: MyEventsActivity= mockk(relaxed = true)
//    @InjectMockKs
//    var myEventsActivity1 = MyEventsActivity()
    @MockK
    var viewModel : MyEventsViewModel = mockk(relaxed = true)
    @MockK
    var sharedPrefsHelper : SharedPrefsHelper = mockk(relaxed = true)
    @MockK
    var dataBinding : ViewDataBinding = mockk(relaxed = true)
    @MockK
    var appActivity:AppActivity= mockk(relaxed = true)
    @BeforeAll
    fun setUp() {
        MockKAnnotations.init(this)

    }

    @Test
    fun eventSelectedNextClick() {
        every { myEventsActivity.onNextClick(1) } answers { callOriginal() }
        val type= myEventsActivity.onNextClick(1)
        Assertions.assertEquals(type, true)
    }
    @Test
    fun eventNotSelectedNextClick() {
        every { myEventsActivity.onNextClick(null) } answers { callOriginal() }
        val type= myEventsActivity.onNextClick(null)
        Assertions.assertEquals(type, false)
    }
}