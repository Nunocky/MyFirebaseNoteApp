package org.nunocky.myfirebasetextapp

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable

@Serializable
object Home

@Serializable
object Login

@Composable
fun AppRouting() {
    val navHostController = rememberNavController()

    NavHost(
        navController = navHostController,
        startDestination = Home
    ) {
        composable<Home> { _ -> HomeScreen(navHostController) }
        composable<Login> { _ -> LoginScreen(navHostController) }
    }
}

@Composable
fun HomeScreen(navHostController: NavHostController) {
    // Home screen content
    Scaffold(
        floatingActionButton = {
            // add fab
            FloatingActionButton(onClick = {}) {
                // FAB content ( + button icon)
                Icons.Default.Add
            }
        }
    ) { innerPadding ->
        Text("Home", modifier = Modifier.padding(innerPadding))
    }
}

@Composable
fun LoginScreen(navHostController: NavHostController) {
    // Login screen content
}