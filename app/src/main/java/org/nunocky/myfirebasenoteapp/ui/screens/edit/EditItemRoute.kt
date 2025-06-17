package org.nunocky.myfirebasenoteapp.ui.screens.edit

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
import org.nunocky.myfirebasenoteapp.data.EditType
import org.nunocky.myfirebasenoteapp.data.UIState
import org.nunocky.myfirebasenoteapp.ui.composables.EditorScreen

@Composable
fun EditItemRoute(
    navHostController: NavHostController,
    viewModel: EditItemViewModel,
    itemId: String,
    onSaveSuccess: () -> Unit = {},
    onEditCancelled: () -> Unit = {},
) {
    val itemLoadUiState: UIState by viewModel.itemLoadUiState.collectAsState()
    val itemSaveUiState: UIState by viewModel.itemSaveUiState.collectAsState()
    val itemDeleteUiState: UIState by viewModel.itemDeleteUiState.collectAsState()

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
            is UIState.Initial -> {}

            UIState.Processing -> {}

            is UIState.Success<*> -> {
                onSaveSuccess()
            }

            is UIState.Error -> {
                // Handle error state if needed
            }

            UIState.Cancelled -> {}
        }
    }

    LaunchedEffect(key1 = itemLoadUiState) {
        when (itemLoadUiState) {
            UIState.Initial -> {}

            UIState.Processing -> {}

            is UIState.Success<*> -> {
                val data = (itemLoadUiState as UIState.Success<*>).data as Pair<String, String>?
                    ?: throw RuntimeException("Unexpected data type")
                title = data.first
                content = data.second
            }

            is UIState.Error -> {
                // エラー処理が必要ならここで行う
            }

            UIState.Cancelled -> {}
        }
    }

    LaunchedEffect(key1 = itemDeleteUiState) {
        when (itemDeleteUiState) {
            UIState.Initial -> {}

            UIState.Processing -> {}

            is UIState.Success<*> -> {
                onEditCancelled()
            }

            is UIState.Error -> {
                // Handle error state if needed
            }

            UIState.Cancelled -> {}
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

