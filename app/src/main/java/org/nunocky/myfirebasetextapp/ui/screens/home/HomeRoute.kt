package org.nunocky.myfirebasetextapp.ui.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.nunocky.myfirebasetextapp.ui.theme.MyFirebaseTextAppTheme
import org.nunocky.myfirebasetextapp.ui.theme.Typography

@Composable
fun HomeRoute(
    navHostController: NavHostController,
    onLoginNeeded: () -> Unit = {}
) {
    LaunchedEffect(key1 = Unit) {
        val auth = Firebase.auth

        if (auth.currentUser == null) {
            onLoginNeeded()
            return@LaunchedEffect
        }
    }

    HomeScreen(
        user = Firebase.auth.currentUser,
        onNewItemButtonClicked = {
            // Handle new item button click
            // For example, navigate to a new screen or show a dialog
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    user: FirebaseUser? = null,
    onNewItemButtonClicked: () -> Unit = {}
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
                FloatingActionButton(onClick = {
                    onNewItemButtonClicked()
                }) {
                    Icons.Default.Add
                }
            }
        ) { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding)) {
                Text(user.displayName.toString())
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