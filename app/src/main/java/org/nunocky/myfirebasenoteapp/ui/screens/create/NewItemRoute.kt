package org.nunocky.myfirebasenoteapp.ui.screens.create

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import org.nunocky.myfirebasenoteapp.data.EditType
import org.nunocky.myfirebasenoteapp.uistate.ItemSaveUIState
import org.nunocky.myfirebasenoteapp.ui.composables.EditorScreen

@Composable
fun NewItemRoute(
    navHostController: NavHostController,
    viewModel: NewItemViewModel,
    onSaveSuccess: () -> Unit = {},
    onEditCancelled: () -> Unit = {},
) {
    val uiState: ItemSaveUIState by viewModel.uiState.collectAsState()
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }

    BackHandler {
        onEditCancelled()
    }

    LaunchedEffect(key1 = uiState) {
        when (uiState) {
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

    EditorScreen(
        editType = EditType.CREATE,
        title = title,
        content = content,
        onSaveRequested = { title, content ->
            viewModel.createNewItem(title, content)
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
    )
}

