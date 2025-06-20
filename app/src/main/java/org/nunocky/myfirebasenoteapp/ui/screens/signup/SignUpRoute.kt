package org.nunocky.myfirebasenoteapp.ui.screens.signup

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.nunocky.myfirebasenoteapp.data.UIState
import org.nunocky.myfirebasenoteapp.data.User
import org.nunocky.myfirebasenoteapp.ui.theme.myfirebasenoteappTheme
import org.nunocky.myfirebasenoteapp.validator.EmailValidator
import org.nunocky.myfirebasenoteapp.validator.PasswordValidator

private const val TAG = "SignUpScreen"

@Composable
fun SignUpRoute(
    navHostController: NavHostController,
    viewModel: SignUpViewModel,
    onSignUpSuccess: (user: User) -> Unit,
    onSignUpCancelled: () -> Unit
) {
    val signupUIState by viewModel.signupUIState.collectAsState()

    SignUpScreen(
        signupUIState,
        onSignupButtonClicked = { email, password ->
            viewModel.createAccount(email, password)
        },
        onCreateAccountSuccess = { user ->
            onSignUpSuccess(user)
        },
        onCreateAccountCancelled = {
            onSignUpCancelled()
        }
    )
}

@Composable
fun SignUpScreen(
    signupUIState: UIState,
    onSignupButtonClicked: (email: String, password: String) -> Unit = { _, _ -> }, // サインアップボタンがクリックされたときのコールバック
    onCreateAccountSuccess: (user: User) -> Unit = {},
    onCreateAccountCancelled: () -> Unit = {},
) {
    var email by rememberSaveable { mutableStateOf("") }
    val isValidEmail = EmailValidator.isValidEmail(email)

    var password by rememberSaveable { mutableStateOf("") }
    var passwordHidden by rememberSaveable { mutableStateOf(true) }

    var password2 by rememberSaveable { mutableStateOf("") }
    var password2Hidden by rememberSaveable { mutableStateOf(true) }

    // バリデーションはrememberで管理
    val isValidPassword = rememberSaveable(password, password2) {
        password == password2 && PasswordValidator.isValidPassword(password)
    }

    // パスワード不一致やバリデーションエラー時のメッセージ
    val passwordError = remember(password, password2) {
        when {
            password.isEmpty() || password2.isEmpty() -> null
            password != password2 -> "パスワードが一致しません"
            !PasswordValidator.isValidPassword(password) -> "パスワードが不正です"
            else -> null
        }
    }

    LaunchedEffect(key1 = signupUIState) {
        when (signupUIState) {
            UIState.Initial -> {}

            UIState.Processing -> {}

            is UIState.Success<*> -> {
                val user = signupUIState.data as User
                onCreateAccountSuccess(user)
            }

            UIState.Cancelled -> {}
            is UIState.Error -> {
                Log.d(TAG, "SignUpScreen: Error: ${signupUIState.e.message}")

            }
        }
    }


    Surface(
        modifier = Modifier
            .fillMaxSize()
            .testTag("background"),
        color = Color.Black.copy(alpha = 0.2f)
    ) {
        Card(
            modifier = Modifier
                .wrapContentHeight(Alignment.CenterVertically)
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(
                    modifier = Modifier.padding(top = 16.dp)
                )
                Text(
                    "MyFirebaseNoteAppへようこそ",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineLarge
                )

                // email
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = email,
                    onValueChange = {
                        email = it
                    },
                    label = { Text("Email") },
                    singleLine = true,
                )

                // password
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = password,
                    onValueChange = { password = it },
                    singleLine = true,
                    label = { Text("Password") },
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

                // password2
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = password2,
                    onValueChange = { password2 = it },
                    singleLine = true,
                    label = { Text("Password (again)") },
                    visualTransformation =
                        if (password2Hidden) PasswordVisualTransformation() else VisualTransformation.None,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    trailingIcon = {
                        IconButton(onClick = { password2Hidden = !password2Hidden }) {
                            val visibilityIcon =
                                if (password2Hidden) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                            // Please provide localized description for accessibility services
                            val description =
                                if (password2Hidden) "Show password" else "Hide password"
                            Icon(imageVector = visibilityIcon, contentDescription = description)
                        }
                    }
                )

                // パスワードエラー表示
                if (passwordError != null) {
                    Text(
                        text = passwordError,
                        color = Color.Red,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp, bottom = 4.dp)
                    )
                }

                Button(
                    enabled = isValidEmail && isValidPassword,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    onClick = {
                        onSignupButtonClicked(email, password)
                    }) {
                    Text("サインアップ")
                }
                Spacer(
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 412, heightDp = 915)
@Composable
fun SignUpScreenPreview() {
    myfirebasenoteappTheme {
        SignUpScreen(
            signupUIState = UIState.Initial,
            onSignupButtonClicked = { _, _ -> }
        )
    }
}
