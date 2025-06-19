package org.nunocky.myfirebasenoteapp.ui.screens.resetpassword

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import org.nunocky.myfirebasenoteapp.ui.theme.myfirebasenoteappTheme


@Composable
fun ResetPasswordRoute(
    navHostController: NavHostController,
    viewModel: ResetPasswordViewModel,

    onResetPasswordSuccess: () -> Unit,
    onResetPasswordCancelled: () -> Unit
) {
    ResetPasswordScreen()
}

@Composable
fun ResetPasswordScreen() {
}

@Preview(showBackground = true, widthDp = 1080, heightDp = 1920)
@Composable
fun ResetPasswordScreenPreview() {
    myfirebasenoteappTheme {
        ResetPasswordScreen()
    }
}
