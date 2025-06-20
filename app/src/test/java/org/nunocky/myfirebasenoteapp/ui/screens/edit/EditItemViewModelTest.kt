package org.nunocky.myfirebasenoteapp.ui.screens.edit

import app.cash.turbine.test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
import org.nunocky.myfirebasenoteapp.usecase.CloudStorageUseCase

@OptIn(ExperimentalCoroutinesApi::class)
class EditItemViewModelTest {

    @Mock
    private lateinit var cloudStorageUseCase: CloudStorageUseCase

    private lateinit var viewModel: EditItemViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = EditItemViewModel(cloudStorageUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `item save initial state should be Initial`() = runTest {
        viewModel.itemSaveUiState.test {
            var uiState = awaitItem()
            assertEquals(UIState.Initial, uiState)
        }
    }

    @Test
    fun `item save should emit Success state when cloudStorageUseCase returns successfully`() =
        runTest {
            val itemId = "dummy_item_id"
            whenever(cloudStorageUseCase.updateItem(any(), any(), any())).thenAnswer { }

            viewModel.itemSaveUiState.test {
                var uiState = awaitItem()
                assertEquals(UIState.Initial, uiState)

                viewModel.updateItem(itemId, "test", "test content")
                uiState = awaitItem()
                assertEquals(UIState.Processing, uiState)

                uiState = awaitItem()
                assertEquals(itemId, (uiState as UIState.Success<*>).data)
            }
        }

    @Test
    fun `item save should emit Error state when cloudStorageUseCase throws an exception`() =
        runTest {
            val exception = RuntimeException("Error")
            whenever(
                cloudStorageUseCase.updateItem(any(), any(), any())
            ).thenThrow(exception)

            viewModel.itemSaveUiState.test {
                var uiState = awaitItem()
                assertEquals(UIState.Initial, uiState)

                viewModel.updateItem("dummy_item_id", "test", "test content")
                uiState = awaitItem()
                assertEquals(UIState.Processing, uiState)

                uiState = awaitItem()
                assertEquals(exception, (uiState as UIState.Error).e)
            }
        }

    @Test
    fun `item load initial state should be Initial`() = runTest {
        viewModel.itemLoadUiState.test {
            var uiState = awaitItem()
            assertEquals(UIState.Initial, uiState)
        }
    }

    @Test
    fun `item load should emit Success state when cloudStorageUseCase returns successfully`() =
        runTest {
            val itemId = "dummy_item_id"
            val title = "Test Title"
            val content = "Test Content"
            whenever(cloudStorageUseCase.getItem(itemId)).thenReturn(Pair(title, content))

            viewModel.itemLoadUiState.test {
                var uiState = awaitItem()
                assertEquals(UIState.Initial, uiState)

                viewModel.loadItem(itemId)
                uiState = awaitItem()
                assertEquals(UIState.Processing, uiState)

                uiState = awaitItem()
                assertEquals(Pair(title, content), (uiState as UIState.Success<*>).data)
            }
        }

    @Test
    fun `item load should emit Error state when cloudStorageUseCase throws an exception`() =
        runTest {
            val itemId = "dummy_item_id"
            val exception = RuntimeException("Error")
            whenever(cloudStorageUseCase.getItem(itemId)).thenThrow(exception)

            viewModel.itemLoadUiState.test {
                var uiState = awaitItem()
                assertEquals(UIState.Initial, uiState)

                viewModel.loadItem(itemId)
                uiState = awaitItem()
                assertEquals(UIState.Processing, uiState)

                uiState = awaitItem()
                assertEquals(exception, (uiState as UIState.Error).e)
            }
        }

    @Test
    fun `item delete initial state should be Initial`() = runTest {
        viewModel.itemDeleteUiState.test {
            assertEquals(UIState.Initial, awaitItem())
        }
    }

    @Test
    fun `item delete should emit Success state when cloudStorageUseCase returns successfully`() =
        runTest {
            val itemId = "dummy_item_id"
            whenever(cloudStorageUseCase.deleteItem(itemId)).thenAnswer { }

            viewModel.itemDeleteUiState.test {
                var uiState = awaitItem()
                assertEquals(UIState.Initial, uiState)

                viewModel.deleteItem(itemId)
                uiState = awaitItem()
                assertEquals(UIState.Processing, uiState)

                uiState = awaitItem()
                assertTrue(uiState is UIState.Success<*>)
            }
        }

    @Test
    fun `item delete should emit Error state when cloudStorageUseCase throws an exception`() =
        runTest {
            val itemId = "dummy_item_id"
            val exception = RuntimeException("Error")
            whenever(cloudStorageUseCase.deleteItem(itemId)).thenThrow(exception)

            viewModel.itemDeleteUiState.test {
                var uiState = awaitItem()
                assertEquals(UIState.Initial, uiState)

                viewModel.deleteItem(itemId)
                uiState = awaitItem()
                assertEquals(UIState.Processing, uiState)

                uiState = awaitItem()
                assertEquals(exception, (uiState as UIState.Error).e)
            }
        }
}