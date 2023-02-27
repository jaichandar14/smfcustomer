package com.smf.customer.view.emailotp

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

internal class EmailOTPViewModelTest {

    @MockK
    private lateinit var emailOTPViewModel: EmailOTPViewModel

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    @DisplayName("otpTimerValidation method callable verification")
    fun otpTimerValidation() {
        // mDataBinding variable for make resend textview button clickable
        // userName variable for sending otp to current user
        every { emailOTPViewModel.otpTimerValidation(null, "") } returns Unit
        emailOTPViewModel.otpTimerValidation(null, "")
        verify(exactly = 1) { emailOTPViewModel.otpTimerValidation(null, "") }
    }

    @Test
    @DisplayName("reSendOTP method callable verification")
    fun reSendOTP() {
        // userName variable for resending otp to current user
        every { emailOTPViewModel.reSendOTP("") } returns Unit
        emailOTPViewModel.reSendOTP("")
        verify(exactly = 1) { emailOTPViewModel.reSendOTP("") }
    }

    @Test
    @DisplayName("loginUser method callable verification")
    fun loginUser() {
        // isValid variable for verify user completes all verifications
        // userId variable for login specific user
        every { emailOTPViewModel.loginUser(true, "userId") } returns Unit
        emailOTPViewModel.loginUser(true, "userId")
        verify(exactly = 1) { emailOTPViewModel.loginUser(true, "userId") }
    }

    @Test
    @DisplayName("getLoginInfo method callable verification")
    fun getLoginInfo() {
        every { emailOTPViewModel.getLoginInfo() } returns Unit
        emailOTPViewModel.getLoginInfo()
        verify(exactly = 1) { emailOTPViewModel.getLoginInfo() }
    }

}