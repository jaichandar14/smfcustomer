package com.smf.customer.view.emailotp

import androidx.lifecycle.MutableLiveData
import com.smf.customer.InstantExecutorExtension
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantExecutorExtension::class)
internal class EmailOTPActivityTest {

    private lateinit var emailOTPActivity: EmailOTPActivity

    @BeforeEach
    fun setUp() {
        // Setting MutableLiveData values
        val mutableLiveData = MutableLiveData<Boolean>()
        mutableLiveData.postValue(false)
        emailOTPActivity = EmailOTPActivity()
    }

    @Test
    @DisplayName("OTP validation without parameter")
    fun otpValidationWithoutValue() {
        Assertions.assertEquals(
            emailOTPActivity.otpValidation("", "", "", ""),
            false
        )
    }

    @Test
    @DisplayName("OTP validation with first box parameter")
    fun otpValidationWithValue() {
        Assertions.assertEquals(
            emailOTPActivity.otpValidation("1", "", "", ""),
            true
        )
    }

}