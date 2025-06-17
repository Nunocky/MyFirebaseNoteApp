package org.nunocky.myfirebasenoteapp.ui.screens.login

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import org.nunocky.myfirebasenoteapp.BuildConfig
import org.nunocky.myfirebasenoteapp.data.UIState
import org.nunocky.myfirebasenoteapp.data.User
import org.nunocky.myfirebasenoteapp.ui.theme.Typography
import org.nunocky.myfirebasenoteapp.ui.theme.myfirebasenoteappTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginRoute(
    navHostController: NavHostController,
    viewModel: LoginViewModel,
    onLoginSuccess: (user: User) -> Unit,
    onLoginCancelled: () -> Unit,
) {
    val loginUIState: UIState by viewModel.signInUIState.collectAsState()

    // スワイプバックや戻るボタンで onLoginCancelled を実行
    BackHandler {
        onLoginCancelled()
    }

    LaunchedEffect(key1 = loginUIState) {
        when (loginUIState) {
            is UIState.Success<*> -> {
                val user = (loginUIState as UIState.Success<*>).data as? User
                    ?: throw RuntimeException("Expected User type")

                // ログイン成功時の処理
                viewModel.registerUser(user)
                onLoginSuccess(user)
            }

            is UIState.Error -> {
                // 必要に応じてエラー処理
            }

            else -> { /* 何もしない */
            }
        }
    }

    LoginScreen(
        loginUIState = loginUIState,
        onLoginRequest = {
            val googleClientId = BuildConfig.WEB_CLIENT_ID
            viewModel.signInWithGoogle(googleClientId)
        },
        onLoginCancelled = onLoginCancelled,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    loginUIState: UIState,
    onLoginRequest: () -> Unit,
    onLoginCancelled: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Login", style = Typography.titleLarge) },
            )
        },
    ) { innerPadding ->
        val buttonEnabled = loginUIState !is UIState.Processing

        Column(modifier = Modifier.padding(innerPadding)) {
            Button(
                onClick = {
                    onLoginRequest()
                },
                enabled = buttonEnabled
            ) {
                Text("Sign in with Google")
            }

            when (loginUIState) {
                is UIState.Initial -> {
                }

                is UIState.Processing -> {
                    Text("Processing...")
                }

                is UIState.Success<*> -> {
                    val user = loginUIState.data as User
                    Text("Login successful: ${user.displayName}")
                }

                is UIState.Error -> {
                    Text("Login failed: $loginUIState")
                }

                is UIState.Cancelled -> {}
            }
        }
    }
}

@Preview
@Composable
fun LoginScreenPreview() {
    myfirebasenoteappTheme {
        LoginScreen(
            loginUIState = UIState.Initial,
            onLoginRequest = {},
            onLoginCancelled = {},
        )
    }
}