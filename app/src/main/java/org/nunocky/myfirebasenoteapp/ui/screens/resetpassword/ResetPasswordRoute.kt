package org.nunocky.myfirebasenoteapp.ui.screens.resetpassword

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.nunocky.myfirebasenoteapp.R
import org.nunocky.myfirebasenoteapp.data.UIState
import org.nunocky.myfirebasenoteapp.ui.theme.myfirebasenoteappTheme
import org.nunocky.myfirebasenoteapp.validator.EmailValidator


@Composable
fun ResetPasswordRoute(
    navHostController: NavHostController,
    viewModel: ResetPasswordViewModel,
    onResetPasswordSuccess: () -> Unit,
    onResetPasswordCancelled: () -> Unit
) {
    val resetUIState by viewModel.resetUIState.collectAsState()

    LaunchedEffect(key1 = resetUIState) {
        when (resetUIState) {
            is UIState.Success<*> -> {
                onResetPasswordSuccess()
            }

            is UIState.Error -> {
                onResetPasswordCancelled()
            }

            else -> {}
        }
    }

    ResetPasswordScreen(
        resetUIState = resetUIState,
        onResetEmailRequest = { email ->
            viewModel.requestResetPassword(email)
        },
    )
}

@Composable
fun ResetPasswordScreen(
    resetUIState: UIState,
    onResetEmailRequest: (String) -> Unit = {},
) {
    var email by rememberSaveable { mutableStateOf("") }
    var isValidEmail = EmailValidator.isValidEmail(email)
    val buttonEnabled = resetUIState !is UIState.Processing && resetUIState !is UIState.Success<*>
    val textEditable = buttonEnabled
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
            Column {
                Spacer(
                    modifier = Modifier.padding(top = 16.dp)
                )

                Text(
                    stringResource(R.string.reset_password),
                    modifier = Modifier.padding(horizontal = 16.dp),
                    style = MaterialTheme.typography.headlineLarge
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
                        },
                        label = { Text(stringResource(R.string.email)) },
                        singleLine = true,
                        enabled = textEditable,
                    )

                    Button(
                        enabled = isValidEmail, modifier = Modifier.fillMaxWidth(), onClick = {
                            onResetEmailRequest(email)
                        }) {
                        Text(stringResource(R.string.reset_password))
                    }
                }
            }

            when (resetUIState) {
                is UIState.Initial -> {
                    Text(
                        text = "", modifier = Modifier.padding(top = 16.dp)
                    )
                }

                is UIState.Processing -> {
                    Text(
                        text = stringResource(R.string.processing),
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }

                is UIState.Success<*> -> {
                    Text(
                        text = "", modifier = Modifier.padding(top = 16.dp)
                    )
                }

                is UIState.Error -> {
                    Text(
                        text = stringResource(R.string.reset_password_failed),
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }

                UIState.Cancelled -> {}
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 412, heightDp = 915)
@Composable
fun ResetPasswordScreenPreview() {
    myfirebasenoteappTheme {
        ResetPasswordScreen(
            resetUIState = UIState.Initial,
            onResetEmailRequest = { _ -> },
        )
    }
}
