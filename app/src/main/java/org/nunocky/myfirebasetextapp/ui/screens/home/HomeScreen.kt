package org.nunocky.myfirebasetextapp.ui.screens.home

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

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
            floatingActionButton = {
                FloatingActionButton(onClick = {}) {
                    Icons.Default.Add
                }
            }
        ) { innerPadding ->
            Text("Home", modifier = Modifier.padding(innerPadding))
        }
    }
}

