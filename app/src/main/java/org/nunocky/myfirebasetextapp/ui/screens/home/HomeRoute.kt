package org.nunocky.myfirebasetextapp.ui.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.nunocky.myfirebasetextapp.uistate.GetNoteListUiState
import org.nunocky.myfirebasetextapp.data.User
import org.nunocky.myfirebasetextapp.ui.theme.MyFirebaseTextAppTheme
import org.nunocky.myfirebasetextapp.ui.theme.Typography

@Composable
fun HomeRoute(
    navHostController: NavHostController,
    viewModel: HomeViewModel,
    onLoginNeeded: () -> Unit = {},
    onCreateNewItem: () -> Unit = {},
    onRequestEditItem: (itemId: String) -> Unit = { _ -> } // 編集画面に遷移するためのコールバック
) {
    val uiState: GetNoteListUiState by viewModel.uiState.collectAsState()

    val itemList = if (uiState is GetNoteListUiState.Success) {
        (uiState as GetNoteListUiState.Success).itemList
    } else {
        emptyList()
    }

    LaunchedEffect(key1 = Unit) {
        // 未ログインならログイン画面に遷移する
        if (viewModel.authentication.currentUser() == null) {
            onLoginNeeded()
            return@LaunchedEffect
        }

        // 画面遷移の際に必ず実行 (forward, backを問わず)
        // アイテムのリストをロードする
        viewModel.getNoteList()
    }

    HomeScreen(
        user = viewModel.authentication.currentUser(),
        itemList = itemList,
        onNewItemButtonClicked = {
            onCreateNewItem()
        },
        onItemClicked = { itemId ->
            onRequestEditItem(itemId)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    user: User? = null,
    itemList: List<Pair<String, String>> = emptyList(),
    onNewItemButtonClicked: () -> Unit = {},
    onItemClicked: (itemId: String) -> Unit = { _ -> },
) {
    if (user != null) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text("Notes", style = Typography.titleLarge)
                    },
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    modifier = Modifier.testTag("FAB"),
                    onClick = {
                        onNewItemButtonClicked()
                    }) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add new item"
                    )
                }
            }
        ) { innerPadding ->
            LazyColumn(modifier = Modifier.padding(innerPadding)) {
                if (itemList.isEmpty()) {
                    item {
                        Text(
                            text = "No Items",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        )
                    }
                } else {
                    items(
                        items = itemList,
                        key = { item -> item.first } // itemIdをkeyにする
                    ) { item ->
                        val (itemId, itemText) = item
                        Text(
                            text = itemText,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onItemClicked(itemId) }
                                .padding(16.dp)
                        )
                        HorizontalDivider()
                    }
                }
            }

        }
    }
}

@Preview(showBackground = true, widthDp = 1080, heightDp = 1920)
@Composable
fun HomeScreenPreview() {
    MyFirebaseTextAppTheme {
        HomeScreen()
    }
}