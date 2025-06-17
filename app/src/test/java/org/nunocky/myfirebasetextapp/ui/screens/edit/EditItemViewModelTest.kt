package org.nunocky.myfirebasetextapp.ui.screens.edit

import app.cash.turbine.test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.nunocky.myfirebasetextapp.uistate.ItemDeleteUIState
import org.nunocky.myfirebasetextapp.uistate.ItemLoadUIState
import org.nunocky.myfirebasetextapp.uistate.ItemSaveUIState
import org.nunocky.myfirebasetextapp.domain.CloudStorageUseCase

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
            assertEquals(ItemSaveUIState.Initial, awaitItem())
        }
    }

    @Test
    fun `item save should emit Success state when cloudStorageUseCase returns successfully`() =
        runTest {
            val itemId = "dummy_item_id"
            whenever(cloudStorageUseCase.updateItem(any(), any(), any())).thenAnswer { }

            viewModel.itemSaveUiState.test {
                assertEquals(ItemSaveUIState.Initial, awaitItem())

                viewModel.updateItem(itemId, "test", "test content")
                assertEquals(ItemSaveUIState.Processing, awaitItem())

                assertEquals(ItemSaveUIState.Success(itemId), awaitItem())
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
                assertEquals(ItemSaveUIState.Initial, awaitItem())

                viewModel.updateItem("dummy_item_id", "test", "test content")
                assertEquals(ItemSaveUIState.Processing, awaitItem())

                assertEquals(ItemSaveUIState.Error(exception), awaitItem())
            }
        }

    @Test
    fun `item load initial state should be Initial`() = runTest {
        viewModel.itemLoadUiState.test {
            assertEquals(ItemLoadUIState.Initial, awaitItem())
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
                assertEquals(ItemLoadUIState.Initial, awaitItem())

                viewModel.loadItem(itemId)
                assertEquals(ItemLoadUIState.Processing, awaitItem())

                assertEquals(ItemLoadUIState.Success(Pair(title, content)), awaitItem())
            }
        }

    @Test
    fun `item load should emit Error state when cloudStorageUseCase throws an exception`() =
        runTest {
            val itemId = "dummy_item_id"
            val exception = RuntimeException("Error")
            whenever(cloudStorageUseCase.getItem(itemId)).thenThrow(exception)

            viewModel.itemLoadUiState.test {
                assertEquals(ItemLoadUIState.Initial, awaitItem())

                viewModel.loadItem(itemId)
                assertEquals(ItemLoadUIState.Processing, awaitItem())

                assertEquals(ItemLoadUIState.Error(exception), awaitItem())
            }
        }

    @Test
    fun `item delete initial state should be Initial`() = runTest {
        viewModel.itemDeleteUiState.test {
            assertEquals(ItemDeleteUIState.Initial, awaitItem())
        }
    }

    @Test
    fun `item delete should emit Success state when cloudStorageUseCase returns successfully`() =
        runTest {
            val itemId = "dummy_item_id"
            whenever(cloudStorageUseCase.deleteItem(itemId)).thenAnswer { }

            viewModel.itemDeleteUiState.test {
                assertEquals(ItemDeleteUIState.Initial, awaitItem())

                viewModel.deleteItem(itemId)
                assertEquals(ItemDeleteUIState.Processing, awaitItem())

                assertEquals(ItemDeleteUIState.Success, awaitItem())
            }
        }

    @Test
    fun `item delete should emit Error state when cloudStorageUseCase throws an exception`() =
        runTest {
            val itemId = "dummy_item_id"
            val exception = RuntimeException("Error")
            whenever(cloudStorageUseCase.deleteItem(itemId)).thenThrow(exception)

            viewModel.itemDeleteUiState.test {
                assertEquals(ItemDeleteUIState.Initial, awaitItem())

                viewModel.deleteItem(itemId)
                assertEquals(ItemDeleteUIState.Processing, awaitItem())

                assertEquals(ItemDeleteUIState.Error(exception), awaitItem())
            }
        }
}