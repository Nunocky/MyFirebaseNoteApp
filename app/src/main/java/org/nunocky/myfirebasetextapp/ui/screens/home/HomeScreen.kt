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
import androidx.navigation.NavHostController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.nunocky.myfirebasetextapp.ui.theme.Typography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navHostController: NavHostController,
    onLoginNeeded: () -> Unit = {}
) {
    val auth = Firebase.auth
    LaunchedEffect(key1 = Unit) {
        if (auth.currentUser == null) {
            onLoginNeeded()
            return@LaunchedEffect
        }
    }

    if (auth.currentUser != null) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text("Notes", style = Typography.titleLarge)
                    },
                )
            },
            floatingActionButton = {
                FloatingActionButton(onClick = {}) {
                    Icons.Default.Add
                }
            }
        ) { innerPadding ->
            val user = Firebase.auth.currentUser!!
            Column(modifier = Modifier.padding(innerPadding)) {
                Text(user.displayName.toString())
            }
        }
    }
}

