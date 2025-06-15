package org.nunocky.myfirebasetextapp.ui.screens.create

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import org.nunocky.myfirebasetextapp.data.ItemSaveUIState
import org.nunocky.myfirebasetextapp.ui.composables.EditorScreen

@Composable
fun NewItemRoute(
    navHostController: NavHostController,
    viewModel: NewItemViewModel,
    onSaveSuccess: () -> Unit = {},
    onEditCancelled: () -> Unit = {},
) {
    val uiState: ItemSaveUIState by viewModel.uiState.collectAsState()

    BackHandler {
        onEditCancelled()
    }

    LaunchedEffect(key1 = uiState) {
        when (uiState) {
            is ItemSaveUIState.Initial -> {}

            ItemSaveUIState.Processing -> {
                // TODO
            }

            is ItemSaveUIState.Success -> {
                onSaveSuccess()
            }

            is ItemSaveUIState.Error -> {
                // Handle error state if needed
            }

        }
    }

    EditorScreen(
        initialTitle = "",
        initialContent = "",
        onSaveRequested = { title, content ->
            viewModel.createNewItem(title, content)
        },
    )
}

