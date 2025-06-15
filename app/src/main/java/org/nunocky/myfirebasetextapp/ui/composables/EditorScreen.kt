package org.nunocky.myfirebasetextapp.ui.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.nunocky.myfirebasetextapp.data.EditType
import org.nunocky.myfirebasetextapp.ui.theme.Typography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditorScreen(
    editType: EditType = EditType.CREATE,
    title: String = "",
    content: String = "",
    onSaveRequested: (
        title: String,
        content: String,
    ) -> Unit = { _, _ -> },
    onTitleChange: (String) -> Unit = { _ -> },
    onContentChange: (String) -> Unit = { _ -> },
    onBackRequested: () -> Unit = {},
    onDeleteItemRequested: () -> Unit = { }
) {
    val scrollState = rememberScrollState()
    var shouldShowDeleteConfirmationDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    val screenTitle = when (editType) {
                        EditType.CREATE -> "Create New Item"
                        EditType.EDIT -> "Edit Item"
                    }
                    Text(screenTitle, style = Typography.titleLarge)
                },
                // バックボタン
                navigationIcon = {
                    IconButton(onClick = { onBackRequested() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    if (editType == EditType.EDIT) {
                        IconButton(onClick = {
                            shouldShowDeleteConfirmationDialog = true
                        }) {
                            Icon(imageVector = Icons.Filled.Delete, contentDescription = "削除")
                        }
                    }

                    IconButton(onClick = {
                        onSaveRequested(title, content)
                    }) {
                        Icon(imageVector = Icons.Filled.Done, contentDescription = "保存")
                    }
                })
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier.Companion
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { onTitleChange(it) },
                label = { Text("Title") },
                modifier = Modifier.Companion
                    .padding(16.dp)
                    .fillMaxSize(fraction = 1f)
            )
            OutlinedTextField(
                value = content,
                onValueChange = { onContentChange(it) },
                label = { Text("Content") },
                modifier = Modifier.Companion
                    .padding(16.dp)
                    .fillMaxSize(fraction = 1f),
                singleLine = false,
                minLines = 5,
                maxLines = Int.MAX_VALUE
            )
        }

        if (shouldShowDeleteConfirmationDialog) {
            AlertDialog(
                onDismissRequest = { shouldShowDeleteConfirmationDialog = false },
                title = { Text("Confirm Deletion") },
                text = { Text("Are you sure you want to delete this item?") },
                confirmButton = {
                    TextButton(onClick = {
                        shouldShowDeleteConfirmationDialog = false
                        onDeleteItemRequested()
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
}

@Preview(showBackground = true, widthDp = 1080, heightDp = 1920)
@Composable
fun EditorScreenPreview() {
    EditorScreen()
}