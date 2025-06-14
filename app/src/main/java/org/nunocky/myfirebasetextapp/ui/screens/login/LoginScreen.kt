package org.nunocky.myfirebasetextapp.ui.screens.login

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.nunocky.myfirebasetextapp.ui.theme.Typography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navHostController: NavHostController,
    onLoginSuccess: (user: FirebaseUser) -> Unit,
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
        topBar = {
            TopAppBar(
                title = { Text("Login", style = Typography.titleLarge) },
                navigationIcon = {
                    // Add a back button to the top app bar
                    IconButton(onClick = onLoginCancelled) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
    ) { innerPadding ->
        val buttonEnabled = loginUIState !is GoogleSignInViewModel.SignInUIState.Processing

        Column(modifier = Modifier.padding(innerPadding)) {
            Button(
                onClick = {
                    scope.launch(Dispatchers.IO) {
                        googleSignInViewModel.signIn()
                    }
                },
                enabled = buttonEnabled
            ) {
                Text("Sign in with Google")
            }

            when (loginUIState) {
                is GoogleSignInViewModel.SignInUIState.Initial -> {
                }

                is GoogleSignInViewModel.SignInUIState.Processing -> {
                    Text("Processing...")
                }

                is GoogleSignInViewModel.SignInUIState.Success -> {
                    Text("Login successful: ${(loginUIState as GoogleSignInViewModel.SignInUIState.Success).user.displayName}")
                    onLoginSuccess((loginUIState as GoogleSignInViewModel.SignInUIState.Success).user)
                }

                is GoogleSignInViewModel.SignInUIState.Failed -> {
                    Text("Login failed: ${(loginUIState as GoogleSignInViewModel.SignInUIState.Failed)}")
                    // TODO show toast
                }
            }
        }
    }
}