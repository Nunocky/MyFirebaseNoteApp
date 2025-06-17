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
import org.nunocky.myfirebasenoteapp.data.UIState
import org.nunocky.myfirebasenoteapp.domain.Authentication
import org.nunocky.myfirebasenoteapp.domain.CloudStorageUseCase

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
        assertEquals(UIState.Initial, initialState)
    }

    @Test
    fun `getNoteList should emit Success state when cloudStorageUseCase returns successfully`() =
        runTest {
            val mockItemList = listOf("item1" to "content1", "item2" to "content2")
            whenever(cloudStorageUseCase.getItemList()).thenReturn(mockItemList)

            viewModel.uiState.test {
                var uiState = awaitItem()
                assertEquals(UIState.Initial, uiState)

                viewModel.getNoteList()
                uiState = awaitItem()
                assertEquals(UIState.Processing, uiState)

                uiState = awaitItem()
                assertTrue(uiState is UIState.Success<*>)
                assertEquals(mockItemList, (uiState as UIState.Success<*>).data)
            }
        }

    @Test
    fun `getNoteList should emit Error state when cloudStorageUseCase throws exception`() =
        runTest {
            val exception = RuntimeException("Test exception")
            whenever(cloudStorageUseCase.getItemList()).thenThrow(exception)

            viewModel.uiState.test {
                assertEquals(UIState.Initial, awaitItem())

                viewModel.getNoteList()
                assertEquals(UIState.Processing, awaitItem())

                val uiState = awaitItem()
                assertTrue(uiState is UIState.Error)
            }
        }

    @Test
    fun `getNoteList should transition through Processing state to Success`() = runTest {
        val mockItemList = listOf("item1" to "content1")
        whenever(cloudStorageUseCase.getItemList()).thenReturn(mockItemList)

        viewModel.uiState.test {
            var uiState = awaitItem()
            assertEquals(UIState.Initial, uiState)

            viewModel.getNoteList()

            uiState = awaitItem()
            assertEquals(UIState.Processing, uiState)

            uiState = awaitItem()
            assertTrue(uiState is UIState.Success<*>)
            assertEquals(mockItemList, (uiState as UIState.Success<*>).data)
        }
    }

    @Test
    fun `getNoteList should transition through Processing state to Error`() = runTest {
        val exception = RuntimeException("Test exception")
        whenever(cloudStorageUseCase.getItemList()).thenThrow(exception)

        viewModel.uiState.test {
            var uiState = awaitItem()
            assertEquals(UIState.Initial, uiState)

            viewModel.getNoteList()

            uiState = awaitItem()
            assertEquals(UIState.Processing, uiState)

            uiState = awaitItem()
            assertTrue(uiState is UIState.Error)
            assertEquals(exception, (uiState as UIState.Error).e)
        }
    }
}