package org.nunocky.myfirebasenoteapp.data

import junit.framework.TestCase
import org.junit.Test

class ItemSaveUIStateTest {
    @Test
    fun testSuccess() {
        val itemId = "item123"
        val state = UIState.Success(itemId)
        assert(state.data == itemId)
    }

    @Test
    fun testError() {
        val exception = Exception("Test save error")
        val state = UIState.Error(exception)
        assert(state.e == exception)
        TestCase.assertEquals(state.e.message, "Test save error")
    }
}