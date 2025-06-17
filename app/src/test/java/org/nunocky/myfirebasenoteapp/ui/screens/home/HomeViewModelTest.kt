package org.nunocky.myfirebasenoteapp.ui.screens.home

import app.cash.turbine.test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever
import org.nunocky.myfirebasenoteapp.domain.Authentication
import org.nunocky.myfirebasenoteapp.domain.CloudStorageUseCase
import org.nunocky.myfirebasenoteapp.uistate.GetNoteListUiState

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    @Mock
    private lateinit var cloudStorageUseCase: CloudStorageUseCase

    @Mock
    private lateinit var authentication: Authentication

    private lateinit var viewModel: HomeViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = HomeViewModel(cloudStorageUseCase, authentication)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state should be Initial`() = runTest {
        val initialState = viewModel.uiState.first()
        assertEquals(GetNoteListUiState.Initial, initialState)
    }

    @Test
    fun `getNoteList should emit Success state when cloudStorageUseCase returns successfully`() =
        runTest {
            val mockItemList = listOf("item1" to "content1", "item2" to "content2")
            whenever(cloudStorageUseCase.getItemList()).thenReturn(mockItemList)

            viewModel.uiState.test {
                assertEquals(GetNoteListUiState.Initial, awaitItem())

                viewModel.getNoteList()

                assertEquals(GetNoteListUiState.Processing, awaitItem())

                val uiState = awaitItem()
                assertTrue(uiState is GetNoteListUiState.Success)
                assertEquals(mockItemList, (uiState as GetNoteListUiState.Success).itemList)
            }
        }

    @Test
    fun `getNoteList should emit Error state when cloudStorageUseCase throws exception`() =
        runTest {
            val exception = RuntimeException("Test exception")
            whenever(cloudStorageUseCase.getItemList()).thenThrow(exception)

            viewModel.uiState.test {
                assertEquals(GetNoteListUiState.Initial, awaitItem())

                viewModel.getNoteList()
                assertEquals(GetNoteListUiState.Processing, awaitItem())

                val uiState = awaitItem()
                assertTrue(uiState is GetNoteListUiState.Error)
            }
        }

    @Test
    fun `getNoteList should transition through Processing state to Success`() = runTest {
        val mockItemList = listOf("item1" to "content1")
        whenever(cloudStorageUseCase.getItemList()).thenReturn(mockItemList)

        viewModel.uiState.test {
            assertEquals(GetNoteListUiState.Initial, awaitItem())

            viewModel.getNoteList()

            assertEquals(GetNoteListUiState.Processing, awaitItem())

            val uiState = awaitItem()
            assertTrue(uiState is GetNoteListUiState.Success)
            assertEquals(mockItemList, (uiState as GetNoteListUiState.Success).itemList)
        }
    }

    @Test
    fun `getNoteList should transition through Processing state to Error`() = runTest {
        val exception = RuntimeException("Test exception")
        whenever(cloudStorageUseCase.getItemList()).thenThrow(exception)

        viewModel.uiState.test {
            assertEquals(GetNoteListUiState.Initial, awaitItem())

            viewModel.getNoteList()

            assertEquals(GetNoteListUiState.Processing, awaitItem())

            val uiState = awaitItem()
            assertTrue(uiState is GetNoteListUiState.Error)
            assertEquals(exception, (uiState as GetNoteListUiState.Error).e)
        }
    }
}