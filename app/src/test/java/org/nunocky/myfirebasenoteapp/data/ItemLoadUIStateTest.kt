package org.nunocky.myfirebasenoteapp.data

import junit.framework.TestCase
import org.junit.Test

class ItemLoadUIStateTest {
    @Test
    fun testSuccess() {
        val data = Pair("id1", "title1")
        val state = UIState.Success(data)
        assert(state.data == data)
    }

    @Test
    fun testError() {
        val exception = Exception("Test load error")
        val state = UIState.Error(exception)
        assert(state.e == exception)
        TestCase.assertEquals(state.e.message, "Test load error")
    }
}