package org.nunocky.myfirebasenoteapp.data

import junit.framework.TestCase
import org.junit.Test

class ItemDeleteUIStateTest {
    @Test
    fun testError() {
        val exception = Exception("Test delete error")
        val state = UIState.Error(exception)
        assert(state.e == exception)
        TestCase.assertEquals(state.e.message, "Test delete error")
    }
}