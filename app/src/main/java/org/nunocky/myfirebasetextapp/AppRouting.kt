package org.nunocky.myfirebasetextapp

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable
import org.nunocky.myfirebasetextapp.ui.screens.home.HomeScreen
import org.nunocky.myfirebasetextapp.ui.screens.login.GoogleSignInViewModel
import org.nunocky.myfirebasetextapp.ui.screens.login.LoginScreen

@Serializable
object Home

@Serializable
object Login

@Composable
fun AppRouting() {
    val navHostController = rememberNavController()

    NavHost(
        navController = navHostController,
        startDestination = Home
    ) {
        composable<Home> { _ ->
            HomeScreen(
                navHostController,
                onLoginNeeded = { navHostController.navigate(Login) }
            )
        }
        composable<Login> { _ ->
            val viewModel = hiltViewModel<GoogleSignInViewModel>()
            val context = LocalContext.current

            LoginScreen(
                navHostController,
                onLoginSuccess = { _ ->
                    navHostController.popBackStack()
                },
                onLoginCancelled = {
                    // アプリケーション終了
                    (context as? Activity)?.finish()
                },
                googleSignInViewModel = viewModel,
            )
        }
    }
}
