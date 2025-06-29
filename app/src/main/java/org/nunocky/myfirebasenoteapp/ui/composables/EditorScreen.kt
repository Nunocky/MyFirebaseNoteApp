package org.nunocky.myfirebasenoteapp.ui.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.nunocky.myfirebasenoteapp.R
import org.nunocky.myfirebasenoteapp.data.EditType
import org.nunocky.myfirebasenoteapp.ui.theme.Typography
import org.nunocky.myfirebasenoteapp.ui.theme.myfirebasenoteappTheme

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
    onDeleteItemRequested: () -> Unit = { },
) {
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    val screenTitle = when (editType) {
                        EditType.CREATE -> stringResource(R.string.create_new_item)
                        EditType.EDIT -> stringResource(R.string.edit_item)
                    }
                    Text(screenTitle, style = Typography.titleLarge)
                },
                // バックボタン
                navigationIcon = {
                    IconButton(onClick = { onBackRequested() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                },
                actions = {
                    if (editType == EditType.EDIT) {
                        IconButton(onClick = {
                            onDeleteItemRequested()
                        }) {
                            Icon(
                                imageVector = Icons.Filled.Delete,
                                contentDescription = stringResource(R.string.delete)
                            )
                        }
                    }

                    IconButton(onClick = {
                        onSaveRequested(title, content)
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Done,
                            contentDescription = stringResource(R.string.save)
                        )
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
                label = { Text(stringResource(R.string.title)) },
                modifier = Modifier.Companion
                    .padding(16.dp)
                    .fillMaxSize(fraction = 1f)
            )
            OutlinedTextField(
                value = content,
                onValueChange = { onContentChange(it) },
                label = { Text(stringResource(R.string.content)) },
                modifier = Modifier.Companion
                    .padding(16.dp)
                    .fillMaxSize(fraction = 1f),
                singleLine = false,
                minLines = 5,
                maxLines = Int.MAX_VALUE
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 412, heightDp = 915)
@Composable
fun EditorScreenPreview() {
    myfirebasenoteappTheme {
        EditorScreen()
    }
}