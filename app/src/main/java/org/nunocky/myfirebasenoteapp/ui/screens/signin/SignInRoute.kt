package org.nunocky.myfirebasenoteapp.ui.screens.signin

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.ui.res.stringResource
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
import org.nunocky.myfirebasenoteapp.R
import org.nunocky.myfirebasenoteapp.data.UIState
import org.nunocky.myfirebasenoteapp.data.User
import org.nunocky.myfirebasenoteapp.ui.theme.myfirebasenoteappTheme
import org.nunocky.myfirebasenoteapp.validator.EmailValidator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInRoute(
    navHostController: NavHostController,
    viewModel: SignInViewModel,
    onLoginSuccess: (user: User) -> Unit,
    onLoginCancelled: () -> Unit,
    onRequestResetPassword: () -> Unit,
    onRequestCreateAccount: () -> Unit,
    snackbarMessage: String = "",
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val loginUIState: UIState by viewModel.signInUIState.collectAsState()

    // スワイプバックや戻るボタンで onLoginCancelled を実行
    BackHandler {
        onLoginCancelled()
    }

    if (snackbarMessage.isNotEmpty()) {
        LaunchedEffect(snackbarMessage) {
            snackbarHostState.showSnackbar(snackbarMessage)
        }
    }

    LaunchedEffect(key1 = loginUIState) {
        when (loginUIState) {
            is UIState.Success<*> -> {
                val user = (loginUIState as UIState.Success<*>).data as? User
                    ?: throw RuntimeException("unexpected data type: expected User, but got ${(loginUIState as UIState.Success<*>).data?.javaClass?.name ?: "null"}")

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

    SignInScreen(
        loginUIState = loginUIState,
        snackbarHostState = snackbarHostState,

        onGoogleLoginRequest = {
            val googleClientId = BuildConfig.WEB_CLIENT_ID
            viewModel.signInWithGoogle(googleClientId)
        },

        onEmailLoginRequest = { email, password ->
            viewModel.signInWithEmail(email, password)
        },

        onResetPasswordRequest = {
            onRequestResetPassword()
        },

        onCreateAccountRequest = {
            onRequestCreateAccount()
        },

        onLoginCancelled = onLoginCancelled,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInScreen(
    loginUIState: UIState,
    snackbarHostState: SnackbarHostState,
    onGoogleLoginRequest: () -> Unit,
    onEmailLoginRequest: (email: String, password: String) -> Unit,
    onResetPasswordRequest: () -> Unit,
    onCreateAccountRequest: () -> Unit,
    onLoginCancelled: () -> Unit
) {
    var email by rememberSaveable { mutableStateOf("") }
    var isValidEmail by rememberSaveable { mutableStateOf(false) }

    var password by rememberSaveable { mutableStateOf("") }
    var passwordHidden by rememberSaveable { mutableStateOf(true) }
    var isValidPassword = password.isNotEmpty() // ログインではバリデーションを行わない(コンソールで設定されている可能性がある)

    val buttonEnabled = loginUIState !is UIState.Processing && loginUIState !is UIState.Success<*>
    val textEditable = buttonEnabled

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth()
                .fillMaxHeight(),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column {
                    Spacer(
                        modifier = Modifier.padding(top = 16.dp)
                    )

                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        onClick = {
                            onGoogleLoginRequest()
                        },
                        enabled = buttonEnabled
                    ) {
                        Text(stringResource(R.string.sign_in_with_google))
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
                            onValueChange = {
                                email = it
                                isValidEmail = EmailValidator.isValidEmail(it)
                            },
                            label = { Text(stringResource(R.string.email)) },
                            singleLine = true,
                            enabled = textEditable,
                        )

                        TextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = password,
                            onValueChange = { password = it },
                            singleLine = true,
                            label = { Text(stringResource(R.string.enter_password)) },
                            visualTransformation = if (passwordHidden) PasswordVisualTransformation() else VisualTransformation.None,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            trailingIcon = {
                                IconButton(onClick = { passwordHidden = !passwordHidden }) {
                                    val visibilityIcon =
                                        if (passwordHidden) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                                    // Please provide localized description for accessibility services
                                    val description =
                                        if (passwordHidden) stringResource(R.string.show_password) else stringResource(
                                            R.string.hide_password
                                        )
                                    Icon(
                                        imageVector = visibilityIcon,
                                        contentDescription = description
                                    )
                                }
                            },
                            enabled = textEditable,
                        )

                        Button(
                            enabled = isValidEmail && isValidPassword,
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                onEmailLoginRequest(email, password)
                            }) {
                            Text("Login")
                        }

                        TextButton(onClick = {
                            onResetPasswordRequest()
                        }) {
                            Text(stringResource(R.string.reset_password))
                        }

                        val LINK_TAG = "linkTag"

                        val annotatedText = buildAnnotatedString {
                            append(stringResource(R.string.don_t_have_a_password))

                            val start = length
                            val linkText = stringResource(R.string.create_an_account)
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
                                ), start = start, end = end
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
                            })

                        when (loginUIState) {
                            is UIState.Initial -> {
                                Text("")
                            }

                            is UIState.Processing -> {
                                Text(stringResource(R.string.processing))
                            }

                            is UIState.Success<*> -> {
                                Text(stringResource(R.string.sign_in_successful))
                            }

                            is UIState.Error -> {
                                Text(stringResource(R.string.sign_in_failed))
                            }

                            is UIState.Cancelled -> {
                                Text("")
                            }
                        }
                        Spacer(
                            modifier = Modifier.padding(top = 16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 412, heightDp = 915)
@Composable
fun SignInScreenPreview_Initial() {
    myfirebasenoteappTheme {
        SignInScreen(
            loginUIState = UIState.Initial,
            snackbarHostState = remember { SnackbarHostState() },
            onGoogleLoginRequest = {},
            onEmailLoginRequest = { _, _ -> },
            onResetPasswordRequest = {},
            onCreateAccountRequest = {},
            onLoginCancelled = { })
    }
}

@Preview(showBackground = true, widthDp = 412, heightDp = 915)
@Composable
fun SignInScreenPreview_Processing() {
    myfirebasenoteappTheme {
        SignInScreen(
            loginUIState = UIState.Processing,
            snackbarHostState = remember { SnackbarHostState() },
            onGoogleLoginRequest = {},
            onEmailLoginRequest = { _, _ -> },
            onResetPasswordRequest = {},
            onCreateAccountRequest = {},
            onLoginCancelled = { })
    }
}

@Preview(showBackground = true, widthDp = 412, heightDp = 915)
@Composable
fun SignInScreenPreview_Success() {
    myfirebasenoteappTheme {
        SignInScreen(
            loginUIState = UIState.Success(
                User(
                    uid = "12345",
                    displayName = "Test User",
                    email = "user@example.com",
                    photoUrl = "https://example.com/photo.jpg",
                    emailVerified = true,
                )
            ),
            snackbarHostState = remember { SnackbarHostState() },
            onGoogleLoginRequest = {},
            onEmailLoginRequest = { _, _ -> },
            onResetPasswordRequest = {},
            onCreateAccountRequest = {},
            onLoginCancelled = { })
    }
}

@Preview(showBackground = true, widthDp = 412, heightDp = 915)
@Composable
fun SignInScreenPreview_Error() {
    myfirebasenoteappTheme {
        SignInScreen(
            loginUIState = UIState.Error(
                Exception("Login failed due to network error")
            ),
            snackbarHostState = remember { SnackbarHostState() },
            onGoogleLoginRequest = {},
            onEmailLoginRequest = { _, _ -> },
            onResetPasswordRequest = {},
            onCreateAccountRequest = {},
            onLoginCancelled = { })
    }
}