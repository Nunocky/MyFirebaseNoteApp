package org.nunocky.myfirebasetextapp.ui.screens.login

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navHostController: NavHostController,
    onLoginSuccess: () -> Unit,
    onLoginCancelled: () -> Unit,
    googleSignInViewModel: GoogleSignInViewModel
) {

    val loginUIState: GoogleSignInViewModel.SignInUIState by googleSignInViewModel.signInUIState.collectAsState()

    val scope = rememberCoroutineScope()

    // スワイプバックや戻るボタンで onLoginCancelled を実行
    BackHandler {
        onLoginCancelled()
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Login") }) }
    ) { innerPadding ->

        val buttonEnabled = loginUIState !is GoogleSignInViewModel.SignInUIState.Processing

        Button(
            modifier = Modifier.padding(innerPadding),
            onClick = {
                // TODO IMPLEMENT THIS
                scope.launch(Dispatchers.IO) {
                    googleSignInViewModel.signIn()
                }
            },
            enabled = buttonEnabled
        ) {

            Text("Sign in with Google")

            when (loginUIState) {
                is GoogleSignInViewModel.SignInUIState.Initial -> {
                    // Initial state, do nothing
                }

                is GoogleSignInViewModel.SignInUIState.Processing -> {
                    Text("Processing...")
                }

                is GoogleSignInViewModel.SignInUIState.Success -> {
                    onLoginSuccess()
                }

                is GoogleSignInViewModel.SignInUIState.Failed -> {
                    Text("Login failed: ${(loginUIState as GoogleSignInViewModel.SignInUIState.Failed)}")
                    // TODO show toast
                }
            }

        }
    }
}