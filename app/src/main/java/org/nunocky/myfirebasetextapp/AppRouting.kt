package org.nunocky.myfirebasetextapp

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable
import org.nunocky.myfirebasetextapp.ui.screens.create.NewItemRoute
import org.nunocky.myfirebasetextapp.ui.screens.create.NewItemViewModel
import org.nunocky.myfirebasetextapp.ui.screens.edit.EditItemRoute
import org.nunocky.myfirebasetextapp.ui.screens.edit.EditItemViewModel
import org.nunocky.myfirebasetextapp.ui.screens.home.HomeRoute
import org.nunocky.myfirebasetextapp.ui.screens.home.HomeViewModel
import org.nunocky.myfirebasetextapp.ui.screens.login.LoginRoute
import org.nunocky.myfirebasetextapp.ui.screens.login.LoginViewModel

@Serializable
object Home

@Serializable
object Login

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
