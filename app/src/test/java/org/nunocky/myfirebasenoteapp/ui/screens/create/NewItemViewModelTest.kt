package org.nunocky.myfirebasenoteapp.ui.screens.create

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
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.nunocky.myfirebasenoteapp.data.UIState
import org.nunocky.myfirebasenoteapp.domain.CloudStorageUseCase

@OptIn(ExperimentalCoroutinesApi::class)
class NewItemViewModelTest {
    @Mock
    private lateinit var cloudStorageUseCase: CloudStorageUseCase

    private lateinit var viewModel: NewItemViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = NewItemViewModel(cloudStorageUseCase)
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
    fun `createNewItem should emit Success state when cloudStorageUseCase returns successfully`() =
        runTest {
            whenever(cloudStorageUseCase.createNewItem(any(), any())).thenReturn("dummy_item_id")

            viewModel.uiState.test {
                var uiState = awaitItem()
                assertEquals(UIState.Initial, uiState)

                viewModel.createNewItem("test", "test content")
                uiState = awaitItem()
                assertEquals(UIState.Processing, uiState)

                uiState = awaitItem()
                assertEquals((uiState as UIState.Success<*>).data, "dummy_item_id")
            }
        }

    @Test

    fun `createNewItem should emit Error state when cloudStorageUseCase fails`() =
        runTest {
            val exception = RuntimeException("Test exception")
            whenever(
                cloudStorageUseCase.createNewItem(any(), any())
            ).thenThrow(exception)

            viewModel.uiState.test {
                var uiState = awaitItem()
                assertEquals(UIState.Initial, uiState)

                viewModel.createNewItem("test", "test content")
                uiState = awaitItem()
                assertEquals(UIState.Processing, uiState)

                uiState = awaitItem()
                assertTrue(uiState is UIState.Error)
            }
        }
}