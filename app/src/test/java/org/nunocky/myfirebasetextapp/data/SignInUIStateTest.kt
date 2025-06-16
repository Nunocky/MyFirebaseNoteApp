package org.nunocky.myfirebasetextapp.data

import com.google.firebase.auth.FirebaseUser
import junit.framework.TestCase
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito

@RunWith(JUnit4::class)
class SignInUIStateTest {
    @Test
    fun testSuccess() {
        val user = Mockito.mock(FirebaseUser::class.java)
        val state = SignInUIState.Success(user)
        assert(state.user == user)
    }

    @Test
    fun testFailed() {
        val exception = Exception("Sign-in failed")
        val state = SignInUIState.Failed(exception)
        assert(state.e == exception)
        TestCase.assertEquals(state.e.message, "Sign-in failed")
    }
}