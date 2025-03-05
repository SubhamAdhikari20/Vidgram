package com.example.vidgram

import com.example.vidgram.repository.AuthRepoImp
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class AuthUnitTest {


        @Mock
        private lateinit var mockAuth: FirebaseAuth

        @Mock
        private lateinit var mockTask: Task<AuthResult>

        @Captor
        private lateinit var captor: ArgumentCaptor<OnCompleteListener<AuthResult>>

        private lateinit var authRepo: AuthRepoImp

        @Before
        fun setUp() {
            MockitoAnnotations.openMocks(this)

            // Initialize your repository
            authRepo = AuthRepoImp(mockAuth)
        }

        @Test
        fun testLoginSuccess() {
            var expectedResult = "" // Initially empty string

            // Arrange: Mock FirebaseAuth sign-in behavior
            val mockAuthResult: AuthResult = mock()
            `when`(mockAuth.signInWithEmailAndPassword(anyString(), anyString())).thenReturn(mockTask)
            `when`(mockTask.isSuccessful).thenReturn(true)
            `when`(mockTask.result).thenReturn(mockAuthResult)

            // Act: Call the login method
            authRepo.login("test@example.com", "password") { success, message ->
                // Update expectedResult with the message from the callback
                expectedResult = message

                // Verify callback was invoked with the expected success result and message
                assert(success)
                assert(message == "Login Successful")
            }

            // Capture the OnCompleteListener to verify its behavior
            verify(mockTask).addOnCompleteListener(captor.capture())

            // Invoke the OnCompleteListener to simulate the callback
            captor.value.onComplete(mockTask)

            // Assert that the message from the callback is as expected
            assertEquals("Login Successful", expectedResult)
        }


    @Test
    fun testLoginFailure() {
        // Arrange: Mock FirebaseAuth sign-in behavior with failure
        val mockException = Exception("Login Failed") // Simulate an exception for failure case

        `when`(mockAuth.signInWithEmailAndPassword(anyString(), anyString())).thenReturn(mockTask)
        `when`(mockTask.isSuccessful).thenReturn(false)
        `when`(mockTask.exception).thenReturn(mockException)  // Return the mocked exception

        // Act: Call the login method
        authRepo.login("test@example.com", "password") { success, message ->
            // Verify callback was invoked with the expected failure result and message
            assert(!success)
            assert(message == "Login Failed")
        }

        // Capture the OnCompleteListener to verify its behavior
        verify(mockTask).addOnCompleteListener(captor.capture())

        // Invoke the OnCompleteListener to simulate the callback
        captor.value.onComplete(mockTask)

        // Verify that the failure callback was called
        verify(mockTask).exception
        assert(mockTask.exception?.message == "Login Failed")
    }


}