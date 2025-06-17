package org.nunocky.myfirebasenoteapp.data

import junit.framework.TestCase.assertEquals
import org.junit.Test

class GetNoteListUiStateTest {
    @Test
    fun testSuccess() {
        val itemList = listOf(Pair("id1", "title1"), Pair("id2", "title2"))
        val state = UIState.Success(itemList)
        assert(state.data == itemList)
    }

    @Test
    fun testError() {
        val exception = Exception("Test error")
        val state = UIState.Error(exception)
        assert(state.e == exception)
        assertEquals(state.e.message, "Test error")
    }
}


