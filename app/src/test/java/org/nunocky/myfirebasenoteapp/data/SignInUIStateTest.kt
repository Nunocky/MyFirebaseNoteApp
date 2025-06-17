package org.nunocky.myfirebasenoteapp.data

import junit.framework.TestCase
import org.junit.Test
import org.mockito.Mockito

class SignInUIStateTest {
    @Test
    fun testSuccess() {
        val user = Mockito.mock(User::class.java)
        val state = UIState.Success(user)
        assert(state.data == user)
    }

    @Test
    fun testFailed() {
        val exception = Exception("Sign-in failed")
        val state = UIState.Error(exception)
        assert(state.e == exception)
        TestCase.assertEquals(state.e.message, "Sign-in failed")
    }
}