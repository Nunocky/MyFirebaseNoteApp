package org.nunocky.myfirebasenoteapp.ui.screens.login

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.nunocky.myfirebasenoteapp.BuildConfig
import org.nunocky.myfirebasenoteapp.data.UIState
import org.nunocky.myfirebasenoteapp.data.User
import org.nunocky.myfirebasenoteapp.ui.theme.myfirebasenoteappTheme


// TODO
// - email. password テキストバリデータ
// - email. password ログインの実装
// - パスワードリセット画面
// - アカウント作成画面

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

        onGoogleLoginRequest = {
            val googleClientId = BuildConfig.WEB_CLIENT_ID
            viewModel.signInWithGoogle(googleClientId)
        },

        onEmailLoginRequest = { email, password ->
            viewModel.signInWithEmail(email, password)
        },

        onResetPasswordRequest = {
            Log.d("LoginScreen", "Forgot password clicked")
        },

        onCreateAccountRequest = {
            Log.d("LoginScreen", "Create account clicked")
            // TODO IMPLEMENT THIS
        },

        onLoginCancelled = onLoginCancelled,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    loginUIState: UIState,
    onGoogleLoginRequest: () -> Unit,
    onEmailLoginRequest: (email: String, password: String) -> Unit,
    onResetPasswordRequest: () -> Unit,
    onCreateAccountRequest: () -> Unit,
    onLoginCancelled: () -> Unit
) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordHidden by rememberSaveable { mutableStateOf(true) }

    // TODO 現状 Google ログインにしか対応していない。email ログインにも対応する。
    val buttonEnabled = loginUIState !is UIState.Processing

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .testTag("background"),
        color = Color.Black.copy(alpha = 0.2f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(Alignment.CenterVertically)
        ) {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                onClick = {
                    onGoogleLoginRequest()
                },
                enabled = buttonEnabled
            ) {
                Text("Sign in with Google")
            }

            Text(
                "または",
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(Alignment.CenterVertically)
                    .padding(top = 8.dp, bottom = 8.dp),
                textAlign = TextAlign.Center
            )

            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = email,
                    onValueChange = {},
                    label = { Text("Email") },
                    singleLine = true,
                )

                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = password,
                    onValueChange = { password = it },
                    singleLine = true,
                    label = { Text("Enter password") },
                    visualTransformation =
                        if (passwordHidden) PasswordVisualTransformation() else VisualTransformation.None,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    trailingIcon = {
                        IconButton(onClick = { passwordHidden = !passwordHidden }) {
                            val visibilityIcon =
                                if (passwordHidden) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                            // Please provide localized description for accessibility services
                            val description =
                                if (passwordHidden) "Show password" else "Hide password"
                            Icon(imageVector = visibilityIcon, contentDescription = description)
                        }
                    }
                )

                Button(
                    enabled = email.isNotBlank() && password.isNotBlank(),
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        onEmailLoginRequest(email, password)
                    }) {
                    Text("Login")
                }

                TextButton(onClick = {
                    onResetPasswordRequest()
                }) {
                    Text("Reset password")
                }

                val LINK_TAG = "linkTag"

                val annotatedText = buildAnnotatedString {
                    append("パスワードを持っていませんか? ")

                    val start = length
                    val linkText = "アカウントを作成してください"
                    append(linkText)
                    val end = length

                    // LINK_TAG アノテーションを付加する
                    addStringAnnotation(
                        tag = LINK_TAG,
                        annotation = linkText,
                        start = start,
                        end = end,
                    )
                    // リンク色とアンダーラインを追加
                    addStyle(
                        style = SpanStyle(
                            color = MaterialTheme.colorScheme.primary,
                            textDecoration = TextDecoration.Underline
                        ),
                        start = start,
                        end = end
                    )
                }

                var layoutResult by remember { mutableStateOf<TextLayoutResult?>(null) }

                BasicText(
                    text = annotatedText,
                    onTextLayout = { layoutResult = it },
                    modifier = Modifier.pointerInput(Unit) {
                        detectTapGestures { offset ->
                            layoutResult?.let { layout ->
                                val position = layout.getOffsetForPosition(offset)
                                val linkAnnotation = annotatedText.getStringAnnotations(
                                    tag = LINK_TAG, start = position, end = position
                                ).firstOrNull()

                                if (linkAnnotation != null) {
                                    onCreateAccountRequest()
                                }
                            }
                        }
                    }
                )
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
            onGoogleLoginRequest = {},
            onEmailLoginRequest = { _, _ -> },
            onResetPasswordRequest = {},
            onCreateAccountRequest = {},
            onLoginCancelled = { }
        )
    }
}