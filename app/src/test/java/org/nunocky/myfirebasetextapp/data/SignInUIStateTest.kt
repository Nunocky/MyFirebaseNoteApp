package org.nunocky.myfirebasetextapp.data

import junit.framework.TestCase
import org.junit.Test
import org.mockito.Mockito

class SignInUIStateTest {
    @Test
    fun testSuccess() {
        val user = Mockito.mock(User::class.java)
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