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
object SignIn

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
        composable<Home> { _ ->
            HomeRoute(
                navHostController,
                viewModel = hiltViewModel<HomeViewModel>(),
                onLoginNeeded = { navHostController.navigate(SignIn) },
                onCreateNewItem = { navHostController.navigate(NewItem) },
                onRequestEditItem = { itemId ->
                    navHostController.navigate(EditItem(itemId = itemId))
                }
            )
        }
        composable<SignIn> { _ ->
            SignInRoute(
                navHostController,
                viewModel = hiltViewModel<SignInViewModel>(),
                onLoginSuccess = { _ ->
                    navHostController.popBackStack()
                },
                onLoginCancelled = {
                    // アプリケーション終了
                    (context as? Activity)?.finish()
                },
                onRequestResetPassword = {
                    navHostController.navigate(ResetPassword)
                },
                onRequestCreateAccount = {
                    navHostController.navigate(SignUp)
                },
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
        composable<ResetPassword> {
            val viewModel = hiltViewModel<ResetPasswordViewModel>()
            ResetPasswordRoute(
                navHostController = navHostController,
                viewModel = viewModel,
                onResetPasswordSuccess = {
                    navHostController.popBackStack(Home, inclusive = false)
                },
                onResetPasswordCancelled = {
                    navHostController.popBackStack(Home, inclusive = false)
                }
            )
        }

        composable<SignUp> {
            val viewModel = hiltViewModel<SignUpViewModel>()
            SignUpRoute(
                navHostController = navHostController,
                viewModel = viewModel,
                onCreateAccountSuccess = {
                    navHostController.popBackStack(Home, inclusive = false)
                },
                onCreateAccountCancelled = {
                    navHostController.popBackStack(Home, inclusive = false)
                }
            )
        }
    }
}
