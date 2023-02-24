package com.smf.customer.view.login

import androidx.lifecycle.MutableLiveData
import com.smf.customer.InstantExecutorExtension
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantExecutorExtension::class)
internal class LoginViewModelTest {

    private lateinit var loginViewModel: LoginViewModel

    @MockK
    private lateinit var loginViewModelMock: LoginViewModel

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        // Setting MutableLiveData values
        val mutableLiveData = MutableLiveData<Boolean>()
        mutableLiveData.postValue(false)
        loginViewModel = LoginViewModel()
    }

    @Test
    @DisplayName("EmailAndNumberValidation without user input")
    fun emailAndNumberValidation() {
        Assertions.assertEquals(
            loginViewModel.emailAndNumberValidation("", "", ""),
            false
        )
    }

    @Test
    @DisplayName("EmailAndNumberValidation with user Email")
    fun emailAndNumberValidationWithEmail() {
        Assertions.assertEquals(
            loginViewModel.emailAndNumberValidation("", "V@gmail.com", ""),
            true
        )
    }

    @Test
    @DisplayName("EmailAndNumberValidation with user Mobile number")
    fun emailAndNumberValidationWithNumber() {
        Assertions.assertEquals(
            loginViewModel.emailAndNumberValidation("8870203141", "", ""),
            true
        )
    }

    @Test
    @DisplayName("Verify getUserDetails method callable")
    fun getUserDetails() {
        every { loginViewModelMock.getUserDetails("loginName") } returns Unit
        loginViewModelMock.getUserDetails("loginName")
        verify(exactly = 1) { loginViewModelMock.getUserDetails("loginName") }
    }

}