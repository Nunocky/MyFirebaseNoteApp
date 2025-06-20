package org.nunocky.myfirebasenoteapp

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable
import org.nunocky.myfirebasenoteapp.ui.screens.create.NewItemRoute
import org.nunocky.myfirebasenoteapp.ui.screens.create.NewItemViewModel
import org.nunocky.myfirebasenoteapp.ui.screens.edit.EditItemRoute
import org.nunocky.myfirebasenoteapp.ui.screens.edit.EditItemViewModel
import org.nunocky.myfirebasenoteapp.ui.screens.home.HomeRoute
import org.nunocky.myfirebasenoteapp.ui.screens.home.HomeViewModel
import org.nunocky.myfirebasenoteapp.ui.screens.resetpassword.ResetPasswordRoute
import org.nunocky.myfirebasenoteapp.ui.screens.resetpassword.ResetPasswordViewModel
import org.nunocky.myfirebasenoteapp.ui.screens.signin.SignInRoute
import org.nunocky.myfirebasenoteapp.ui.screens.signin.SignInViewModel
import org.nunocky.myfirebasenoteapp.ui.screens.signup.SignUpRoute
import org.nunocky.myfirebasenoteapp.ui.screens.signup.SignUpViewModel

@Serializable
object Home

@Serializable
object NewItem

@Serializable
data class EditItem(
    var itemId: String
)

@Serializable
data class SignIn(
    var snackbarMessage: String? = null // デフォルト値をnullに
)

@Serializable
object SignUp

@Serializable
object ResetPassword

@Composable
fun AppRouting() {
    val context = LocalContext.current
    val navHostController = rememberNavController()

    NavHost(
        navController = navHostController,
        startDestination = Home
    ) {
        composable<Home> { backStackEntry ->
            HomeRoute(
                navHostController,
                viewModel = hiltViewModel<HomeViewModel>(),
                onLoginNeeded = { message ->
                    navHostController.navigate(SignIn(message))
                },
                onCreateNewItem = { navHostController.navigate(NewItem) },
                onRequestEditItem = { itemId ->
                    navHostController.navigate(EditItem(itemId))
                },
            )
        }

        composable<SignIn> { backStackEntry ->
            val snackbarMessage = backStackEntry.toRoute<SignIn>().snackbarMessage
            SignInRoute(
                navHostController,
                viewModel = hiltViewModel<SignInViewModel>(),
                onLoginSuccess = { _ ->
                    navHostController.popBackStack()
                },
                onLoginCancelled = {
                    (context as? Activity)?.finish() // アプリケーション終了
                },
                onRequestResetPassword = {
                    navHostController.navigate(ResetPassword)
                },
                onRequestCreateAccount = {
                    navHostController.navigate(SignUp)
                },
                snackbarMessage = snackbarMessage ?: ""
            )
        }

        composable<SignUp> {
            val viewModel = hiltViewModel<SignUpViewModel>()
            SignUpRoute(
                navHostController = navHostController,
                viewModel = viewModel,
                onSignUpSuccess = {
                    val message = context.getString(R.string.account_created)
                    // navigation argumentsでSignInにメッセージを渡す
                    navHostController.navigate(SignIn(message)) {
                        popUpTo<SignIn> { inclusive = true }
                    }
                },
                onSignUpCancelled = {
                    navHostController.popBackStack<SignIn>(inclusive = false)
                }
            )
        }

        composable<ResetPassword> {
            val viewModel = hiltViewModel<ResetPasswordViewModel>()
            ResetPasswordRoute(
                navHostController = navHostController,
                viewModel = viewModel,
                onResetPasswordSuccess = {
                    val message = context.getString(R.string.email_sent_to_reset_password)
                    navHostController.navigate(SignIn(message)) {
                        popUpTo<SignIn> { inclusive = true }
                    }
                },
                onResetPasswordCancelled = {
                    navHostController.popBackStack()
                }
            )
        }

        composable<NewItem> { _ ->
            NewItemRoute(
                navHostController = navHostController,
                viewModel = hiltViewModel<NewItemViewModel>(),
                onSaveSuccess = { navHostController.popBackStack() },
                onEditCancelled = { navHostController.popBackStack() },
            )
        }

        composable<EditItem> { backStackEntry ->
            val args: EditItem = backStackEntry.toRoute()
            EditItemRoute(
                navHostController = navHostController,
                viewModel = hiltViewModel<EditItemViewModel>(),
                itemId = args.itemId,
                onSaveSuccess = { navHostController.popBackStack() },
                onEditCancelled = { navHostController.popBackStack() },
            )
        }
    }
}