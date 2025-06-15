package org.nunocky.myfirebasetextapp.ui.screens.edit

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
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

    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }

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

    EditorScreen(
        screenTitle = "Create New Item",
        title = title,
        content = content,
        onSaveRequested = { title, content ->
            viewModel.updateItem(
                contentId = itemId,
                title = title,
                content = content
            )
        },
        onTitleChange = { s ->
            title = s
        },
        onContentChange = { s ->
            content = s
        }
    )
}

