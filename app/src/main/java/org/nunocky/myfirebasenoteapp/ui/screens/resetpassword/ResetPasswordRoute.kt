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

@Preview(showBackground = true, widthDp = 412, heightDp = 915)
@Composable
fun ResetPasswordScreenPreview() {
    myfirebasenoteappTheme {
        ResetPasswordScreen()
    }
}
