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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseUser
import org.nunocky.myfirebasetextapp.BuildConfig
import org.nunocky.myfirebasetextapp.data.SignInUIState
import org.nunocky.myfirebasetextapp.ui.theme.MyFirebaseTextAppTheme
import org.nunocky.myfirebasetextapp.ui.theme.Typography


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginRoute(
    navHostController: NavHostController,
    viewModel: LoginViewModel,
    onLoginSuccess: (user: FirebaseUser) -> Unit,
    onLoginCancelled: () -> Unit,
) {
    val loginUIState: SignInUIState by viewModel.signInUIState.collectAsState()

    // スワイプバックや戻るボタンで onLoginCancelled を実行
    BackHandler {
        onLoginCancelled()
    }

    LaunchedEffect(key1 = loginUIState) {
        when (loginUIState) {
            is SignInUIState.Success -> {
                (loginUIState as SignInUIState.Success).user.let {
                    viewModel.registerUser(it)
                    onLoginSuccess(it)
                }
            }

            is SignInUIState.Failed -> {
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
    loginUIState: SignInUIState,
    onLoginRequest: () -> Unit,
    onLoginCancelled: () -> Unit,
) {
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
        val buttonEnabled = loginUIState !is SignInUIState.Processing

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
                is SignInUIState.Initial -> {
                }

                is SignInUIState.Processing -> {
                    Text("Processing...")
                }

                is SignInUIState.Success -> {
                    Text("Login successful: ${loginUIState.user.displayName}")
                }

                is SignInUIState.Failed -> {
                    Text("Login failed: $loginUIState")
                }
            }
        }
    }
}

@Preview
@Composable
fun LoginScreenPreview() {
    MyFirebaseTextAppTheme {
        LoginScreen(
            loginUIState = SignInUIState.Initial,
            onLoginRequest = {},
            onLoginCancelled = {},
        )
    }
}