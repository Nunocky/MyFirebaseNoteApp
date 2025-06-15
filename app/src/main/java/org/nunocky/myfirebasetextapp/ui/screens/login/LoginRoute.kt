package org.nunocky.myfirebasetextapp.ui.screens.login

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import org.nunocky.myfirebasetextapp.BuildConfig
import org.nunocky.myfirebasetextapp.data.SignInUIState
import org.nunocky.myfirebasetextapp.ui.theme.MyFirebaseTextAppTheme
import org.nunocky.myfirebasetextapp.ui.theme.Typography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginRoute(
    navHostController: NavHostController,
    onLoginSuccess: (user: FirebaseUser) -> Unit,
    onLoginCancelled: () -> Unit,
    googleSignInViewModel: GoogleSignInViewModel
) {
    // スワイプバックや戻るボタンで onLoginCancelled を実行
    BackHandler {
        onLoginCancelled()
    }

    val loginUIState: SignInUIState by googleSignInViewModel.signInUIState.collectAsState()

    LaunchedEffect(key1 = loginUIState) {
        when (loginUIState) {
            is SignInUIState.Success -> {

                // FireStore インスタンスを取得
                val db = FirebaseFirestore.getInstance()

                // 現在サインインしているユーザーを取得
                val user = FirebaseAuth.getInstance().currentUser

                user?.let {
                    val userId = it.uid // ユーザーのUIDを取得
                    val displayName = it.displayName ?: "名無しさん" // 表示名を取得、またはデフォルト値
                    val email = it.email ?: "メールアドレスなし" // メールアドレスを取得、またはデフォルト値

                    // 保存したいユーザーデータのMapを作成
                    val userData = hashMapOf(
                        "displayName" to displayName,
                        "email" to email,
                        "createdAt" to com.google.firebase.Timestamp.now() // ユーザー作成日時なども追加できます
                    )

                    // usersコレクションの、ユーザーのUIDをIDとしたドキュメントにデータを書き込む
                    db.collection("users").document(userId)
                        .set(userData) // set()を使うと、ドキュメントがなければ作成、あれば上書き
                        .addOnSuccessListener {
                            // 保存成功！
                            println("ユーザー情報を Firestore に保存しました: $userId")
                        }
                        .addOnFailureListener { e ->
                            // 保存失敗...
                            println("ユーザー情報の保存に失敗しました: $e")
                        }
                }

                onLoginSuccess((loginUIState as SignInUIState.Success).user)
            }

            is SignInUIState.Failed -> {
                // 必要に応じてエラー処理
            }

            else -> { /* 何もしない */
            }
        }
    }

    LoginScreen(
        onLoginRequest = {
            val googleClientId = BuildConfig.WEB_CLIENT_ID
            googleSignInViewModel.signIn(googleClientId)
        },
        onLoginCancelled = onLoginCancelled,
        loginUIState = loginUIState
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginRequest: () -> Unit,
    onLoginCancelled: () -> Unit,
    loginUIState: SignInUIState
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Login", style = Typography.titleLarge) },
                navigationIcon = {
                    // Add a back button to the top app bar
                    IconButton(onClick = onLoginCancelled) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
    ) { innerPadding ->
        val buttonEnabled = loginUIState !is SignInUIState.Processing

        Column(modifier = Modifier.padding(innerPadding)) {
            Button(
                onClick = {
                    onLoginRequest()
                },
                enabled = buttonEnabled
            ) {
                Text("Sign in with Google")
            }

            when (loginUIState) {
                is SignInUIState.Initial -> {
                }

                is SignInUIState.Processing -> {
                    Text("Processing...")
                }

                is SignInUIState.Success -> {
                    Text("Login successful: ${loginUIState.user.displayName}")
                }

                is SignInUIState.Failed -> {
                    Text("Login failed: $loginUIState")
                }
            }
        }
    }
}

@Preview
@Composable
fun LoginScreenPreview() {
    MyFirebaseTextAppTheme {
        LoginScreen(
            onLoginRequest = {},
            onLoginCancelled = {},
            loginUIState = SignInUIState.Initial
        )
    }
}