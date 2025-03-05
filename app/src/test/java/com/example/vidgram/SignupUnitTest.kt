import com.example.vidgram.repository.SignupRepoImp
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

class SignupUnitTest {

    @Mock
    private lateinit var mockAuth: FirebaseAuth

    @Mock
    private lateinit var mockTask: Task<AuthResult>

    @Mock
    private lateinit var mockFirebaseUser: FirebaseUser

    private lateinit var signupRepo: SignupRepoImp

    @Captor
    private lateinit var captor: ArgumentCaptor<OnCompleteListener<AuthResult>>

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        signupRepo = SignupRepoImp(mockAuth)
    }

    @Test
    fun testSignup_Successful() {
        val email = "test@example.com"
        val password = "password123"
        var expectedResult = "Initial Value" // Define the initial value
        val expectedUid = "mockedUserId" // The mocked UID returned after signup

        // Mocking task to simulate successful signup
        `when`(mockTask.isSuccessful).thenReturn(true)
        `when`(mockTask.result).thenReturn(mock())
        `when`(mockAuth.createUserWithEmailAndPassword(email, password)).thenReturn(mockTask)

        // Mocking getCurrentUser to return the mocked FirebaseUser
        `when`(mockAuth.currentUser).thenReturn(mockFirebaseUser)
        `when`(mockFirebaseUser.uid).thenReturn(expectedUid) // Simulating the UID for the created user

        // Define a callback that updates the expectedResult
        val callback = { success: Boolean, message: String?, uid: String ->
            expectedResult = message ?: "Callback message is null"
            assertEquals(expectedUid, uid) // Verify the UID passed to callback
        }

        // Call the function under test
        signupRepo.signup(email, password, callback)

        // Capture the OnCompleteListener to verify its behavior
        verify(mockTask).addOnCompleteListener(captor.capture())

        // Simulate the callback invocation to verify the result
        captor.value.onComplete(mockTask)

        // Assert the result
        assertEquals("Sign Up Successful.", expectedResult)
    }

    @Test
    fun testSignup_Failure() {
        val email = "test@example.com"
        val password = "password123"
        var expectedResult = "Initial Value" // Define the initial value

        // Mocking task to simulate signup failure
        val mockException = Exception("Signup Failed")
        `when`(mockTask.isSuccessful).thenReturn(false)
        `when`(mockTask.exception).thenReturn(mockException)
        `when`(mockAuth.createUserWithEmailAndPassword(email, password)).thenReturn(mockTask)

        // Define a callback that updates the expectedResult
        val callback = { success: Boolean, message: String?, uid: String ->
            expectedResult = message ?: "Callback message is null"
            assertEquals("", uid) // Ensure that the UID is empty on failure
        }

        // Call the function under test
        signupRepo.signup(email, password, callback)

        // Capture the OnCompleteListener to verify its behavior
        verify(mockTask).addOnCompleteListener(captor.capture())

        // Simulate the callback invocation to verify the result
        captor.value.onComplete(mockTask)

        // Assert the result
        assertEquals("Signup Failed", expectedResult)
    }
}
