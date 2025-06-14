package org.nunocky.myfirebasetextapp.ui.screens.login

import android.app.Application
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.nunocky.myfirebasetextapp.BuildConfig
import javax.inject.Inject

/**
 * Google Sign In
 * very thanks to https://qiita.com/kisayama/items/5dc7618b76f6d86a6d55
 */

@HiltViewModel
class GoogleSignInViewModel @Inject constructor(
    private val application: Application
) : ViewModel() {
    sealed class SignInUIState {
        data object Initial : SignInUIState()
        data object Processing : SignInUIState()
        class Success(val user: FirebaseUser) : SignInUIState()
        class Failed(e: Exception) : SignInUIState()
    }

    private val _signInUIState = MutableStateFlow<SignInUIState>(SignInUIState.Initial)
    val signInUIState = _signInUIState.asStateFlow()

    private fun getSignInRequest(): GetCredentialRequest {
        val googleClientId = BuildConfig.WEB_CLIENT_ID

        // Googleサインインリクエストを作成
        val googleIdOption: GetSignInWithGoogleOption = GetSignInWithGoogleOption.Builder(
            googleClientId
        ) // nonceがあれば設定
            .build()

        // GetCredentialRequestを作成
        return GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()
    }

    fun signIn() {
        viewModelScope.launch {
            try {
                _signInUIState.value = SignInUIState.Processing

                val auth: FirebaseAuth = FirebaseAuth.getInstance()
                val credentialManager = CredentialManager.create(application)

                val request = getSignInRequest()

                val credential = try {
                    val result =
                        credentialManager.getCredential(context = application, request = request)
                    result.credential
                } catch (e: Exception) {
                    throw e
                }

                // 取得した資格情報がGoogle IDトークンであるか確認
                if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    val googleIdTokenCredential =
                        GoogleIdTokenCredential.createFrom(credential.data)

                    // IDトークンを取得
                    val googleIdToken = googleIdTokenCredential.idToken

                    // Firebaseの認証情報を作成
                    val firebaseCredential = GoogleAuthProvider.getCredential(googleIdToken, null)

                    // Firebaseでサインイン処理を実行
                    val authResult = auth.signInWithCredential(firebaseCredential).await()

                    // サインイン成功時のユーザー情報を取得
                    val firebaseUser = authResult.user

                    if (firebaseUser != null) {
                        _signInUIState.value = SignInUIState.Success(firebaseUser)
                    }
                } else {
                    // アカウント情報が見つからない時の処理
                    _signInUIState.value = SignInUIState.Failed(Exception("account not found"))
                }
            } catch (e: Exception) {
                _signInUIState.value = SignInUIState.Failed(e)
            }
        }
    }
}