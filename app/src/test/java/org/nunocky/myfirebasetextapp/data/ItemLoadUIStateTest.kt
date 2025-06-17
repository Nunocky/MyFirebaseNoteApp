package org.nunocky.myfirebasetextapp.data

import junit.framework.TestCase
import org.junit.Test
import org.nunocky.myfirebasetextapp.uistate.ItemLoadUIState

class ItemLoadUIStateTest {
    @Test
    fun testSuccess() {
        val data = Pair("id1", "title1")
        val state = ItemLoadUIState.Success(data)
        assert(state.data == data)
    }

    @Test
    fun testError() {
        val exception = Exception("Test load error")
        val state = ItemLoadUIState.Error(exception)
        assert(state.e == exception)
        TestCase.assertEquals(state.e.message, "Test load error")
    }
}