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
import org.nunocky.myfirebasenoteapp.uistate.ItemSaveUIState
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
        assertEquals(ItemSaveUIState.Initial, initialState)
    }

    @Test
    fun `createNewItem should emit Success state when cloudStorageUseCase returns successfully`() =
        runTest {
            whenever(cloudStorageUseCase.createNewItem(any(), any())).thenReturn("dummy_item_id")

            viewModel.uiState.test {
                assertEquals(ItemSaveUIState.Initial, awaitItem())

                viewModel.createNewItem("test", "test content")
                assertEquals(ItemSaveUIState.Processing, awaitItem())

                assertEquals(ItemSaveUIState.Success("dummy_item_id"), awaitItem())
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
                assertEquals(ItemSaveUIState.Initial, awaitItem())

                viewModel.createNewItem("test", "test content")
                assertEquals(ItemSaveUIState.Processing, awaitItem())

                val uiState = awaitItem()
                assertTrue(uiState is ItemSaveUIState.Error)
            }
        }
}