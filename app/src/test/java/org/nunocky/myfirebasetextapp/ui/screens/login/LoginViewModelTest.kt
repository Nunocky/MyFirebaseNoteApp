package org.nunocky.myfirebasetextapp.ui.screens.login

import app.cash.turbine.test
import com.google.firebase.auth.FirebaseUser
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.nunocky.myfirebasetextapp.data.SignInUIState
import org.nunocky.myfirebasetextapp.domain.CloudStorageUseCase
import org.nunocky.myfirebasetextapp.domain.GoogleSignInUseCase
import org.nunocky.myfirebasetextapp.domain.GoogleSignInUseCase.SignInResult

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {
    @Mock
    private lateinit var googleSignInUseCase: GoogleSignInUseCase

    @Mock
    private lateinit var cloudStorageUseCase: CloudStorageUseCase

    private lateinit var viewModel: LoginViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = LoginViewModel(googleSignInUseCase, cloudStorageUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `login initial state should be Initial`() = runTest {
        viewModel.signInUIState.test {
            assertEquals(SignInUIState.Initial, awaitItem())
        }
    }

    @Test
    fun `sign in with Google should emit Processing state before sign in`() = runTest {
        val user = mock<FirebaseUser>()

        whenever(googleSignInUseCase.signIn(any())).thenReturn(
            SignInResult.Success(user)
        )

        viewModel.signInUIState.test {
            assertEquals(SignInUIState.Initial, awaitItem())

            viewModel.signInWithGoogle("dummy_client_id")
            assertEquals(SignInUIState.Processing, awaitItem())

            assert(awaitItem() is SignInUIState.Success)
        }
    }
}