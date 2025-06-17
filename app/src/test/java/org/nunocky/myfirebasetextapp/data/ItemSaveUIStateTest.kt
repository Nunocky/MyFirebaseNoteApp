package org.nunocky.myfirebasetextapp.data

import junit.framework.TestCase
import org.junit.Test
import org.nunocky.myfirebasetextapp.uistate.ItemSaveUIState

class ItemSaveUIStateTest {
    @Test
    fun testSuccess() {
        val itemId = "item123"
        val state = ItemSaveUIState.Success(itemId)
        assert(state.itemId == itemId)
    }

    @Test
    fun testError() {
        val exception = Exception("Test save error")
        val state = ItemSaveUIState.Error(exception)
        assert(state.e == exception)
        TestCase.assertEquals(state.e.message, "Test save error")
    }
}