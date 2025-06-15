package org.nunocky.myfirebasetextapp.ui.screens.edit

import androidx.activity.compose.BackHandler
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import org.nunocky.myfirebasetextapp.data.EditType
import org.nunocky.myfirebasetextapp.data.ItemDeleteUIState
import org.nunocky.myfirebasetextapp.data.ItemLoadUIState
import org.nunocky.myfirebasetextapp.data.ItemSaveUIState
import org.nunocky.myfirebasetextapp.ui.composables.EditorScreen

@Composable
fun EditItemRoute(
    navHostController: NavHostController,
    viewModel: EditItemViewModel,
    itemId: String,
    onSaveSuccess: () -> Unit = {},
    onEditCancelled: () -> Unit = {},
) {
    val itemLoadUiState: ItemLoadUIState by viewModel.itemLoadUiState.collectAsState()
    val itemSaveUiState: ItemSaveUIState by viewModel.itemSaveUiState.collectAsState()
    val itemDeleteUiState: ItemDeleteUIState by viewModel.itemDeleteUiState.collectAsState()

    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var shouldShowDeleteConfirmationDialog by remember { mutableStateOf(false) }

    BackHandler {
        onEditCancelled()
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.loadItem(itemId = itemId)
    }

    LaunchedEffect(key1 = itemSaveUiState) {
        when (itemSaveUiState) {
            is ItemSaveUIState.Initial -> {}

            ItemSaveUIState.Processing -> {}

            is ItemSaveUIState.Success -> {
                onSaveSuccess()
            }

            is ItemSaveUIState.Error -> {
                // Handle error state if needed
            }
        }
    }

    LaunchedEffect(key1 = itemLoadUiState) {
        when (itemLoadUiState) {
            ItemLoadUIState.Initial -> {}

            ItemLoadUIState.Processing -> {}

            is ItemLoadUIState.Success -> {
                val data = (itemLoadUiState as ItemLoadUIState.Success).data
                title = data.first
                content = data.second
            }

            is ItemLoadUIState.Error -> {
                // エラー処理が必要ならここで行う
            }
        }
    }

    LaunchedEffect(key1 = itemDeleteUiState) {
        when (itemDeleteUiState) {
            ItemDeleteUIState.Initial -> {}

            ItemDeleteUIState.Processing -> {}

            is ItemDeleteUIState.Success -> {
                onEditCancelled()
            }

            is ItemDeleteUIState.Error -> {
                // Handle error state if needed
            }
        }
    }

    EditorScreen(
        editType = EditType.EDIT,
        title = title,
        content = content,
        onSaveRequested = { title, content ->
            viewModel.updateItem(
                itemId = itemId,
                title = title,
                content = content
            )
        },
        onTitleChange = { s ->
            title = s
        },
        onContentChange = { s ->
            content = s
        },
        onBackRequested = {
            onEditCancelled()
        },
        onDeleteItemRequested = {
            shouldShowDeleteConfirmationDialog = true
        },
    )

    if (shouldShowDeleteConfirmationDialog) {
        AlertDialog(
            onDismissRequest = { shouldShowDeleteConfirmationDialog = false },
            title = { Text("Confirm Deletion") },
            text = { Text("Are you sure you want to delete this item?") },
            confirmButton = {
                TextButton(onClick = {
                    shouldShowDeleteConfirmationDialog = false
                    viewModel.deleteItem(itemId)
                }) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    shouldShowDeleteConfirmationDialog = false
                }) {
                    Text("Cancel")
                }
            },
        )
    }
}

