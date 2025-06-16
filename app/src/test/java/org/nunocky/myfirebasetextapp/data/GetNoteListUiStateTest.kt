package org.nunocky.myfirebasetextapp.data

import junit.framework.TestCase.assertEquals
import org.junit.Test

import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class GetNoteListUiStateTest {
    @Test
    fun testSuccess() {
        val itemList = listOf(Pair("id1", "title1"), Pair("id2", "title2"))
        val state = GetNoteListUiState.Success(itemList)
        assert(state.itemList == itemList)
    }

    @Test
    fun testError() {
        val exception = Exception("Test error")
        val state = GetNoteListUiState.Error(exception)
        assert(state.e == exception)
        assertEquals(state.e.message, "Test error")
    }
}


