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
import org.nunocky.myfirebasenoteapp.ui.screens.createaccount.CreateAccountRoute
import org.nunocky.myfirebasenoteapp.ui.screens.createaccount.CreateAccountViewModel
import org.nunocky.myfirebasenoteapp.ui.screens.edit.EditItemRoute
import org.nunocky.myfirebasenoteapp.ui.screens.edit.EditItemViewModel
import org.nunocky.myfirebasenoteapp.ui.screens.home.HomeRoute
import org.nunocky.myfirebasenoteapp.ui.screens.home.HomeViewModel
import org.nunocky.myfirebasenoteapp.ui.screens.login.LoginRoute
import org.nunocky.myfirebasenoteapp.ui.screens.login.LoginViewModel
import org.nunocky.myfirebasenoteapp.ui.screens.resetpassword.ResetPasswordRoute
import org.nunocky.myfirebasenoteapp.ui.screens.resetpassword.ResetPasswordViewModel

@Serializable
object Home

@Serializable
object Login

@Serializable
object ResetPassword

@Serializable
object CreateAccount

@Serializable
object NewItem

@Serializable
data class EditItem(
    var itemId: String
)

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
                onLoginNeeded = { navHostController.navigate(Login) },
                onCreateNewItem = { navHostController.navigate(NewItem) },
                onRequestEditItem = { itemId ->
                    navHostController.navigate(EditItem(itemId = itemId))
                }
            )
        }
        composable<Login> { _ ->
            LoginRoute(
                navHostController,
                viewModel = hiltViewModel<LoginViewModel>(),
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
                    navHostController.navigate(CreateAccount)
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

        composable<CreateAccount> {
            val viewModel = hiltViewModel<CreateAccountViewModel>()
            CreateAccountRoute(
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
