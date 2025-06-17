package org.nunocky.myfirebasetextapp.data

import junit.framework.TestCase
import org.junit.Test
import org.nunocky.myfirebasetextapp.uistate.ItemDeleteUIState

class ItemDeleteUIStateTest {
    @Test
    fun testError() {
        val exception = Exception("Test delete error")
        val state = ItemDeleteUIState.Error(exception)
        assert(state.e == exception)
        TestCase.assertEquals(state.e.message, "Test delete error")
    }
}