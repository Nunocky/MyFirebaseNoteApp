package org.nunocky.myfirebasetextapp.ui.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.nunocky.myfirebasetextapp.ui.theme.Typography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditorScreen(
    initialTitle: String = "",
    initialContent: String = "",
    onSaveRequested: (
        title: String,
        content: String,
    ) -> Unit = { _, _ -> }
) {
    var title by remember { mutableStateOf(initialTitle) }
    var content by remember { mutableStateOf(initialContent) }
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text("Create New Item", style = Typography.titleLarge)
            }, actions = {
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
                onValueChange = { title = it },
                label = { Text("Title") },
                modifier = Modifier.Companion
                    .padding(16.dp)
                    .fillMaxSize(fraction = 1f)
            )
            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                label = { Text("Content") },
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

@Preview(showBackground = true, widthDp = 1080, heightDp = 1920)
@Composable
fun EditorScreenPreview() {
    EditorScreen()
}