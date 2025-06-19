package org.nunocky.myfirebasenoteapp.ui.screens.createaccount

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
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
import org.nunocky.myfirebasenoteapp.ui.theme.myfirebasenoteappTheme
import org.nunocky.myfirebasenoteapp.validator.EmailValidator
import org.nunocky.myfirebasenoteapp.validator.PasswordValidator


@Composable
fun CreateAccountRoute(
    navHostController: NavHostController,
    viewModel: CreateAccountViewModel,
    onCreateAccountSuccess: () -> Unit,
    onCreateAccountCancelled: () -> Unit
) {
    val signupUIState by viewModel.signupUIState.collectAsState()

    CreateAccountScreen(
        signupUIState,
        onSignupButtonClicked = { email, password ->
            viewModel.createAccount(email, password)
        },
        onCreateAccountSuccess = {
            onCreateAccountSuccess()
        },
        onCreateAccountCancelled = {
            onCreateAccountCancelled()
        }
    )
}

@Composable
fun CreateAccountScreen(
    signupUIState: UIState,
    onSignupButtonClicked: (email: String, password: String) -> Unit = { _, _ -> }, // サインアップボタンがクリックされたときのコールバック
    onCreateAccountSuccess: () -> Unit = {},
    onCreateAccountCancelled: () -> Unit = {},
) {
    var email by rememberSaveable { mutableStateOf("") }
    var isValueEmail by rememberSaveable { mutableStateOf(false) }

    var password by rememberSaveable { mutableStateOf("") }
    var passwordHidden by rememberSaveable { mutableStateOf(true) }

    var password2 by rememberSaveable { mutableStateOf("") }
    var password2Hidden by rememberSaveable { mutableStateOf(true) }

    var isValuePassword = password.equals(password2) && PasswordValidator.isValidPassword(password)

    LaunchedEffect(key1 = signupUIState) {
        when (signupUIState) {
            UIState.Initial -> {
                // 初期状態では何もしない
            }

            UIState.Processing -> {}

            is UIState.Success<*> -> {
                onCreateAccountSuccess()
            }

            UIState.Cancelled -> {}
            is UIState.Error -> {}
        }
    }


    Surface(
        modifier = Modifier
            .fillMaxSize()
            .testTag("background"),
        color = Color.Black.copy(alpha = 0.2f)
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .wrapContentHeight(Alignment.CenterVertically)
        ) {
            Text(
                "MyFirebaseNoteAppへようこそ",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineLarge
            )

            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // email
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = email,
                    onValueChange = {
                        email = it
                        isValueEmail = EmailValidator.isValidEmail(it)
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

                // password2
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = password2,
                    onValueChange = { password2 = it },
                    singleLine = true,
                    label = { Text("Enter password again") },
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

                Button(
                    enabled = isValueEmail && isValuePassword,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    onClick = {
                        onSignupButtonClicked(email, password)
                    }) {
                    Text("サインアップ")
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 1080, heightDp = 1920)
@Composable
fun CreateAccountScreenPreview() {
    myfirebasenoteappTheme {
        CreateAccountScreen(
            signupUIState = UIState.Initial,
            onSignupButtonClicked = { _, _ -> }
        )
    }
}
