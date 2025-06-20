package org.nunocky.myfirebasenoteapp.ui.screens.signin

import app.cash.turbine.test
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.nunocky.myfirebasenoteapp.data.SignInResult
import org.nunocky.myfirebasenoteapp.data.UIState
import org.nunocky.myfirebasenoteapp.data.User
import org.nunocky.myfirebasenoteapp.domain.Authentication
import org.nunocky.myfirebasenoteapp.domain.CloudStorageUseCase

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {
    @Mock
    private lateinit var authentication: Authentication

    @Mock
    private lateinit var cloudStorageUseCase: CloudStorageUseCase

    private lateinit var viewModel: SignInViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = SignInViewModel(authentication, cloudStorageUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `login initial state should be Initial`() = runTest {
        viewModel.signInUIState.test {
            assertEquals(UIState.Initial, awaitItem())
        }
    }

    @Test
    fun `sign in with Google should emit Processing state before sign in`() = runTest {
        val user = mock<User>()

        whenever(authentication.googleSignIn(any())).thenReturn(
            SignInResult.Success(user)
        )

        viewModel.signInUIState.test {
            var uiState = awaitItem()
            assertEquals(UIState.Initial, uiState)

            viewModel.signInWithGoogle("dummy_client_id")

            uiState = awaitItem()
            assertEquals(UIState.Processing, uiState)

            uiState = awaitItem()
            assertTrue(uiState is UIState.Success<*>)
            assertTrue((uiState as UIState.Success<*>).data is User)
        }
    }
}